#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;
import com.akkagrpc.grpc.Genre;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize
@Builder
public class ${capitalize_artifactId} {
    String movieId;
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
    public ${capitalize_artifactId}(String movieId, String title, int releaseYear, Float rating, Genre genre, String createdBy, String lastModifiedBy, String creationDateTime, String lastModifiedDateTime, String smStatus) {
        this.movieId = movieId;
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

    public static ${capitalize_artifactId} EMPTY = new ${capitalize_artifactId}(null, null, 0, null, null, null, null, null, null, "EMPTY");

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
