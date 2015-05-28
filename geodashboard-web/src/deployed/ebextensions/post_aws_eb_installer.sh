#!/usr/bin/env bash

# This script is run by the AWS Elastic Beanstalk installer and it runs after tomcat is installed.

# Copy geoserver to the webapps
mv /usr/share/tomcat6/conf/geoserver.war /usr/share/tomcat6/webapps/geoserver.war
mkdir /usr/share/tomcat6/vault
chmod 777 /usr/share/tomcat6/vault


# Application context
# mv /usr/share/tomcat6/webapps/ROOT /usr/share/tomcat6/webapps/e3rrl