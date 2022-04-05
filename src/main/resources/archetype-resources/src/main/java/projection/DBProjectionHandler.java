#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.projection;

import akka.Done;
import akka.persistence.query.typed.EventEnvelope;
import akka.projection.javadsl.HandlerLifecycle;
import akka.projection.r2dbc.javadsl.R2dbcSession;
import ${package}.server.${first_word_of_artifactId}Report;
import ${package}.server.event.Event;
import ${package}.server.event.${first_word_of_artifactId}Registered;

import java.util.concurrent.CompletionStage;

public interface DBProjectionHandler  extends HandlerLifecycle  {

    CompletionStage<Done> process(R2dbcSession session, EventEnvelope<Event> envelope);

    ${first_word_of_artifactId}Report convertEventDetailsTo${first_word_of_artifactId}Report(${first_word_of_artifactId}Registered event);
}
