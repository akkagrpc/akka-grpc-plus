#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import akka.actor.ActorSystem;
import akka.discovery.Discovery;
import akka.discovery.ServiceDiscovery;
import akka.grpc.GrpcClientSettings;
import com.akkagrpc.grpc.${aggregate_name_with_proper_case}ServiceClient;
import com.typesafe.config.Config;
import io.grpc.StatusRuntimeException;

public class ${aggregate_name_with_proper_case}Client {
    private static final String JWT_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0c1Q2VG1PWHIyWjhiTnJuaHZsRkV1TlEwWTh5eG4weDBsTHN5UEl4REo4In0.eyJleHAiOjE2NDQ4NDgwOTMsImlhdCI6MTY0NDg0Nzc5MywianRpIjoiZjY5OTAwNDQtODE3OC00YWZlLWE2ZGItMmFhNmRhYjgzMmZiIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL0lYTWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImIwOWU3NWUyLTM5YjEtNDIzYS1iMjdkLWM5NzBmMmUxNmMyNCIsInR5cCI6IkJlYXJlciIsImF6cCI6Iml4bGFnb20iLCJzZXNzaW9uX3N0YXRlIjoiMjMzZGY1ZTgtZDdjOC00MTgwLTk4ZDYtZDY0MGJiZjFjNjM4IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjkwMDAvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1peG1hc3RlciIsIml4dXNlciIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiIyMzNkZjVlOC1kN2M4LTQxODAtOThkNi1kNjQwYmJmMWM2MzgiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InRlc3R1c2VyIn0.cS_7LXGVKS-MR74LgIxCmmP6vz6HD-h9-7P0pPMee0lcgXWhLTZh9a0G9DpY30AN9deFi1fVVmWaPXwigtnRXZPcQ5qYfDCv7qDntUq3ZAM4seV9Bs4yWKXyvFMtE3fnG7bUIDdZ_h08427L2P5xfJ-TnvSB7pI8dRwZ3imuAAkECDVD_yJibHkdeBmDNozCoPeX6LqCFHc-9rYLInfwna-NlgQgd9QVuuyQ-XTSeF48QkwBcQuz8j_MFugnYcJLawIAjpcjN4yXiiUoesgYl7Vi5R_ahza9cvBypEIT0fWaJN4-jl23ngyMmMCnqxN8gzAW-n72CTIDuj2VOixcUw";

    public static void main(String[] args) {

        final ActorSystem system = ActorSystem.create();

        Config config = system.settings().config();
        String serverHost = config.getString("${artifactId}.grpc.interface");
        int serverPort = config.getInt("${artifactId}.grpc.port");

        ServiceDiscovery serviceDiscovery = Discovery.get(system).discovery();

        // Configure the client by code:
        GrpcClientSettings settings = GrpcClientSettings
                .connectToServiceAt(serverHost, serverPort, system)
                .usingServiceDiscovery("${aggregate_name_with_proper_case}Service", system)
                .withTls(false);

        // Or via application.conf:
        // GrpcClientSettings settings = GrpcClientSettings.fromConfig(GreeterService.name, system);

        ${aggregate_name_with_proper_case}ServiceClient client = null;
        try {
            client = ${aggregate_name_with_proper_case}ServiceClient.create(settings, system);
            //register${aggregate_name_with_proper_case}(client);
        } catch (StatusRuntimeException e) {
            System.out.println("Status: " + e.getStatus());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (client != null) client.close();
            system.terminate();
        }
    }
    /*private static void register${aggregate_name_with_proper_case}(${aggregate_name_with_proper_case}ServiceClient client) throws Exception {
        Register${aggregate_name_with_proper_case}Request request = Register${aggregate_name_with_proper_case}Request.newBuilder()
                .set${aggregate_name_with_proper_case}Name("Test${aggregate_name_with_proper_case}")
                .setDescription("${aggregate_name_with_proper_case}Description")
                .setDomainName("npb")
                .setIpAddress("10.210.0.5")
                .setPort("389")
                .setUsername("rkatwate")
                .setPassword("G2R?6CyTU")
                .setGroup("Domain Users")
                .setOwner("Raviraj Katwate")
                .setPermission("Full")
                .build();
        CompletionStage<Register${aggregate_name_with_proper_case}Response> register${aggregate_name_with_proper_case}Reply = client.register${aggregate_name_with_proper_case}().addHeader("Authorization", JWT_TOKEN).invoke(request);
        register${aggregate_name_with_proper_case}Reply.whenComplete((reply, error) -> {
            if (error == null) {
                System.out.println(reply.get${aggregate_name_with_proper_case}Id());
            } else {
                System.out.println(error.getMessage());
            }
        });
    }*/

}
