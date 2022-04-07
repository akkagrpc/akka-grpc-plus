import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

Path projectPath = Paths.get(request.outputDirectory, request.artifactId)
Properties properties = request.properties
String packageExp = properties.get("package")
String artifactIdProperCaseExp = properties.get("aggregate_name_with_proper_case")
String artifactIdLowerCaseExp = properties.get("aggregate_name_with_lower_case")
Path protoFilePath = projectPath.resolve("src/main/protobuf/" + "${artifactIdLowerCaseExp}" + ".proto")
Path protoEventsFilePath = projectPath.resolve("src/main/protobuf/" + "${artifactIdLowerCaseExp}" + "events" + ".proto")
String content = Files.readString(protoFilePath)
String event_content = Files.readString(protoEventsFilePath)
content = content.replaceAll("movie", artifactIdLowerCaseExp)
content = content.replaceAll("Movie", artifactIdProperCaseExp)
event_content = event_content.replaceAll("Movie", artifactIdProperCaseExp)
Files.writeString(protoFilePath, content);
Files.writeString(protoEventsFilePath, event_content);

Path ddlFilePath = projectPath.resolve("src/main/resources/ddl-scripts/create_projection_tables.sql")
String ddl_content = Files.readString(ddlFilePath)
ddl_content = ddl_content.replaceAll("movie", artifactIdLowerCaseExp)
Files.writeString(ddlFilePath, ddl_content);

Path appConfFilePath = projectPath.resolve("src/main/resources/application.conf")
String app_conf_content = Files.readString(appConfFilePath)
app_conf_content = app_conf_content.replaceAll("movie", artifactIdLowerCaseExp)
app_conf_content = app_conf_content.replaceAll("Movie", artifactIdProperCaseExp)
Files.writeString(appConfFilePath, app_conf_content);

Path grpcConfFilePath = projectPath.resolve("src/main/resources/grpc.conf")
String grpc_conf_content = Files.readString(grpcConfFilePath)
grpc_conf_content = grpc_conf_content.replaceAll("movie", artifactIdLowerCaseExp)
Files.writeString(grpcConfFilePath, grpc_conf_content);

Path kafkaConfFilePath = projectPath.resolve("src/main/resources/kafka.conf")
String kafka_conf_content = Files.readString(kafkaConfFilePath)
kafka_conf_content = kafka_conf_content.replaceAll("movie", artifactIdLowerCaseExp)
Files.writeString(kafkaConfFilePath, kafka_conf_content);

Path persistenceConfFilePath = projectPath.resolve("src/main/resources/persistence.conf")
String persistence_conf_content = Files.readString(persistenceConfFilePath)
persistence_conf_content = persistence_conf_content.replaceAll("movie", artifactIdLowerCaseExp)
Files.writeString(persistenceConfFilePath, persistence_conf_content);

Path serializationConfFilePath = projectPath.resolve("src/main/resources/serialization.conf")
String serialization_conf_content = Files.readString(serializationConfFilePath)
serialization_conf_content = serialization_conf_content.replaceAll("movie", packageExp)
Files.writeString(serializationConfFilePath, serialization_conf_content);

Path localSharedConfFilePath = projectPath.resolve("src/main/resources/local-shared.conf")
String local_shared_conf_content = Files.readString(localSharedConfFilePath)
local_shared_conf_content = local_shared_conf_content.replaceAll("movie", packageExp)
local_shared_conf_content = local_shared_conf_content.replaceAll("Movie", artifactIdProperCaseExp)
Files.writeString(localSharedConfFilePath, local_shared_conf_content);

Path dc_appFilePath = projectPath.resolve("docker-compose-app.yml")
String dc_app_content = Files.readString(dc_appFilePath)
dc_app_content = dc_app_content.replaceAll("movie", artifactIdLowerCaseExp)
dc_app_content = dc_app_content.replaceAll("Movie", artifactIdProperCaseExp)
Files.writeString(dc_appFilePath, dc_app_content);

Path dc_infraFilePath = projectPath.resolve("docker-compose-infra.yml")
String dc_infra_content = Files.readString(dc_infraFilePath)
dc_infra_content = dc_infra_content.replaceAll("movie", artifactIdLowerCaseExp)
Files.writeString(dc_infraFilePath, dc_infra_content);

Path envoyConfigFilePath = projectPath.resolve("envoy-config.yml")
String envoy_Config_content = Files.readString(envoyConfigFilePath)
envoy_Config_content = envoy_Config_content.replaceAll("Movie", artifactIdProperCaseExp)
Files.writeString(envoyConfigFilePath, envoy_Config_content);

Path startEnvoyFilePath = projectPath.resolve("start-envoy.sh")
String start_envoy_content = Files.readString(startEnvoyFilePath)
start_envoy_content = start_envoy_content.replaceAll("Movie", artifactIdProperCaseExp)
Files.writeString(startEnvoyFilePath, start_envoy_content);

Path descriptorFilePath = projectPath.resolve("generate-descriptor.sh")
String generate_descriptor_content = Files.readString(descriptorFilePath)
generate_descriptor_content = generate_descriptor_content.replaceAll("movie", artifactIdLowerCaseExp)
Files.writeString(descriptorFilePath, generate_descriptor_content);

Path pomXmlFilePath = projectPath.resolve("pom.xml")
String pom_xml_content = Files.readString(pomXmlFilePath)
pom_xml_content = pom_xml_content.replaceAll("movie", packageExp)
pom_xml_content = pom_xml_content.replaceAll("Movie", artifactIdProperCaseExp)
Files.writeString(pomXmlFilePath, pom_xml_content);