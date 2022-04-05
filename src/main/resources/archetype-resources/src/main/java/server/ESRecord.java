#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server;
import com.akkagrpc.grpc.Genre;

import lombok.Data;

@Data
public class ESRecord {
    private String ${package}Id;
    private String title;
    private int releaseYear;
    private Float rating;
    private Genre genre;
    private String createdDate;
    private String createdBy;
    private String lastModifiedDate;
    private String lastModifiedBy;
}
