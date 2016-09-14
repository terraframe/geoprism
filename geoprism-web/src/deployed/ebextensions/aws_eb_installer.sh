#!/usr/bin/env bash
#
# Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
#
# This file is part of Runway SDK(tm).
#
# Runway SDK(tm) is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# Runway SDK(tm) is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
#

# This script is run by the AWS Elastic Beanstalk installer
set -o verbose

# Install Oracle JDK
wget -O .ebextensions/jre-8u60-linux-x64.rpm "http://nexus.terraframe.com/service/local/artifact/maven/redirect?r=releases&g=com.oracle&a=server-jre-linux&v=8u60&e=rpm&c=x64"
rpm --erase --nodeps java-1.8.0-openjdk-1.8.0.51-1.b16.6.amzn1.x86_64 java-1.8.0-openjdk-headless-1.8.0.51-1.b16.6.amzn1.x86_64
rpm -Uvh .ebextensions/jre-8u60-linux-x64.rpm
/usr/sbin/alternatives --install /usr/bin/java java /usr/java/default/bin/java 3
/usr/sbin/alternatives --set java /usr/java/default/bin/java
/usr/sbin/alternatives --install /usr/bin/java_sdk java_sdk /usr/java/default/bin/java 3
/usr/sbin/alternatives --set java_sdk /usr/java/default/bin/java

export TOMCAT_HOME=/usr/share/tomcat8 # This value is also hardcoded in server.xml, the post_aws_eb_installer, platform.properties, log4j.properties, java environoment variables
export JAVA_HOME=/usr/java/default # This is also hardcoded in platform.properties
#export JAVA_HOME=/usr/java/default/jre # This is the value for when we're using a jdk

# Forward port 443 to 8443
/sbin/iptables -t nat -I PREROUTING -p tcp --dport 443 -j REDIRECT --to-port 8443
/sbin/service iptables save

# Deploy the tomcat/lib jars
cp -R .ebextensions/tomcat-lib/* $TOMCAT_HOME/lib/

# Add our security cert to the jvm
$JAVA_HOME/bin/keytool -importcert -alias geoprism -file .ebextensions/geoprism.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt
cp .ebextensions/geoprism.ks $TOMCAT_HOME/conf/geoprism.ks
cp .ebextensions/geoprism.tstore $TOMCAT_HOME/conf/geoprism.ts
# sed -i "s/{TOMCAT_HOME}/$TOMCAT_HOME/g" .ebextensions/server.xml

# Copy the server.xml config file
cp .ebextensions/server.xml $TOMCAT_HOME/conf/server.xml

# Copy the geoserver war over
# cp .ebextensions/geoserver.war $TOMCAT_HOME/conf/geoserver.war

# Download the latest geoserver war to a temp directory. This will be moved into webapps in the post install script.
wget -O $TOMCAT_HOME/conf/geoserver.war "http://nexus.terraframe.com/service/local/repositories/releases/content/org/geoserver/geoserver/2.9.1/geoserver-2.9.1.war"

# Copy Post Installation Script:
# mkdir /opt/elasticbeanstalk/hooks/appdeploy/post
cp .ebextensions/post_aws_eb_installer.sh /opt/elasticbeanstalk/hooks/appdeploy/post/02post_aws_eb_installer.sh
chmod +x /opt/elasticbeanstalk/hooks/appdeploy/post/02post_aws_eb_installer.sh

echo "aws_eb_installer script success"