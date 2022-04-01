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
import ${package}.server.${capitalize_artifactId}Aggregate;
import ${package}.server.event.Event;
import ${package}.server.event.MovieRegistered;

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
                        "SecureTemplateProjection",
                        sliceRanges.size(),
                        i -> ProjectionBehavior.create(createProjection(sliceRanges.get(i), system)),
                        ProjectionBehavior.stopMessage());
    }

    @SneakyThrows
    @Override
    public CompletionStage<Done> process(R2dbcSession session, EventEnvelope<Event> envelope) {
        Event event = envelope.event();
        if (event instanceof MovieRegistered) {
            MovieRegistered templateRegistered = (MovieRegistered) event;
            logger.info("Secure template with ID {} was created at {}", templateRegistered.movieId, templateRegistered.createdDateTime);
            Statement stmt =
                    session.createStatement("INSERT into movie (movieid, title, releaseyear, rating, genre, createdby, creationdatetime, smstatus) " +
                                    "VALUES ($1, $2, $3, $4, $5, $6, $7, $8)")
                            .bind(0, templateRegistered.movieId)
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
            logger.debug("SecureTemplate changed by {}", event);
            return CompletableFuture.completedFuture(Done.getInstance());
        }
    }


    private Projection<EventEnvelope<Event>> createProjection(Pair<Integer, Integer> sliceRange,
                                                              ActorSystem<?> system) {
        int minSlice = sliceRange.first();
        int maxSlice = sliceRange.second();

        String entityType = ${capitalize_artifactId}Aggregate.ENTITY_KEY.name();

        SourceProvider<Offset, EventEnvelope<Event>> sourceProvider =
                EventSourcedProvider.eventsBySlices(system, R2dbcReadJournal.Identifier(), entityType, minSlice, maxSlice);

        ProjectionId projectionId =
                ProjectionId.of("${capitalize_artifactId}", "${artifactId}-" + minSlice + "-" + maxSlice);

        Optional<R2dbcProjectionSettings> settings = Optional.empty();

        return R2dbcProjection.exactlyOnce(
                projectionId, settings, sourceProvider, DBProjectionImpl::new, system);
    }
}

