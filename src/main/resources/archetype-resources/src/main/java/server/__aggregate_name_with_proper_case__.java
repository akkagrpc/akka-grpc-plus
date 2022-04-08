#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;
import com.akkagrpc.${aggregate_name_with_lower_case}.Genre;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize
@Builder
public class ${aggregate_name_with_proper_case} {
    String ${aggregate_name_with_lower_case}Id;
    String title;
    int releaseYear;
    Float rating;
    Genre genre;
    String createdBy;
    String lastModifiedBy;
    String creationDateTime;
    String lastModifiedDateTime;
    String smStatus;

    @JsonCreator
    public ${aggregate_name_with_proper_case}(String ${aggregate_name_with_lower_case}Id, String title, int releaseYear, Float rating, Genre genre, String createdBy, String lastModifiedBy, String creationDateTime, String lastModifiedDateTime, String smStatus) {
        this.${aggregate_name_with_lower_case}Id = ${aggregate_name_with_lower_case}Id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.genre = genre;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
        this.creationDateTime = creationDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
        this.smStatus = smStatus;
    }

    public static ${aggregate_name_with_proper_case} EMPTY = new ${aggregate_name_with_proper_case}(null, null, 0, null, null, null, null, null, null, "EMPTY");

    public boolean isEmpty() {
        return "EMPTY".equals(this.smStatus);
    }

    public boolean isNew() {
        return "NEW".equals(this.smStatus);
    }

    public boolean isChanged() {
        return "CHANGED".equals(this.smStatus);
    }

    public boolean isDisabled() {
        return "DISABLED".equals(this.smStatus);
    }
}
