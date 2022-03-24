#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.projection;

import akka.actor.CoordinatedShutdown;
import akka.actor.typed.ActorSystem;
import akka.cluster.sharding.typed.javadsl.ShardedDaemonProcess;
import akka.japi.Pair;
import akka.kafka.ProducerSettings;
import akka.kafka.javadsl.SendProducer;
import akka.persistence.query.Offset;
import akka.persistence.query.typed.EventEnvelope;
import akka.persistence.r2dbc.query.javadsl.R2dbcReadJournal;
import akka.projection.Projection;
import akka.projection.ProjectionBehavior;
import akka.projection.ProjectionId;
import akka.projection.eventsourced.javadsl.EventSourcedProvider;
import akka.projection.javadsl.SourceProvider;
import akka.projection.r2dbc.R2dbcProjectionSettings;
import akka.projection.r2dbc.javadsl.R2dbcProjection;
import ${package}.server.MovieAggregate;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import ${package}.server.event.Event;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public final class EventsProjectionImpl implements EventsProjection {
    ActorSystem<?> system;

    @Inject
    public EventsProjectionImpl(ActorSystem<?> system) {
        this.system = system;
    }

    private EventsProjectionImpl() {
    }

    @Override
    public void startProjection() {
        int numberOfSliceRanges = 4;
        List<Pair<Integer, Integer>> sliceRanges =
                EventSourcedProvider.sliceRanges(system, R2dbcReadJournal.Identifier(), numberOfSliceRanges);
        SendProducer<String, byte[]> sendProducer = createProducer(system);
        String topic = system.settings().config().getString("secure-template-service.kafka.topic");

        ShardedDaemonProcess.get(system)
                .init(ProjectionBehavior.Command.class,
                        "PublishEventsProjection",
                        sliceRanges.size(),
                        i -> ProjectionBehavior.create(createProjection(sliceRanges.get(i), system, topic, sendProducer)),
                        ProjectionBehavior.stopMessage());
    }


    static Projection<EventEnvelope<Event>> createProjection(Pair<Integer, Integer> sliceRange, ActorSystem<?> system,
                                                             String topic, SendProducer<String, byte[]> sendProducer) {
        int minSlice = sliceRange.first();
        int maxSlice = sliceRange.second();

        String entityType = MovieAggregate.ENTITY_KEY.name();

        SourceProvider<Offset, EventEnvelope<Event>> sourceProvider =
                EventSourcedProvider.eventsBySlices(system, R2dbcReadJournal.Identifier(), entityType, minSlice, maxSlice);

        ProjectionId projectionId =
                ProjectionId.of("SecureTemplateEvents", "events-" + minSlice + "-" + maxSlice);

        Optional<R2dbcProjectionSettings> settings = Optional.empty();

        return R2dbcProjection.atLeastOnce(projectionId, settings, sourceProvider, () -> new EventsProjectionHandler(topic, sendProducer), system);
    }

    static SendProducer<String, byte[]> createProducer(ActorSystem<?> system) {
        ProducerSettings<String, byte[]> producerSettings =
                ProducerSettings.create(system, new StringSerializer(), new ByteArraySerializer());
        SendProducer<String, byte[]> sendProducer = new SendProducer<>(producerSettings, system);
        CoordinatedShutdown.get(system)
                .addTask(
                        CoordinatedShutdown.PhaseActorSystemTerminate(),
                        "close-sendProducer",
                        () -> sendProducer.close());
        return sendProducer;
    }

}

