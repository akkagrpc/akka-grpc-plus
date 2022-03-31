#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.Behaviors;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.Entity;
import akka.cluster.sharding.typed.javadsl.EntityTypeKey;
import akka.persistence.typed.PersistenceId;
import akka.persistence.typed.javadsl.*;
import ${package}.server.command.Command;
import ${package}.server.command.DisableMovie;
import ${package}.server.command.GetMovie;
import ${package}.server.command.RegisterMovie;
import ${package}.server.event.Event;
import ${package}.server.event.MovieDisabled;
import ${package}.server.event.MovieRegistered;
import ${package}.server.reply.Accepted;
import ${package}.server.reply.Rejected;
import ${package}.server.reply.Summary;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ${capitalize_artifactId}Aggregate extends EventSourcedBehaviorWithEnforcedReplies<Command, Event, ${capitalize_artifactId}> {

    public final String movieId;
    public final String projectionTag;
    public static EntityTypeKey<Command> ENTITY_KEY = EntityTypeKey.create(Command.class, "MovieAggregate");

    private ${capitalize_artifactId}Aggregate(String movieId, String projectionTag) {
        super(PersistenceId.of(ENTITY_KEY.name(), movieId),
                SupervisorStrategy.restartWithBackoff(Duration.ofMillis(200), Duration.ofSeconds(5), 0.1));
        this.movieId = movieId;
        this.projectionTag = projectionTag;
    }

    public static Behavior<Command> create(String movieId, String projectionTag) {
        return Behaviors.setup(
                ctx -> EventSourcedBehavior.start(new ${capitalize_artifactId}Aggregate(movieId, projectionTag), ctx));
    }

    public static final List<String> TAGS =
            Collections.unmodifiableList(
                    Arrays.asList("${artifactId}-0", "${artifactId}-1", "${artifactId}-2", "${artifactId}-3", "${artifactId}-4"));

    public static void init(ActorSystem<?> system) {
        ClusterSharding.get(system)
                .init(
                        Entity.of(
                                ENTITY_KEY,
                                entityContext -> {
                                    int i = Math.abs(entityContext.getEntityId().hashCode() % TAGS.size());
                                    String selectedTag = TAGS.get(i);
                                    return ${capitalize_artifactId}Aggregate.create(entityContext.getEntityId(), selectedTag);
                                }));
    }

    @Override
    public ${capitalize_artifactId} emptyState() {
        return ${capitalize_artifactId}.EMPTY;
    }

    @Override
    public RetentionCriteria retentionCriteria() {
        return RetentionCriteria.snapshotEvery(100, 2);
    }

    @Override
    public Set<String> tagsFor(Event event) {
        return Collections.singleton(projectionTag);
    }

    @Override
    public EventHandler<${capitalize_artifactId}, Event> eventHandler() {
        return newEventHandlerBuilder()
                .forAnyState()
                .onEvent(MovieRegistered.class, (state, evt) -> new ${capitalize_artifactId}(evt.getMovieId(),
                        evt.getTitle(), evt.getDescription(), evt.getRating(), evt.getGenre(),
                        evt.getCreatedBy(), null, evt.getCreatedDateTime(), null, "NEW"))
                .onEvent(MovieDisabled.class, (state, evt) -> new ${capitalize_artifactId}(evt.getMovieId(),
                        state.getTitle(), state.getDescription(), state.getRating(),
                        state.getGenre(), state.getCreatedBy(), state.getLastModifiedBy(),
                        state.getCreationDateTime(), state.getLastModifiedDateTime(), "DISABLED"))
                .build();
    }

    @Override
    public CommandHandlerWithReply<Command, Event, ${capitalize_artifactId}> commandHandler() {
        CommandHandlerWithReplyBuilder<Command, Event, ${capitalize_artifactId}> builder = newCommandHandlerWithReplyBuilder();

        builder.forState(${capitalize_artifactId}::isEmpty)
                .onCommand(RegisterMovie.class, this::onRegisterMovie);

        builder.forState(${capitalize_artifactId}::isNew)
                .onCommand(DisableMovie.class, this::onDisableMovie);

        builder.forState(${capitalize_artifactId}::isDisabled)
                .onCommand(DisableMovie.class, cmd -> Effect().reply(cmd.replyTo, new Rejected("Cannot disable already disabled template")));

        builder.forAnyState().onCommand(GetMovie.class, this::onGet);

        return builder.build();
    }

    private ReplyEffect<Event, ${capitalize_artifactId}> onRegisterMovie(${capitalize_artifactId} secureTemplate, RegisterMovie cmd) {
        return Effect()
                .persist(new MovieRegistered(movieId, cmd.getMovieDetails().getTitle(),
                        cmd.getMovieDetails().getDescription(),
                        cmd.getMovieDetails().getRating(),
                        cmd.getMovieDetails().getGenre(),
                        cmd.getCreatedBy(), Instant.now().toString()))
                .thenReply(cmd.replyTo, s -> new Accepted(toSummary(s)));
    }

    private ReplyEffect<Event, ${capitalize_artifactId}> onDisableMovie(${capitalize_artifactId} secureTemplate, DisableMovie cmd) {
        return Effect()
                .persist(new MovieDisabled(movieId,
                        cmd.getDisabledBy(), Instant.now().toString()))
                .thenReply(cmd.replyTo, s -> new Accepted(toSummary(s)));

    }

    private ReplyEffect<Event, ${capitalize_artifactId}> onGet(${capitalize_artifactId} movie, GetMovie cmd) {
        return Effect().reply(cmd.replyTo, toSummary(movie));
    }

    private Summary toSummary(${capitalize_artifactId} movie) {
        return new Summary(movie.getMovieId(), movie.getTitle(), movie.getDescription(), movie.getRating(), movie.getGenre(), movie.getCreatedBy(), movie.getLastModifiedBy(), movie.getCreationDateTime(), movie.getLastModifiedDateTime(), movie.getSmStatus());
    }
}
