import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

Path projectPath = Paths.get(request.outputDirectory, request.artifactId)
Properties properties = request.properties
String packageExp = properties.get("package")
String destExp = packageExp.substring(0, 1).toUpperCase() + packageExp.substring(1)
String protoFileName = properties.get("package")
Path protoFilePath = projectPath.resolve("src/main/protobuf/" + "${protoFileName}" + ".proto")
Path protoEventsFilePath = projectPath.resolve("src/main/protobuf/" + "${protoFileName}" + "events" + ".proto")
String content = Files.readString(protoFilePath)
String event_content = Files.readString(protoEventsFilePath)
content = content.replaceAll("movie", packageExp)
content = content.replaceAll("Movie", destExp)
event_content = event_content.replaceAll("Movie", destExp)
Files.writeString(protoFilePath, content);
Files.writeString(protoEventsFilePath, event_content);