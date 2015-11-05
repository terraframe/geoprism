<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

    This file is part of Runway SDK(tm).

    Runway SDK(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Runway SDK(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>

<div class="panel panel-default" ng-repeat="type in types">
  <a class="opener" data-toggle="collapse" data-parent="#type-accordion" ng-href="#collapse{{$index}}">{{type.label}}</a>
  
  <div id="{{'collapse' + $index}}" class="panel-collapse collapse">
    <div class="panel-body">
      <accordion-attribute ng-repeat="attribute in type.attributes" attribute="attribute" identifier="{{$parent.$index}}-{{$index}}" new-layer="newLayer(mdAttributeId)"></accordion-attribute>
    </div>
  </div>
</div>
