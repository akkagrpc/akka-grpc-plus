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
import ${package}.server.command.Disable${aggregate_name_with_proper_case};
import ${package}.server.command.Get${aggregate_name_with_proper_case};
import ${package}.server.command.Register${aggregate_name_with_proper_case};
import ${package}.server.event.Event;
import ${package}.server.event.${aggregate_name_with_proper_case}Disabled;
import ${package}.server.event.${aggregate_name_with_proper_case}Registered;
import ${package}.server.reply.Accepted;
import ${package}.server.reply.Rejected;
import ${package}.server.reply.Summary;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ${aggregate_name_with_proper_case}Aggregate extends EventSourcedBehaviorWithEnforcedReplies<Command, Event, ${aggregate_name_with_proper_case}> {

    public final String ${aggregate_name_with_lower_case}Id;
    public final String projectionTag;
    public static EntityTypeKey<Command> ENTITY_KEY = EntityTypeKey.create(Command.class, "${aggregate_name_with_proper_case}Aggregate");

    private ${aggregate_name_with_proper_case}Aggregate(String ${aggregate_name_with_lower_case}Id, String projectionTag) {
        super(PersistenceId.of(ENTITY_KEY.name(), ${aggregate_name_with_lower_case}Id),
                SupervisorStrategy.restartWithBackoff(Duration.ofMillis(200), Duration.ofSeconds(5), 0.1));
        this.${aggregate_name_with_lower_case}Id = ${aggregate_name_with_lower_case}Id;
        this.projectionTag = projectionTag;
    }

    public static Behavior<Command> create(String ${aggregate_name_with_lower_case}Id, String projectionTag) {
        return Behaviors.setup(
                ctx -> EventSourcedBehavior.start(new ${aggregate_name_with_proper_case}Aggregate(${aggregate_name_with_lower_case}Id, projectionTag), ctx));
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
                                    return ${aggregate_name_with_proper_case}Aggregate.create(entityContext.getEntityId(), selectedTag);
                                }));
    }

    @Override
    public ${aggregate_name_with_proper_case} emptyState() {
        return ${aggregate_name_with_proper_case}.EMPTY;
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
    public EventHandler<${aggregate_name_with_proper_case}, Event> eventHandler() {
        return newEventHandlerBuilder()
                .forAnyState()
                .onEvent(${aggregate_name_with_proper_case}Registered.class, (state, evt) -> new ${aggregate_name_with_proper_case}(evt.get${aggregate_name_with_proper_case}Id(),
                        evt.getTitle(), evt.getReleaseYear(), evt.getRating(), evt.getGenre(),
                        evt.getCreatedBy(), null, evt.getCreatedDateTime(), null, "NEW"))
                .onEvent(${aggregate_name_with_proper_case}Disabled.class, (state, evt) -> new ${aggregate_name_with_proper_case}(evt.get${aggregate_name_with_proper_case}Id(),
                        state.getTitle(), state.getReleaseYear(), state.getRating(),
                        state.getGenre(), state.getCreatedBy(), state.getLastModifiedBy(),
                        state.getCreationDateTime(), state.getLastModifiedDateTime(), "DISABLED"))
                .build();
    }

    @Override
    public CommandHandlerWithReply<Command, Event, ${aggregate_name_with_proper_case}> commandHandler() {
        CommandHandlerWithReplyBuilder<Command, Event, ${aggregate_name_with_proper_case}> builder = newCommandHandlerWithReplyBuilder();

        builder.forState(${aggregate_name_with_proper_case}::isEmpty)
                .onCommand(Register${aggregate_name_with_proper_case}.class, this::onRegister${aggregate_name_with_proper_case});

        builder.forState(${aggregate_name_with_proper_case}::isNew)
                .onCommand(Disable${aggregate_name_with_proper_case}.class, this::onDisable${aggregate_name_with_proper_case});

        builder.forState(${aggregate_name_with_proper_case}::isDisabled)
                .onCommand(Disable${aggregate_name_with_proper_case}.class, cmd -> Effect().reply(cmd.replyTo, new Rejected("Cannot disable already disabled")));

        builder.forAnyState().onCommand(Get${aggregate_name_with_proper_case}.class, this::onGet);

        return builder.build();
    }

    private ReplyEffect<Event, ${aggregate_name_with_proper_case}> onRegister${aggregate_name_with_proper_case}(${aggregate_name_with_proper_case} ${aggregate_name_with_lower_case}, Register${aggregate_name_with_proper_case} cmd) {
        return Effect()
                .persist(new ${aggregate_name_with_proper_case}Registered(${aggregate_name_with_lower_case}Id, cmd.get${aggregate_name_with_proper_case}Details().getTitle(),
                        cmd.get${aggregate_name_with_proper_case}Details().getReleaseYear(),
                        cmd.get${aggregate_name_with_proper_case}Details().getRating(),
                        cmd.get${aggregate_name_with_proper_case}Details().getGenre(),
                        cmd.getCreatedBy(), Instant.now().toString()))
                .thenReply(cmd.replyTo, s -> new Accepted(toSummary(s)));
    }

    private ReplyEffect<Event, ${aggregate_name_with_proper_case}> onDisable${aggregate_name_with_proper_case}(${aggregate_name_with_proper_case} ${aggregate_name_with_lower_case}, Disable${aggregate_name_with_proper_case} cmd) {
        return Effect()
                .persist(new ${aggregate_name_with_proper_case}Disabled(${aggregate_name_with_lower_case}Id,
                        cmd.getDisabledBy(), Instant.now().toString()))
                .thenReply(cmd.replyTo, s -> new Accepted(toSummary(s)));

    }

    private ReplyEffect<Event, ${aggregate_name_with_proper_case}> onGet(${aggregate_name_with_proper_case} ${aggregate_name_with_lower_case}, Get${aggregate_name_with_proper_case} cmd) {
        return Effect().reply(cmd.replyTo, toSummary(${aggregate_name_with_lower_case}));
    }

    private Summary toSummary(${aggregate_name_with_proper_case} ${aggregate_name_with_lower_case}) {
        return new Summary(${aggregate_name_with_lower_case}.get${aggregate_name_with_proper_case}Id(), ${aggregate_name_with_lower_case}.getTitle(), ${aggregate_name_with_lower_case}.getReleaseYear(), ${aggregate_name_with_lower_case}.getRating(), ${aggregate_name_with_lower_case}.getGenre(), ${aggregate_name_with_lower_case}.getCreatedBy(), ${aggregate_name_with_lower_case}.getLastModifiedBy(), ${aggregate_name_with_lower_case}.getCreationDateTime(), ${aggregate_name_with_lower_case}.getLastModifiedDateTime(), ${aggregate_name_with_lower_case}.getSmStatus());
    }
}
