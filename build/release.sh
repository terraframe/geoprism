#
# Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
#
# This file is part of Geoprism(tm).
#
# Geoprism(tm) is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# Geoprism(tm) is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
#

#if curl -f -s --head "http://nexus.terraframe.com/service/local/artifact/maven/redirect?r=allrepos&g=net.geoprism&a=geoprism-web&p=war&v=$GP_VERSION" | head -n 1 | grep "HTTP/1.[01] [23].." > /dev/null; then
#   echo "The release version $GP_VERSION has already been deployed! Please ensure you are releasing the correct version of geoprism."
#   exit 1
#fi

git config --global user.name "$GIT_TF_BUILDER_USERNAME"
git config --global user.email builder@terraframe.com

cd $WORKSPACE/geoprism

git checkout dev
git pull

mvn license:format
git add -A
git diff-index --quiet HEAD || git commit -m 'License headers'
git push

git checkout master
git merge dev
git push


mvn release:prepare -B -Dtag=$GP_VERSION \
                 -DreleaseVersion=$GP_VERSION \
                 -DdevelopmentVersion=$GP_NEXT
                 
mvn release:perform -B -Darguments="-Dmaven.javadoc.skip=true -Dmaven.site.skip=true"


cd ..
rm -rf rwdev
mkdir rwdev
cd rwdev
git clone -b master git@github.com:terraframe/geoprism.git
cd geoprism
git checkout dev
git merge master
git push