#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import ${package}.server.ESRecord;
import ${package}.server.${aggregate_name_with_proper_case}Report;
import org.elasticsearch.client.RestHighLevelClient;

public interface ElasticSearchRestClient {

    ESRecord convertToESRecord(${aggregate_name_with_proper_case}Report record);

    RestHighLevelClient getElasticSearchRestClient(boolean esAuthenticationEnabled);
}
