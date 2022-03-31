#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
import akka.actor.typed.ActorSystem;
import ${package}.dagger.DaggerMovieManagementApp_MovieApp;
import ${package}.dagger.MovieManagementApp;
import ${package}.server.CommandServer;
import org.junit.Assert;
import org.junit.Test;

public class ${capitalize_artifactId}ManagementDaggerTest {

    @Test
    public void givenGeneratedComponent_whenBuildingSecureTemplateApp_thenDependenciesInjected() {
        ${capitalize_artifactId}ManagementApp.${capitalize_artifactId}App app = Dagger${capitalize_artifactId}ManagementApp_${capitalize_artifactId}App.create();

        CommandServer injectedCommandServer = app.commandServer();
        ActorSystem<?> injectedActorSystem = app.actorSystem();

        Assert.assertNotNull(injectedCommandServer);
        Assert.assertNotNull(injectedActorSystem);
    }
}
