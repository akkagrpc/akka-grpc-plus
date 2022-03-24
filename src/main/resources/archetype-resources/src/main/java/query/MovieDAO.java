#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.query;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import ${package}.server.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieDAO {
    Mono<Movie> getMovieById(String ${package}Id);

    Source<Movie, NotUsed> getMovieByTemplateId(String ${package}Id);

    Flux<Movie> getMovies();
}