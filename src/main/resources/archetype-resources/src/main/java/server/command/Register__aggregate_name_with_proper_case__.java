#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.command;

import akka.actor.typed.ActorRef;
import com.akkagrpc.grpc.Register${aggregate_name_with_proper_case}Request;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;
import ${package}.server.reply.Confirmation;

@Value
@JsonDeserialize
public class Register${aggregate_name_with_proper_case} implements Command<Confirmation> {
    private Register${aggregate_name_with_proper_case}Request ${aggregate_name_with_lower_case}Details;
    public String createdBy;
    public ActorRef<Confirmation> replyTo;

    @JsonCreator
    public Register${aggregate_name_with_proper_case}(Register${aggregate_name_with_proper_case}Request ${aggregate_name_with_lower_case}Details, String createdBy, ActorRef<Confirmation> replyTo) {
        this.${aggregate_name_with_lower_case}Details = ${aggregate_name_with_lower_case}Details;
        this.createdBy = createdBy;
        this.replyTo = replyTo;
    }
}
