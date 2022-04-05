#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.command;

import akka.actor.typed.ActorRef;
import com.akkagrpc.grpc.Register${first_word_of_artifactId}Request;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;
import ${package}.server.reply.Confirmation;

@Value
@JsonDeserialize
public class Register${first_word_of_artifactId} implements Command<Confirmation> {
    private Register${first_word_of_artifactId}Request ${package}Details;
    public String createdBy;
    public ActorRef<Confirmation> replyTo;

    @JsonCreator
    public Register${first_word_of_artifactId}(Register${first_word_of_artifactId}Request ${package}Details, String createdBy, ActorRef<Confirmation> replyTo) {
        this.${package}Details = ${package}Details;
        this.createdBy = createdBy;
        this.replyTo = replyTo;
    }
}
