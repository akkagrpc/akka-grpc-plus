#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;
import com.akkagrpc.${aggregate_name_with_lower_case}.Genre;

import lombok.Data;

@Data
public class ${aggregate_name_with_proper_case}Report {
    private String ${aggregate_name_with_lower_case}Id;
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
