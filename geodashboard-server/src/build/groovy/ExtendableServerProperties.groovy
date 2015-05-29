import org.apache.commons.lang.StringUtils;

def extensionDir = session.executionRootDirectory;
def extensionPom = new File(extensionDir + "/pom.xml").text;
def projectName = StringUtils.substringBetween(extensionPom, "<artifactId>", "</artifactId>");

project.properties["overrideServerPropertiesPath"] = extensionDir + "/" + projectName + "-server/src/main/resources/runwaysdk/server.properties";
println "Extension server.properties file resolved to [" + project.properties["overrideServerPropertiesPath"] + "]";