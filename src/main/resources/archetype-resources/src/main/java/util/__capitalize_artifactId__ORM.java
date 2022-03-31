#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;


import ${package}.server.${capitalize_artifactId};

public final class ${capitalize_artifactId}ORM {
    private ${capitalize_artifactId}ORM(){}

    public static ${capitalize_artifactId} mapRowTo${capitalize_artifactId}(io.r2dbc.spi.Row row){
        return ${capitalize_artifactId}.builder()
                .movieId(row.get("movieid", String.class))
                .title(row.get("title", String.class))
                .description(row.get("description", String.class))
                .rating(row.get("rating", Float.class))
                .genre(row.get("genre", String.class))
                .createdBy(row.get("createdby", String.class))
                .creationDateTime(row.get("creationdatetime", String.class))
                .lastModifiedBy(row.get("lastmodifiedby", String.class))
                .lastModifiedDateTime(row.get("disableddatetime", String.class))
                .smStatus(row.get("smstatus", String.class))
                .build();
    }
}
