/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
import org.apache.commons.lang.StringUtils;

def extensionDir = session.executionRootDirectory;
def extensionPom = new File(extensionDir + "/pom.xml").text;
def projectName = StringUtils.substringBetween(extensionPom, "<artifactId>", "</artifactId>");

if (projectName.equals("geoprism"))
{
  project.properties["overrideServerPropertiesPath"] = extensionDir + "/" + projectName + "-server/target/classes/runwaysdk/server.properties";
}
else
{
  project.properties["overrideServerPropertiesPath"] = extensionDir + "/" + projectName + "-server/src/main/resources/runwaysdk/server.properties";
}

println "Extension server.properties file resolved to [" + project.properties["overrideServerPropertiesPath"] + "]";