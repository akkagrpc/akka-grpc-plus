import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

Path projectPath = Paths.get(request.outputDirectory, request.artifactId)
Properties properties = request.properties
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