#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.query;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import ${package}.server.${aggregate_name_with_proper_case};
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ${aggregate_name_with_proper_case}DAO {
    Mono<${aggregate_name_with_proper_case}> get${aggregate_name_with_proper_case}ById(String ${aggregate_name_with_lower_case}Id);

    Source<${aggregate_name_with_proper_case}, NotUsed> get${aggregate_name_with_proper_case}By${aggregate_name_with_proper_case}Id(String ${aggregate_name_with_lower_case}Id);

    Flux<${aggregate_name_with_proper_case}> get${aggregate_name_with_proper_case}s();
}
