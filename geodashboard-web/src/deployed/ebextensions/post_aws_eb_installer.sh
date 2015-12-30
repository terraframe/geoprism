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

export TOMCAT_HOME=/usr/share/tomcat8

# This script is run by the AWS Elastic Beanstalk installer and it runs after tomcat is installed.

# Copy geoserver to the webapps
cp $TOMCAT_HOME/conf/geoserver.war $TOMCAT_HOME/webapps/geoserver.war

# Make vault directory
mkdir $TOMCAT_HOME/vault
chmod 700 $TOMCAT_HOME/vault
chown tomcat:tomcat $TOMCAT_HOME/vault
