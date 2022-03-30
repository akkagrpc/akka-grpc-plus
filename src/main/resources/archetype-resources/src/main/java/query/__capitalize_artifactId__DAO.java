#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.query;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import ${package}.server.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ${capitalize_artifactId}DAO {
    Mono<Movie> getMovieById(String movieId);

    Source<Movie, NotUsed> getMovieByTemplateId(String movieId);

    Flux<Movie> getMovies();
}
