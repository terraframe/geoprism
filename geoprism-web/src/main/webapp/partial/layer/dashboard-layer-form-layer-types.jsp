<%--

    Copyright (c) 2022 TerraFrame, Inc. All rights reserved.

    This file is part of Geoprism(tm).

    Geoprism(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Geoprism(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="../../WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
       
<div id="geom-type-holder" class="row-holder">  
  <div class="label-holder style04">
    <strong><gdb:localize key="DashboardThematicLayer.form.chooseLayerType"/></strong>
  </div>  
  <div class="holder style04">
    <ul class="nav-tabs type-tabs">
      
      <li id={{type}} class={{type}} ng-repeat="type in dynamicDataModel.layerTypeNames" ng-class="{ 'active' : '{{type}}' == '{{layerModel.layerType}}' }">
    
        <!-- data-toggle="tab" REMOVED FROM A ELEMENT BECAUSE OF CONFLICT BETWEEN BOOTSTRAP AND ANGULAR -->
        
        <a href="#" data-gdb-tab-type="{{type}}" ng-click="ctrl.setLayerType(type)"> 

          <div class="rad-area" ng-class="{'rad-checked' : (layerModel.layerType == type)}">
            <span></span>
          </div> 

          <label for="radio{{$index}}">{{dynamicDataModel.layerTypeLabels[$index]}}</label>
        </a>
      </li>
      
    </ul>
  </div>
</div>
