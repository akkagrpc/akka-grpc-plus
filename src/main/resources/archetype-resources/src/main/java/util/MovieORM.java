#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;


import ${package}.server.Movie;

public final class MovieORM {
    private MovieORM(){}

    public static Movie mapRowToMovie(io.r2dbc.spi.Row row){
        return Movie.builder()
                .${package}Id(row.get("${package}id", String.class))
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
