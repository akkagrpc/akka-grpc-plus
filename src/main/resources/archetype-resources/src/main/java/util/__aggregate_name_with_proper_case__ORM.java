#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;


import ${package}.server.${aggregate_name_with_proper_case};
import com.akkagrpc.grpc.Genre;

public final class ${aggregate_name_with_proper_case}ORM {
    private ${aggregate_name_with_proper_case}ORM(){}

    public static ${aggregate_name_with_proper_case} mapRowTo${aggregate_name_with_proper_case}(io.r2dbc.spi.Row row){
        return ${aggregate_name_with_proper_case}.builder()
                .${aggregate_name_with_lower_case}Id(row.get("${aggregate_name_with_lower_case}id", String.class))
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
