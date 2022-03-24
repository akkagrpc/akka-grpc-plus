#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import ${package}.server.ESRecord;
import ${package}.server.MovieReport;
import org.elasticsearch.client.RestHighLevelClient;

public interface ElasticSearchRestClient {

    ESRecord convertToESRecord(MovieReport record);

    RestHighLevelClient getElasticSearchRestClient(boolean esAuthenticationEnabled);
}
