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

public class MovieAggregate extends EventSourcedBehaviorWithEnforcedReplies<Command, Event, Movie> {

    public final String ${package}Id;
    public final String projectionTag;
    public static EntityTypeKey<Command> ENTITY_KEY = EntityTypeKey.create(Command.class, "MovieAggregate");

    private MovieAggregate(String ${package}Id, String projectionTag) {
        super(PersistenceId.of(ENTITY_KEY.name(), ${package}Id),
                SupervisorStrategy.restartWithBackoff(Duration.ofMillis(200), Duration.ofSeconds(5), 0.1));
        this.${package}Id = ${package}Id;
        this.projectionTag = projectionTag;
    }

    public static Behavior<Command> create(String ${package}Id, String projectionTag) {
        return Behaviors.setup(
                ctx -> EventSourcedBehavior.start(new MovieAggregate(${package}Id, projectionTag), ctx));
    }

    public static final List<String> TAGS =
            Collections.unmodifiableList(
                    Arrays.asList("${package}-0", "${package}-1", "${package}-2", "${package}-3", "${package}-4"));

    public static void init(ActorSystem<?> system) {
        ClusterSharding.get(system)
                .init(
                        Entity.of(
                                ENTITY_KEY,
                                entityContext -> {
                                    int i = Math.abs(entityContext.getEntityId().hashCode() % TAGS.size());
                                    String selectedTag = TAGS.get(i);
                                    return MovieAggregate.create(entityContext.getEntityId(), selectedTag);
                                }));
    }

    @Override
    public Movie emptyState() {
        return Movie.EMPTY;
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
    public EventHandler<Movie, Event> eventHandler() {
        return newEventHandlerBuilder()
                .forAnyState()
                .onEvent(MovieRegistered.class, (${package}, evt) -> new Movie(evt.getMovieId(),
                        evt.getTitle(), evt.getDescription(), evt.getRating(), evt.getGenre(),
                        evt.getCreatedBy(), null, evt.getCreatedDateTime(), null, "NEW"))
                .onEvent(MovieDisabled.class, (${package}, evt) -> new Movie(evt.getMovieId(),
                        ${package}.getTitle(), ${package}.getDescription(), ${package}.getRating(),
                        ${package}.getGenre(), ${package}.getCreatedBy(), ${package}.getLastModifiedBy(),
                        ${package}.getCreationDateTime(), ${package}.getLastModifiedDateTime(), "DISABLED"))
                .build();
    }

    @Override
    public CommandHandlerWithReply<Command, Event, Movie> commandHandler() {
        CommandHandlerWithReplyBuilder<Command, Event, Movie> builder = newCommandHandlerWithReplyBuilder();

        builder.forState(Movie::isEmpty)
                .onCommand(RegisterMovie.class, this::onRegisterMovie);

        builder.forState(Movie::isNew)
                .onCommand(DisableMovie.class, this::onDisableMovie);

        builder.forState(Movie::isDisabled)
                .onCommand(DisableMovie.class, cmd -> Effect().reply(cmd.replyTo, new Rejected("Cannot disable already disabled template")));

        builder.forAnyState().onCommand(GetMovie.class, this::onGet);

        return builder.build();
    }

    private ReplyEffect<Event, Movie> onRegisterMovie(Movie secureTemplate, RegisterMovie cmd) {
        return Effect()
                .persist(new MovieRegistered(${package}Id, cmd.getMovieDetails().getTitle(),
                        cmd.getMovieDetails().getDescription(),
                        cmd.getMovieDetails().getRating(),
                        cmd.getMovieDetails().getGenre(),
                        cmd.getCreatedBy(), Instant.now().toString()))
                .thenReply(cmd.replyTo, s -> new Accepted(toSummary(s)));
    }

    private ReplyEffect<Event, Movie> onDisableMovie(Movie secureTemplate, DisableMovie cmd) {
        return Effect()
                .persist(new MovieDisabled(${package}Id,
                        cmd.getDisabledBy(), Instant.now().toString()))
                .thenReply(cmd.replyTo, s -> new Accepted(toSummary(s)));

    }

    private ReplyEffect<Event, Movie> onGet(Movie ${package}, GetMovie cmd) {
        return Effect().reply(cmd.replyTo, toSummary(${package}));
    }

    private Summary toSummary(Movie ${package}) {
        return new Summary(${package}.getMovieId(), ${package}.getTitle(), ${package}.getDescription(), ${package}.getRating(), ${package}.getGenre(), ${package}.getCreatedBy(), ${package}.getLastModifiedBy(), ${package}.getCreationDateTime(), ${package}.getLastModifiedDateTime(), ${package}.getSmStatus());
    }
}
