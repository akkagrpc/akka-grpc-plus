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
public class ${aggregate_name_with_proper_case}Disabled implements Event {

    public String ${aggregate_name_with_lower_case}Id;
    public String disabledBy;
    public String disabledDateTime;

    @JsonCreator
    public ${aggregate_name_with_proper_case}Disabled(String ${aggregate_name_with_lower_case}Id, String disabledBy, String disabledDateTime) {
        this.${aggregate_name_with_lower_case}Id = Preconditions.checkNotNull(${aggregate_name_with_lower_case}Id, "${aggregate_name_with_lower_case}Id");
        this.disabledBy = Preconditions.checkNotNull(disabledBy, "disabledBy");
        this.disabledDateTime = disabledDateTime;
    }
}
