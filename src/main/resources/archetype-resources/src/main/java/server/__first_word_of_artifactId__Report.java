#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;
import com.akkagrpc.grpc.Genre;

import lombok.Data;

@Data
public class ${first_word_of_artifactId}Report {
    private String ${package}Id;
    private String title;
    private int releaseYear;
    private Float rating;
    private Genre genre;
    private String creationDateTime;
    private String createdBy;
    private String lastModifiedDateTime;
    private String lastModifiedBy;
    private String smStatus;
}
