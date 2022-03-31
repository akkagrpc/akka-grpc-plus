#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;

import lombok.Data;

@Data
public class ${capitalize_artifactId}Report {
    private String movieId;
    private String title;
    private String description;
    private Float rating;
    private String genre;
    private String creationDateTime;
    private String createdBy;
    private String lastModifiedDateTime;
    private String lastModifiedBy;
    private String smStatus;
}
