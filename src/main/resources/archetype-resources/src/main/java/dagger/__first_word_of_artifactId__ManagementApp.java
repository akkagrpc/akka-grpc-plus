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
import ${package}.server.${first_word_of_artifactId}Aggregate;

import javax.inject.Singleton;

public class ${first_word_of_artifactId}ManagementApp {

    @Singleton
    @Component(modules = {ActorSystemModule.class, ${first_word_of_artifactId}ManagementModule.class})
    public interface ${first_word_of_artifactId}App {
        ActorSystem<?> actorSystem();

        AkkaManagement akkaManagement();

        ClusterBootstrap clusterBootstrap();

        DBProjection dbProjection();

        EventsProjection eventsProjection();

        CommandServer commandServer();

        QueryServer queryServer();
    }

    public static void main(String[] args) {
        ${first_word_of_artifactId}App app = Dagger${first_word_of_artifactId}ManagementApp_${first_word_of_artifactId}App.builder().build();
        app.akkaManagement().start();
        app.clusterBootstrap().start();
        ${first_word_of_artifactId}Aggregate.init(app.actorSystem());
        app.dbProjection().startProjection();
        app.eventsProjection().startProjection();
        app.commandServer().startCommandServer();
        app.queryServer().startQueryServer();
    }
}