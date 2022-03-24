#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.Value;

@Value
@JsonDeserialize
public class MovieDisabled implements Event {

    public String ${package}Id;
    public String disabledBy;
    public String disabledDateTime;

    @JsonCreator
    public MovieDisabled(String ${package}Id, String disabledBy, String disabledDateTime) {
        this.${package}Id = Preconditions.checkNotNull(${package}Id, "${package}Id");
        this.disabledBy = Preconditions.checkNotNull(disabledBy, "disabledBy");
        this.disabledDateTime = disabledDateTime;
    }
}
