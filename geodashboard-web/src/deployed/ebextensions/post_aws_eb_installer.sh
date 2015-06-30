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


# This script is run by the AWS Elastic Beanstalk installer and it runs after tomcat is installed.

# Copy geoserver to the webapps
mv /usr/share/tomcat6/conf/geoserver.war /usr/share/tomcat6/webapps/geoserver.war

# Make vault directory
mkdir /usr/share/tomcat6/vault
chmod 770 /usr/share/tomcat6/vault
# chown tomcat:tomcat /usr/share/tomcat6/vault

# Application context
# mv /usr/share/tomcat6/webapps/ROOT /usr/share/tomcat6/webapps/e3rrl