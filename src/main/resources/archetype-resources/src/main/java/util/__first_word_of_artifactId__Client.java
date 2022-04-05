#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import akka.actor.ActorSystem;
import akka.discovery.Discovery;
import akka.discovery.ServiceDiscovery;
import akka.grpc.GrpcClientSettings;
import com.akkagrpc.grpc.${first_word_of_artifactId}ServiceClient;
import com.typesafe.config.Config;
import io.grpc.StatusRuntimeException;

public class ${first_word_of_artifactId}Client {
    private static final String JWT_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0c1Q2VG1PWHIyWjhiTnJuaHZsRkV1TlEwWTh5eG4weDBsTHN5UEl4REo4In0.eyJleHAiOjE2NDQ4NDgwOTMsImlhdCI6MTY0NDg0Nzc5MywianRpIjoiZjY5OTAwNDQtODE3OC00YWZlLWE2ZGItMmFhNmRhYjgzMmZiIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL0lYTWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImIwOWU3NWUyLTM5YjEtNDIzYS1iMjdkLWM5NzBmMmUxNmMyNCIsInR5cCI6IkJlYXJlciIsImF6cCI6Iml4bGFnb20iLCJzZXNzaW9uX3N0YXRlIjoiMjMzZGY1ZTgtZDdjOC00MTgwLTk4ZDYtZDY0MGJiZjFjNjM4IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjkwMDAvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1peG1hc3RlciIsIml4dXNlciIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiIyMzNkZjVlOC1kN2M4LTQxODAtOThkNi1kNjQwYmJmMWM2MzgiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InRlc3R1c2VyIn0.cS_7LXGVKS-MR74LgIxCmmP6vz6HD-h9-7P0pPMee0lcgXWhLTZh9a0G9DpY30AN9deFi1fVVmWaPXwigtnRXZPcQ5qYfDCv7qDntUq3ZAM4seV9Bs4yWKXyvFMtE3fnG7bUIDdZ_h08427L2P5xfJ-TnvSB7pI8dRwZ3imuAAkECDVD_yJibHkdeBmDNozCoPeX6LqCFHc-9rYLInfwna-NlgQgd9QVuuyQ-XTSeF48QkwBcQuz8j_MFugnYcJLawIAjpcjN4yXiiUoesgYl7Vi5R_ahza9cvBypEIT0fWaJN4-jl23ngyMmMCnqxN8gzAW-n72CTIDuj2VOixcUw";

    public static void main(String[] args) {

        final ActorSystem system = ActorSystem.create();

        Config config = system.settings().config();
        String serverHost = config.getString("secure-template-service.grpc.interface");
        int serverPort = config.getInt("secure-template-service.grpc.port");

        ServiceDiscovery serviceDiscovery = Discovery.get(system).discovery();

        // Configure the client by code:
        GrpcClientSettings settings = GrpcClientSettings
                .connectToServiceAt(serverHost, serverPort, system)
                .usingServiceDiscovery("SecureTemplateService", system)
                .withTls(false);

        // Or via application.conf:
        // GrpcClientSettings settings = GrpcClientSettings.fromConfig(GreeterService.name, system);

        ${first_word_of_artifactId}ServiceClient client = null;
        try {
            client = ${first_word_of_artifactId}ServiceClient.create(settings, system);
            //registerSecureTemplate(client);
        } catch (StatusRuntimeException e) {
            System.out.println("Status: " + e.getStatus());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (client != null) client.close();
            system.terminate();
        }
    }
    /*private static void registerSecureTemplate(SecureTemplateServiceClient client) throws Exception {
        RegisterSecureTemplateRequest request = RegisterSecureTemplateRequest.newBuilder()
                .setTemplateName("TestSecTemplate")
                .setDescription("SecTemplateDescription")
                .setDomainName("npb")
                .setIpAddress("10.210.0.5")
                .setPort("389")
                .setUsername("rkatwate")
                .setPassword("G2R?6CyTU")
                .setGroup("Domain Users")
                .setOwner("Raviraj Katwate")
                .setPermission("Full")
                .build();
        CompletionStage<RegisterSecureTemplateResponse> registerSecureTemplateReply = client.registerSecureTemplate().addHeader("Authorization", JWT_TOKEN).invoke(request);
        registerSecureTemplateReply.whenComplete((reply, error) -> {
            if (error == null) {
                System.out.println(reply.getTemplateId());
            } else {
                System.out.println(error.getMessage());
            }
        });
    }*/

}