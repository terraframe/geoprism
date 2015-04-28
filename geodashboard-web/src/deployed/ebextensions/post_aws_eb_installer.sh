#!/usr/bin/env bash

# This script is run by the AWS Elastic Beanstalk installer and it runs after tomcat is installed.

# Copy geoserver to the webapps
mv /usr/share/tomcat6/conf/geoserver.war /usr/share/tomcat6/webapps/geoserver.war
mv /usr/share/tomcat6/webapps/ROOT /usr/share/tomcat6/webapps/ideiq
