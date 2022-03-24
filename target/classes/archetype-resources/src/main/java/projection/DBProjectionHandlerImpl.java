#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.projection;

import akka.Done;
import akka.persistence.query.typed.EventEnvelope;
import akka.projection.r2dbc.javadsl.R2dbcHandler;
import akka.projection.r2dbc.javadsl.R2dbcSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.Statement;
import lombok.SneakyThrows;
import ${package}.server.ESRecord;
import ${package}.server.MovieReport;
import ${package}.server.event.Event;
import ${package}.server.event.MovieRegistered;
import ${package}.util.ElasticSearchRestClient;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class DBProjectionHandlerImpl extends R2dbcHandler<EventEnvelope<Event>> implements DBProjectionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    ElasticSearchRestClient elasticSearchRestClient;

    @Inject
    public DBProjectionHandlerImpl(ElasticSearchRestClient elasticSearchRestClient) {
        this.elasticSearchRestClient = elasticSearchRestClient;
    }

    @SneakyThrows
    @Override
    public CompletionStage<Done> process(R2dbcSession session, EventEnvelope<Event> envelope) {
        Event event = envelope.event();
        if (event instanceof MovieRegistered) {
            MovieRegistered registered = (MovieRegistered) event;
            logger.info("Movie with ID {} was created at {}", registered.${package}Id, registered.createdDateTime);
            Statement stmt =
                    session.createStatement("INSERT into ${package} (${package}id, title, description, rating, genre, createdby, creationdatetime, smstatus) " +
                                    "VALUES (${symbol_dollar}1, ${symbol_dollar}2, ${symbol_dollar}3, ${symbol_dollar}4, ${symbol_dollar}5, ${symbol_dollar}6, ${symbol_dollar}7, ${symbol_dollar}8)")
                            .bind(0, registered.${package}Id)
                            .bind(1, registered.title)
                            .bind(2, registered.description)
                            .bind(3, registered.rating)
                            .bind(4, registered.genre)
                            .bind(5, registered.createdBy)
                            .bind(6, registered.createdDateTime)
                            .bind(7, "NEW");
            //persistToElasticSearch(convertEventDetailsToSecureTemplateReport(templateRegistered));
            return session.updateOne(stmt).thenApply(rowsUpdated -> Done.getInstance());
        } else {
            logger.debug("Movie changed by {}", event);
            return CompletableFuture.completedFuture(Done.getInstance());
        }
    }

    @Override
    public MovieReport convertEventDetailsToMovieReport(MovieRegistered event) {
        MovieReport report = new MovieReport();
        report.setMovieId(event.getMovieId());
        report.setTitle(event.getTitle());
        report.setDescription(event.getDescription());
        report.setRating(event.getRating());
        report.setGenre(event.getGenre());
        report.setCreatedBy(event.getCreatedBy());
        report.setCreationDateTime(event.getCreatedDateTime());
        report.setSmStatus("NEW");
        return report;
    }

    private void persistToElasticSearch(MovieReport report) throws IOException {
        IndexRequest indexRequest = new IndexRequest("ps_${package}s");
        indexRequest.id(report.getMovieId());
        ESRecord esSecTempRecord = elasticSearchRestClient.convertToESRecord(report);
        indexRequest.source(new ObjectMapper().writeValueAsString(esSecTempRecord), XContentType.JSON);
        elasticSearchRestClient.getElasticSearchRestClient(false).index(indexRequest, RequestOptions.DEFAULT);
    }

}
