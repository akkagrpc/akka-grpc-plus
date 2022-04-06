#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
import akka.actor.typed.ActorSystem;
import ${package}.dagger.Dagger${aggregate_name_with_proper_case}ManagementApp_${aggregate_name_with_proper_case}App;
import ${package}.dagger.${aggregate_name_with_proper_case}ManagementApp;
import ${package}.server.CommandServer;
import org.junit.Assert;
import org.junit.Test;

public class ${aggregate_name_with_proper_case}ManagementDaggerTest {

    @Test
    public void givenGeneratedComponent_whenBuilding${aggregate_name_with_proper_case}App_thenDependenciesInjected() {
        ${aggregate_name_with_proper_case}ManagementApp.${aggregate_name_with_proper_case}App app = Dagger${aggregate_name_with_proper_case}ManagementApp_${aggregate_name_with_proper_case}App.create();

        CommandServer injectedCommandServer = app.commandServer();
        ActorSystem<?> injectedActorSystem = app.actorSystem();

        Assert.assertNotNull(injectedCommandServer);
        Assert.assertNotNull(injectedActorSystem);
    }
}
