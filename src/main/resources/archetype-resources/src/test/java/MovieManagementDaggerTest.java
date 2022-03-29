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
        MovieManagementApp.MovieApp movieApp = DaggerMovieManagementApp_MovieApp.create();

        CommandServer injectedCommandServer = movieApp.commandServer();
        ActorSystem<?> injectedActorSystem = movieApp.actorSystem();

        Assert.assertNotNull(injectedCommandServer);
        Assert.assertNotNull(injectedActorSystem);
    }
}
