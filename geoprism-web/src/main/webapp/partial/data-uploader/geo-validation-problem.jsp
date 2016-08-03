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
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<div class="row-holder">    
  <div>
<!--     <hr> -->
    <div class="error-message" ng-repeat="error in errors track by $index">
      <p >{{error}}</p>
    </div>
    <ng-form name="ctrl.problemForm" isolate-form>
      <div class="inline-value">
        <ol ng-if="problem.context.length > 0">
          <li ng-repeat="context in problem.context">{{context.label}} ({{context.universal}})</li>        
        </ol>
      </div>
      <div class="inline-value error-message">{{problem.label}} ({{problem.universalLabel}})</div>
      <div ng-if="!problem.resolved">      
        <div class="inline-combo">
          <input class="synonym" name="{{::$index + '-name'}}" type="text" autocomplete="on" ng-required="true" callback-auto-complete source="ctrl.getGeoEntitySuggestions" setter="ctrl.setSynonym"></input>
        </div>
        <div class="inline-value"><input type="button" value="<gdb:localize key="dataUploader.createSynonym"/>" class="btn btn-primary" ng-click="ctrl.createSynonym()" ng-disabled="ctrl.problemForm.$invalid" /></div>
        <div class="inline-value"><input type="button"  value="<gdb:localize key="dataUploader.createNewEntity"/>" class="btn" ng-click="ctrl.createEntity()" /></div>
      </div>
      <div ng-if="problem.resolved">
        <div class="inline-value"><input type="button" value="<gdb:localize key="dataUploader.undo"/>" class="btn btn-primary" ng-click="ctrl.undoAction()"/></div>
      </div>
    </ng-form>
  </div>
</div>