#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
import akka.actor.typed.ActorSystem;
import ${package}.dagger.Dagger${first_word_of_artifactId}ManagementApp_${first_word_of_artifactId}App;
import ${package}.dagger.${first_word_of_artifactId}ManagementApp;
import ${package}.server.CommandServer;
import org.junit.Assert;
import org.junit.Test;

public class ${first_word_of_artifactId}ManagementDaggerTest {

    @Test
    public void givenGeneratedComponent_whenBuildingSecureTemplateApp_thenDependenciesInjected() {
        ${first_word_of_artifactId}ManagementApp.${first_word_of_artifactId}App app = Dagger${first_word_of_artifactId}ManagementApp_${first_word_of_artifactId}App.create();

        CommandServer injectedCommandServer = app.commandServer();
        ActorSystem<?> injectedActorSystem = app.actorSystem();

        Assert.assertNotNull(injectedCommandServer);
        Assert.assertNotNull(injectedActorSystem);
    }
}
