#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.command;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import ${package}.server.reply.Summary;

@Getter
public class GetMovie implements Command<Summary> {

    public String movieId;
    public ActorRef<? super Summary> replyTo;

    @JsonCreator
    public GetMovie(String movieId, ActorRef<Summary> replyTo) {
        this.movieId = movieId;
        this.replyTo = replyTo;
    }
}
