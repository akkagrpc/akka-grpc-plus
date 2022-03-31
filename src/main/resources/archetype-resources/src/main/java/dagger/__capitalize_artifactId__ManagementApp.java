#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dagger;

import akka.actor.typed.ActorSystem;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import akka.management.javadsl.AkkaManagement;
import dagger.Component;
import ${package}.projection.DBProjection;
import ${package}.projection.EventsProjection;
import ${package}.query.QueryServer;
import ${package}.server.CommandServer;
import ${package}.server.${capitalize_artifactId}Aggregate;

import javax.inject.Singleton;

public class ${capitalize_artifactId}ManagementApp {

    @Singleton
    @Component(modules = {ActorSystemModule.class, ${capitalize_artifactId}ManagementModule.class})
    public interface ${capitalize_artifactId}App {
        ActorSystem<?> actorSystem();

        AkkaManagement akkaManagement();

        ClusterBootstrap clusterBootstrap();

        DBProjection dbProjection();

        EventsProjection eventsProjection();

        CommandServer commandServer();

        QueryServer queryServer();
    }

    public static void main(String[] args) {
        ${capitalize_artifactId}App app = Dagger${capitalize_artifactId}ManagementApp_${capitalize_artifactId}App.builder().build();
        app.akkaManagement().start();
        app.clusterBootstrap().start();
        ${capitalize_artifactId}Aggregate.init(app.actorSystem());
        app.dbProjection().startProjection();
        app.eventsProjection().startProjection();
        app.commandServer().startCommandServer();
        app.queryServer().startQueryServer();
    }
}