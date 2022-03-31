#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.projection;

import akka.Done;
import akka.persistence.query.typed.EventEnvelope;
import akka.projection.javadsl.HandlerLifecycle;
import akka.projection.r2dbc.javadsl.R2dbcSession;
import ${package}.server.${capitalize_artifactId}Report;
import ${package}.server.event.Event;
import ${package}.server.event.MovieRegistered;

import java.util.concurrent.CompletionStage;

public interface DBProjectionHandler  extends HandlerLifecycle  {

    CompletionStage<Done> process(R2dbcSession session, EventEnvelope<Event> envelope);

    ${capitalize_artifactId}Report convertEventDetailsTo${capitalize_artifactId}Report(MovieRegistered event);
}
