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
import ${package}.server.command.Disable${first_word_of_artifactId};
import ${package}.server.command.Get${first_word_of_artifactId};
import ${package}.server.command.Register${first_word_of_artifactId};
import ${package}.server.event.Event;
import ${package}.server.event.${first_word_of_artifactId}Disabled;
import ${package}.server.event.${first_word_of_artifactId}Registered;
import ${package}.server.reply.Accepted;
import ${package}.server.reply.Rejected;
import ${package}.server.reply.Summary;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ${first_word_of_artifactId}Aggregate extends EventSourcedBehaviorWithEnforcedReplies<Command, Event, ${first_word_of_artifactId}> {

    public final String ${package}Id;
    public final String projectionTag;
    public static EntityTypeKey<Command> ENTITY_KEY = EntityTypeKey.create(Command.class, "${first_word_of_artifactId}Aggregate");

    private ${first_word_of_artifactId}Aggregate(String ${package}Id, String projectionTag) {
        super(PersistenceId.of(ENTITY_KEY.name(), ${package}Id),
                SupervisorStrategy.restartWithBackoff(Duration.ofMillis(200), Duration.ofSeconds(5), 0.1));
        this.${package}Id = ${package}Id;
        this.projectionTag = projectionTag;
    }

    public static Behavior<Command> create(String ${package}Id, String projectionTag) {
        return Behaviors.setup(
                ctx -> EventSourcedBehavior.start(new ${first_word_of_artifactId}Aggregate(${package}Id, projectionTag), ctx));
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
                                    return ${first_word_of_artifactId}Aggregate.create(entityContext.getEntityId(), selectedTag);
                                }));
    }

    @Override
    public ${first_word_of_artifactId} emptyState() {
        return ${first_word_of_artifactId}.EMPTY;
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
    public EventHandler<${first_word_of_artifactId}, Event> eventHandler() {
        return newEventHandlerBuilder()
                .forAnyState()
                .onEvent(${first_word_of_artifactId}Registered.class, (state, evt) -> new ${first_word_of_artifactId}(evt.get${first_word_of_artifactId}Id(),
                        evt.getTitle(), evt.getReleaseYear(), evt.getRating(), evt.getGenre(),
                        evt.getCreatedBy(), null, evt.getCreatedDateTime(), null, "NEW"))
                .onEvent(${first_word_of_artifactId}Disabled.class, (state, evt) -> new ${first_word_of_artifactId}(evt.get${first_word_of_artifactId}Id(),
                        state.getTitle(), state.getReleaseYear(), state.getRating(),
                        state.getGenre(), state.getCreatedBy(), state.getLastModifiedBy(),
                        state.getCreationDateTime(), state.getLastModifiedDateTime(), "DISABLED"))
                .build();
    }

    @Override
    public CommandHandlerWithReply<Command, Event, ${first_word_of_artifactId}> commandHandler() {
        CommandHandlerWithReplyBuilder<Command, Event, ${first_word_of_artifactId}> builder = newCommandHandlerWithReplyBuilder();

        builder.forState(${first_word_of_artifactId}::isEmpty)
                .onCommand(Register${first_word_of_artifactId}.class, this::onRegister${first_word_of_artifactId});

        builder.forState(${first_word_of_artifactId}::isNew)
                .onCommand(Disable${first_word_of_artifactId}.class, this::onDisable${first_word_of_artifactId});

        builder.forState(${first_word_of_artifactId}::isDisabled)
                .onCommand(Disable${first_word_of_artifactId}.class, cmd -> Effect().reply(cmd.replyTo, new Rejected("Cannot disable already disabled template")));

        builder.forAnyState().onCommand(Get${first_word_of_artifactId}.class, this::onGet);

        return builder.build();
    }

    private ReplyEffect<Event, ${first_word_of_artifactId}> onRegister${first_word_of_artifactId}(${first_word_of_artifactId} ${package}, Register${first_word_of_artifactId} cmd) {
        return Effect()
                .persist(new ${first_word_of_artifactId}Registered(${package}Id, cmd.get${first_word_of_artifactId}Details().getTitle(),
                        cmd.get${first_word_of_artifactId}Details().getReleaseYear(),
                        cmd.get${first_word_of_artifactId}Details().getRating(),
                        cmd.get${first_word_of_artifactId}Details().getGenre(),
                        cmd.getCreatedBy(), Instant.now().toString()))
                .thenReply(cmd.replyTo, s -> new Accepted(toSummary(s)));
    }

    private ReplyEffect<Event, ${first_word_of_artifactId}> onDisable${first_word_of_artifactId}(${first_word_of_artifactId} ${package}, Disable${first_word_of_artifactId} cmd) {
        return Effect()
                .persist(new ${first_word_of_artifactId}Disabled(${package}Id,
                        cmd.getDisabledBy(), Instant.now().toString()))
                .thenReply(cmd.replyTo, s -> new Accepted(toSummary(s)));

    }

    private ReplyEffect<Event, ${first_word_of_artifactId}> onGet(${first_word_of_artifactId} ${package}, Get${first_word_of_artifactId} cmd) {
        return Effect().reply(cmd.replyTo, toSummary(${package}));
    }

    private Summary toSummary(${first_word_of_artifactId} ${package}) {
        return new Summary(${package}.get${first_word_of_artifactId}Id(), ${package}.getTitle(), ${package}.getReleaseYear(), ${package}.getRating(), ${package}.getGenre(), ${package}.getCreatedBy(), ${package}.getLastModifiedBy(), ${package}.getCreationDateTime(), ${package}.getLastModifiedDateTime(), ${package}.getSmStatus());
    }
}
