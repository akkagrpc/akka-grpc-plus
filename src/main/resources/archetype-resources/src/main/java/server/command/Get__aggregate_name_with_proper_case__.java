#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.command;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import ${package}.server.reply.Summary;

@Getter
public class Get${aggregate_name_with_proper_case} implements Command<Summary> {

    public String ${aggregate_name_with_lower_case}Id;
    public ActorRef<? super Summary> replyTo;

    @JsonCreator
    public Get${aggregate_name_with_proper_case}(String ${aggregate_name_with_lower_case}Id, ActorRef<Summary> replyTo) {
        this.${aggregate_name_with_lower_case}Id = ${aggregate_name_with_lower_case}Id;
        this.replyTo = replyTo;
    }
}
