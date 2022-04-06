#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.Value;
import com.akkagrpc.grpc.Genre;

@Value
@JsonDeserialize
public class ${aggregate_name_with_proper_case}Registered implements Event {
    public String ${aggregate_name_with_lower_case}Id;
    public String title;
    public int releaseYear;
    public Float rating;
    public Genre genre;
    public String createdBy;
    public String createdDateTime;

    @JsonCreator
    public ${aggregate_name_with_proper_case}Registered(String ${aggregate_name_with_lower_case}Id, String title, int releaseYear, Float rating, Genre genre, String createdBy, String createdDateTime) {
        this.${aggregate_name_with_lower_case}Id = ${aggregate_name_with_lower_case}Id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.genre = genre;
        this.createdBy = createdBy;
        this.createdDateTime = createdDateTime;
    }
}
