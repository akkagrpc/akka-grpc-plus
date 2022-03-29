#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;

import lombok.Data;

@Data
public class ESRecord {
    private String movieId;
    private String title;
    private String description;
    private Float rating;
    private String genre;
    private String createdDate;
    private String createdBy;
    private String lastModifiedDate;
    private String lastModifiedBy;
}
