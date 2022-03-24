#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
import akka.actor.typed.ActorSystem;
import ${package}.dagger.DaggerMovieManagementApp_MovieApp;
import ${package}.dagger.MovieManagementApp;
import ${package}.server.CommandServer;
import org.junit.Assert;
import org.junit.Test;

public class MovieManagementDaggerTest {

    @Test
    public void givenGeneratedComponent_whenBuildingSecureTemplateApp_thenDependenciesInjected() {
        MovieManagementApp.MovieApp ${package}App = DaggerMovieManagementApp_MovieApp.create();

        CommandServer injectedCommandServer = ${package}App.commandServer();
        ActorSystem<?> injectedActorSystem = ${package}App.actorSystem();

        Assert.assertNotNull(injectedCommandServer);
        Assert.assertNotNull(injectedActorSystem);
    }
}
