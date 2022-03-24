#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.reply;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

@Value
@JsonDeserialize
public class Summary implements Reply {
    String ${package}Id;
    String title;
    String description;
    Float rating;
    String genre;
    String createdBy;
    String lastModifiedBy;
    String creationDateTime;
    String lastModifiedDateTime;
    String smStatus;

    @JsonCreator
    public Summary(String ${package}Id, String title, String description, Float rating, String genre, String createdBy, String lastModifiedBy, String creationDateTime, String lastModifiedDateTime, String smStatus) {
        this.${package}Id = ${package}Id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.genre = genre;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
        this.creationDateTime = creationDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
        this.smStatus = smStatus;
    }
}


