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
public class DisableMovie implements Command<Confirmation> {
    private String movieId;
    private String disabledBy;
    public ActorRef<Confirmation> replyTo;

    @JsonCreator
    public DisableMovie(String movieId, String disabledBy, ActorRef<Confirmation> replyTo) {
        this.movieId = Preconditions.checkNotNull(movieId, "Blank movieId");
        this.disabledBy = Preconditions.checkNotNull(disabledBy, "Blank disabledBy");
        this.replyTo = replyTo;
    }
}