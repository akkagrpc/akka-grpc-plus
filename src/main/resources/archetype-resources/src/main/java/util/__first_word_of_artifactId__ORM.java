#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;


import ${package}.server.${first_word_of_artifactId};
import com.akkagrpc.grpc.Genre;

public final class ${first_word_of_artifactId}ORM {
    private ${first_word_of_artifactId}ORM(){}

    public static ${first_word_of_artifactId} mapRowTo${first_word_of_artifactId}(io.r2dbc.spi.Row row){
        return ${first_word_of_artifactId}.builder()
                .${package}Id(row.get("${package}id", String.class))
                .title(row.get("title", String.class))
                .releaseYear(row.get("releaseyear", Integer.class))
                .rating(row.get("rating", Float.class))
                .genre(row.get("genre", Genre.class))
                .createdBy(row.get("createdby", String.class))
                .creationDateTime(row.get("creationdatetime", String.class))
                .lastModifiedBy(row.get("lastmodifiedby", String.class))
                .lastModifiedDateTime(row.get("disableddatetime", String.class))
                .smStatus(row.get("smstatus", String.class))
                .build();
    }
}
