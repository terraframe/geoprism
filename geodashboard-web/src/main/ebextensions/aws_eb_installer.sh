#!/usr/bin/env bash

# This script is run by the AWS Elastic Beanstalk installer

# Install Oracle JDK
rpm -Uvh jdk-6u45-linux-amd64.rpm
alternatives --install /usr/bin/java java /usr/java/default/bin/java 3
alternatives --set java /usr/java/default/bin/java

export TOMCAT_HOME=/usr/share/tomcat6 # This value is also hardcoded in server.xml
export JAVA_HOME=/usr/lib/jvm/jre

# Forward port 443 to 8443
/sbin/iptables -t nat -I PREROUTING -p tcp --dport 443 -j REDIRECT --to-port 8443
/sbin/service iptables save

# Deploy the tomcat/lib jars
cp -R .ebextensions/tomcat-lib/* $TOMCAT_HOME/lib/

# Add our security cert to the jvm
$JAVA_HOME/bin/keytool -importcert -alias ideiq -file .ebextensions/ideiq.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt
cp .ebextensions/ideiq.ks $TOMCAT_HOME/conf/ideiq.ks
# sed -i "s/{TOMCAT_HOME}/$TOMCAT_HOME/g" .ebextensions/server.xml

# Copy the server.xml config file
cp .ebextensions/server.xml $TOMCAT_HOME/conf/server.xml

# Copy the geoserver war over
cp .ebextensions/geoserver.war $TOMCAT_HOME/conf/geoserver.war
