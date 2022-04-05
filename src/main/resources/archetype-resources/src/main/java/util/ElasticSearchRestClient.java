#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import ${package}.server.ESRecord;
import ${package}.server.${first_word_of_artifactId}Report;
import org.elasticsearch.client.RestHighLevelClient;

public interface ElasticSearchRestClient {

    ESRecord convertToESRecord(${first_word_of_artifactId}Report record);

    RestHighLevelClient getElasticSearchRestClient(boolean esAuthenticationEnabled);
}
