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
public class MovieRegistered implements Event {
    public String movieId;
    public String title;
    public int releaseYear;
    public Float rating;
    public Genre genre;
    public String createdBy;
    public String createdDateTime;

    @JsonCreator
    public MovieRegistered(String movieId, String title, int releaseYear, Float rating, Genre genre, String createdBy, String createdDateTime) {
        this.movieId = movieId;
        this.title = title;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.genre = genre;
        this.createdBy = createdBy;
        this.createdDateTime = createdDateTime;
    }
}
