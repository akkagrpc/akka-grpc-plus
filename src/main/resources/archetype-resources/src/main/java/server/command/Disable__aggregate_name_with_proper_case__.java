#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.command;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.Value;
import ${package}.server.reply.Confirmation;

@Value
@JsonDeserialize
public class Disable${aggregate_name_with_proper_case} implements Command<Confirmation> {
    private String ${aggregate_name_with_lower_case}Id;
    private String disabledBy;
    public ActorRef<Confirmation> replyTo;

    @JsonCreator
    public Disable${aggregate_name_with_proper_case}(String ${aggregate_name_with_lower_case}Id, String disabledBy, ActorRef<Confirmation> replyTo) {
        this.${aggregate_name_with_lower_case}Id = Preconditions.checkNotNull(${aggregate_name_with_lower_case}Id, "Blank ${aggregate_name_with_lower_case}Id");
        this.disabledBy = Preconditions.checkNotNull(disabledBy, "Blank disabledBy");
        this.replyTo = replyTo;
    }
}