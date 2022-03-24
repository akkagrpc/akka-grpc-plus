#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.reply;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

@Value
@JsonDeserialize
public class Accepted implements Confirmation {
    public Summary summary;

    @JsonCreator
    public Accepted(Summary summary) {
        this.summary = summary;
    }
}
