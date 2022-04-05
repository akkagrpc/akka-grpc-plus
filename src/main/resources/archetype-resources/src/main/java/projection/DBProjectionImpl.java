#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.projection;

import akka.Done;
import akka.actor.typed.ActorSystem;
import akka.cluster.sharding.typed.javadsl.ShardedDaemonProcess;
import akka.japi.Pair;
import akka.persistence.query.Offset;
import akka.persistence.query.typed.EventEnvelope;
import akka.persistence.r2dbc.query.javadsl.R2dbcReadJournal;
import akka.projection.Projection;
import akka.projection.ProjectionBehavior;
import akka.projection.ProjectionId;
import akka.projection.eventsourced.javadsl.EventSourcedProvider;
import akka.projection.javadsl.SourceProvider;
import akka.projection.r2dbc.R2dbcProjectionSettings;
import akka.projection.r2dbc.javadsl.R2dbcHandler;
import akka.projection.r2dbc.javadsl.R2dbcProjection;
import akka.projection.r2dbc.javadsl.R2dbcSession;
import io.r2dbc.spi.Statement;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ${package}.server.${first_word_of_artifactId}Aggregate;
import ${package}.server.event.Event;
import ${package}.server.event.${first_word_of_artifactId}Registered;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class DBProjectionImpl extends R2dbcHandler<EventEnvelope<Event>> implements DBProjection {
    private final Logger logger = LoggerFactory.getLogger(DBProjectionImpl.class);
    ActorSystem<?> system;

    @Inject
    public DBProjectionImpl(ActorSystem<?> system) {
        this.system = system;
    }

    private DBProjectionImpl() {
    }

    @Override
    public void startProjection() {
        int numberOfSliceRanges = 4;
        List<Pair<Integer, Integer>> sliceRanges =
                EventSourcedProvider.sliceRanges(system, R2dbcReadJournal.Identifier(), numberOfSliceRanges);

        ShardedDaemonProcess.get(system)
                .init(ProjectionBehavior.Command.class,
                        "${first_word_of_artifactId}Projection",
                        sliceRanges.size(),
                        i -> ProjectionBehavior.create(createProjection(sliceRanges.get(i), system)),
                        ProjectionBehavior.stopMessage());
    }

    @SneakyThrows
    @Override
    public CompletionStage<Done> process(R2dbcSession session, EventEnvelope<Event> envelope) {
        Event event = envelope.event();
        if (event instanceof ${first_word_of_artifactId}Registered) {
            ${first_word_of_artifactId}Registered templateRegistered = (${first_word_of_artifactId}Registered) event;
            logger.info("${first_word_of_artifactId} with ID {} was created at {}", templateRegistered.${package}Id, templateRegistered.createdDateTime);
            Statement stmt =
                    session.createStatement("INSERT into ${package} (${package}id, title, releaseyear, rating, genre, createdby, creationdatetime, smstatus) " +
                                    "VALUES ($1, $2, $3, $4, $5, $6, $7, $8)")
                            .bind(0, templateRegistered.${package}Id)
                            .bind(1, templateRegistered.title)
                            .bind(2, templateRegistered.releaseYear)
                            .bind(3, templateRegistered.rating)
                            .bind(4, templateRegistered.genre)
                            .bind(5, templateRegistered.createdBy)
                            .bind(6, templateRegistered.createdDateTime)
                            .bind(7, "NEW");
            //persistToElasticSearch(convertEventDetailsToSecureTemplateReport(templateRegistered));
            return session.updateOne(stmt).thenApply(rowsUpdated -> Done.getInstance());
        } else {
            logger.debug("${first_word_of_artifactId} changed by {}", event);
            return CompletableFuture.completedFuture(Done.getInstance());
        }
    }


    private Projection<EventEnvelope<Event>> createProjection(Pair<Integer, Integer> sliceRange,
                                                              ActorSystem<?> system) {
        int minSlice = sliceRange.first();
        int maxSlice = sliceRange.second();

        String entityType = ${first_word_of_artifactId}Aggregate.ENTITY_KEY.name();

        SourceProvider<Offset, EventEnvelope<Event>> sourceProvider =
                EventSourcedProvider.eventsBySlices(system, R2dbcReadJournal.Identifier(), entityType, minSlice, maxSlice);

        ProjectionId projectionId =
                ProjectionId.of("${first_word_of_artifactId}", "${artifactId}-" + minSlice + "-" + maxSlice);

        Optional<R2dbcProjectionSettings> settings = Optional.empty();

        return R2dbcProjection.exactlyOnce(
                projectionId, settings, sourceProvider, DBProjectionImpl::new, system);
    }
}

