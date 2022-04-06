#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.projection;

import akka.Done;
import akka.persistence.query.typed.EventEnvelope;
import akka.projection.javadsl.HandlerLifecycle;
import akka.projection.r2dbc.javadsl.R2dbcSession;
import ${package}.server.${aggregate_name_with_proper_case}Report;
import ${package}.server.event.Event;
import ${package}.server.event.${aggregate_name_with_proper_case}Registered;

import java.util.concurrent.CompletionStage;

public interface DBProjectionHandler  extends HandlerLifecycle  {

    CompletionStage<Done> process(R2dbcSession session, EventEnvelope<Event> envelope);

    ${aggregate_name_with_proper_case}Report convertEventDetailsTo${aggregate_name_with_proper_case}Report(${aggregate_name_with_proper_case}Registered event);
}
