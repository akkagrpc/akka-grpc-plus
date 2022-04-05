#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.query;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import ${package}.server.${first_word_of_artifactId};
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ${first_word_of_artifactId}DAO {
    Mono<${first_word_of_artifactId}> get${first_word_of_artifactId}ById(String movieId);

    Source<${first_word_of_artifactId}, NotUsed> get${first_word_of_artifactId}ByTemplateId(String ${package}Id);

    Flux<${first_word_of_artifactId}> get${first_word_of_artifactId}s();
}
