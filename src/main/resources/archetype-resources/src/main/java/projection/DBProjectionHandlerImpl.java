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
import ${package}.server.${aggregate_name_with_proper_case}Report;
import ${package}.server.event.Event;
import ${package}.server.event.${aggregate_name_with_proper_case}Registered;
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
        if (event instanceof ${aggregate_name_with_proper_case}Registered) {
            ${aggregate_name_with_proper_case}Registered registered = (${aggregate_name_with_proper_case}Registered) event;
            logger.info("${aggregate_name_with_proper_case} with ID {} was created at {}", registered.${aggregate_name_with_lower_case}Id, registered.createdDateTime);
            Statement stmt =
                    session.createStatement("INSERT into ${aggregate_name_with_lower_case} (${aggregate_name_with_lower_case}id, title, releaseyear, rating, genre, createdby, creationdatetime, smstatus) " +
                                    "VALUES ($1, $2, $3, $4, $5, $6, $7, $8)")
                            .bind(0, registered.${aggregate_name_with_lower_case}Id)
                            .bind(1, registered.title)
                            .bind(2, registered.releaseYear)
                            .bind(3, registered.rating)
                            .bind(4, registered.genre)
                            .bind(5, registered.createdBy)
                            .bind(6, registered.createdDateTime)
                            .bind(7, "NEW");
            //persistToElasticSearch(convertEventDetailsToSecureTemplateReport(templateRegistered));
            return session.updateOne(stmt).thenApply(rowsUpdated -> Done.getInstance());
        } else {
            logger.debug("${aggregate_name_with_proper_case} changed by {}", event);
            return CompletableFuture.completedFuture(Done.getInstance());
        }
    }

    @Override
    public ${aggregate_name_with_proper_case}Report convertEventDetailsTo${aggregate_name_with_proper_case}Report(${aggregate_name_with_proper_case}Registered event) {
        ${aggregate_name_with_proper_case}Report report = new ${aggregate_name_with_proper_case}Report();
        report.set${aggregate_name_with_proper_case}Id(event.get${aggregate_name_with_proper_case}Id());
        report.setTitle(event.getTitle());
        report.setReleaseYear(event.getReleaseYear());
        report.setRating(event.getRating());
        report.setGenre(event.getGenre());
        report.setCreatedBy(event.getCreatedBy());
        report.setCreationDateTime(event.getCreatedDateTime());
        report.setSmStatus("NEW");
        return report;
    }

    private void persistToElasticSearch(${aggregate_name_with_proper_case}Report report) throws IOException {
        IndexRequest indexRequest = new IndexRequest("ps_${aggregate_name_with_lower_case}s");
        indexRequest.id(report.get${aggregate_name_with_proper_case}Id());
        ESRecord esSecTempRecord = elasticSearchRestClient.convertToESRecord(report);
        indexRequest.source(new ObjectMapper().writeValueAsString(esSecTempRecord), XContentType.JSON);
        elasticSearchRestClient.getElasticSearchRestClient(false).index(indexRequest, RequestOptions.DEFAULT);
    }

}

