#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import io.micrometer.prometheus.PrometheusMeterRegistry;

import javax.inject.Singleton;

@Singleton
public interface MicrometerClient {
    PrometheusMeterRegistry monitoringSystem();
}
