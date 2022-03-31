#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.query;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import ${package}.server.${capitalize_artifactId};
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ${capitalize_artifactId}DAO {
    Mono<${capitalize_artifactId}> getMovieById(String movieId);

    Source<${capitalize_artifactId}, NotUsed> getMovieByTemplateId(String movieId);

    Flux<${capitalize_artifactId}> getMovies();
}
