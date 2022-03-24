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
public class MovieRegistered implements Event {
    public String ${package}Id;
    public String title;
    public String description;
    public Float rating;
    public String genre;
    public String createdBy;
    public String createdDateTime;

    @JsonCreator
    public MovieRegistered(String ${package}Id, String title, String description, Float rating, String genre, String createdBy, String createdDateTime) {
        this.${package}Id = ${package}Id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.genre = genre;
        this.createdBy = createdBy;
        this.createdDateTime = createdDateTime;
    }
}
