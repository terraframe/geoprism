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


<div>
  <div class="label-holder">
    <strong> </strong>
  </div>
  <div class="holder">
    <div class="row-holder">
    	<p><gdb:localize key="dataUploader.titleUploadToExistingOrNewSubtitle"/></p>
    </div>
  </div>
  
  <div class="label-holder">
    <strong><gdb:localize key="dataUploader.existingDataset"/></strong>
  </div>
  <div class="holder">
  	<ul class="list-group">   
        <li class="list-group-item" ng-repeat="match in sheet.matches track by $index">
        	{{match.label}}            
  		
  		    <a href="#" class="fa-stack fa-1x pull-right" ng-click="ctrl.select(match, true)" title="<gdb:localize key="dataUploader.replaceDataset"/>">
    			<i class="fa fa-file-o fa-stack-2x"></i>
      			<i class="fa-stack-1x fa-stack-text file-text fa fa-minus"></i>
  			</a>
  			<a href="#" class="fa-stack fa-1x pull-right" ng-click="ctrl.select(match, false)" title="<gdb:localize key="dataUploader.selectDataset"/>">
    			<i class="fa fa-file-o fa-stack-2x"></i>
      			<i class="fa-stack-1x fa-stack-text file-text fa fa-plus"></i>
  			</a>

  		</li>
    </ul>
  </div> 
</div>