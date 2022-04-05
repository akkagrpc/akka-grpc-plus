#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.command;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import ${package}.server.reply.Summary;

@Getter
public class Get${first_word_of_artifactId} implements Command<Summary> {

    public String ${package}Id;
    public ActorRef<? super Summary> replyTo;

    @JsonCreator
    public Get${first_word_of_artifactId}(String ${package}Id, ActorRef<Summary> replyTo) {
        this.${package}Id = ${package}Id;
        this.replyTo = replyTo;
    }
}
