#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MicrometerClientImpl implements MicrometerClient {
    PrometheusMeterRegistry prometheusMeterRegistry;

    @Inject
    public MicrometerClientImpl(PrometheusMeterRegistry prometheusMeterRegistry) {
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }


    public PrometheusMeterRegistry monitoringSystem() {
        new ClassLoaderMetrics().bindTo(prometheusMeterRegistry);
        new JvmMemoryMetrics().bindTo(prometheusMeterRegistry);
        new JvmGcMetrics().bindTo(prometheusMeterRegistry);
        new ProcessorMetrics().bindTo(prometheusMeterRegistry);
        new JvmThreadMetrics().bindTo(prometheusMeterRegistry);
        return prometheusMeterRegistry;
    }

    static String getMetrics(PrometheusMeterRegistry registry) {
        return registry.scrape();
    }
}
