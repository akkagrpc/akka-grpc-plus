#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dagger;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import akka.management.javadsl.AkkaManagement;
import com.typesafe.config.Config;
import dagger.Module;
import dagger.Provides;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import ${package}.util.ClusterListenerActor;

import javax.inject.Singleton;

@Module()
public class ActorSystemModule {
    public final String SYSTEM_NAME = "MovieService";
    @Provides
    @Singleton
    public ActorSystem<?> provideActorSystem() {
        return ActorSystem.create(create(), SYSTEM_NAME);
    }

    @Provides
    @Singleton
    public AkkaManagement provideAkkaManagement(ActorSystem<?> system) {
        return AkkaManagement.get(system);
    }

    @Provides
    @Singleton
    public ClusterBootstrap provideClusterBootstrap(ActorSystem<?> system) {
        return ClusterBootstrap.get(system);
    }

    @Provides
    @Singleton
    public Config provideConfig(ActorSystem<?> system) {
        return system.settings().config();
    }

    @Provides
    @Singleton
    public PrometheusMeterRegistry providePrometheusMeterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    private Behavior<Void> create() {
        return Behaviors.setup(context -> {
            bootstrap(context);
            return Behaviors.receive(Void.class)
                    .onSignal(Terminated.class, signal -> Behaviors.stopped())
                    .build();
        });
    }

    private void bootstrap(final ActorContext<Void> context) {
        context.spawn(ClusterListenerActor.create(), "clusterListener");
    }
}

