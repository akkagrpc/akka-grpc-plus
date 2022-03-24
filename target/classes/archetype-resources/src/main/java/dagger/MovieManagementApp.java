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
import ${package}.server.MovieAggregate;

import javax.inject.Singleton;

public class MovieManagementApp {

    @Singleton
    @Component(modules = {ActorSystemModule.class, MovieManagementModule.class})
    public interface MovieApp {
        ActorSystem<?> actorSystem();

        AkkaManagement akkaManagement();

        ClusterBootstrap clusterBootstrap();

        DBProjection dbProjection();

        EventsProjection eventsProjection();

        CommandServer commandServer();

        QueryServer queryServer();
    }

    public static void main(String[] args) {
        MovieApp ${package}App = DaggerMovieManagementApp_MovieApp.builder().build();
        ${package}App.akkaManagement().start();
        ${package}App.clusterBootstrap().start();
        MovieAggregate.init(${package}App.actorSystem());
        ${package}App.dbProjection().startProjection();
        ${package}App.eventsProjection().startProjection();
        ${package}App.commandServer().startCommandServer();
        ${package}App.queryServer().startQueryServer();
    }
}