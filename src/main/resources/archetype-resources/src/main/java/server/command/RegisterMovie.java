#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.command;

import akka.actor.typed.ActorRef;
import ${groupId}.grpc.RegisterMovieRequest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;
import ${package}.server.reply.Confirmation;

@Value
@JsonDeserialize
public class RegisterMovie implements Command<Confirmation> {
    private RegisterMovieRequest ${package}Details;
    public String createdBy;
    public ActorRef<Confirmation> replyTo;

    @JsonCreator
    public RegisterMovie(RegisterMovieRequest ${package}Details, String createdBy, ActorRef<Confirmation> replyTo) {
        this.${package}Details = ${package}Details;
        this.createdBy = createdBy;
        this.replyTo = replyTo;
    }
}
