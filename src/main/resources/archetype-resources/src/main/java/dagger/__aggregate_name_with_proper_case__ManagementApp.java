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
import ${package}.server.${aggregate_name_with_proper_case}Aggregate;

import javax.inject.Singleton;

public class ${aggregate_name_with_proper_case}ManagementApp {

    @Singleton
    @Component(modules = {ActorSystemModule.class, ${aggregate_name_with_proper_case}ManagementModule.class})
    public interface ${aggregate_name_with_proper_case}App {
        ActorSystem<?> actorSystem();

        AkkaManagement akkaManagement();

        ClusterBootstrap clusterBootstrap();

        DBProjection dbProjection();

        EventsProjection eventsProjection();

        CommandServer commandServer();

        QueryServer queryServer();
    }

    public static void main(String[] args) {
        ${aggregate_name_with_proper_case}App app = Dagger${aggregate_name_with_proper_case}ManagementApp_${aggregate_name_with_proper_case}App.builder().build();
        app.akkaManagement().start();
        app.clusterBootstrap().start();
        ${aggregate_name_with_proper_case}Aggregate.init(app.actorSystem());
        app.dbProjection().startProjection();
        app.eventsProjection().startProjection();
        app.commandServer().startCommandServer();
        app.queryServer().startQueryServer();
    }
}