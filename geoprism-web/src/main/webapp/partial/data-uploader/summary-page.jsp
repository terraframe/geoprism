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
  <div ng-if="ctrl.hasFieldType('TEXT')">
	  <div class="label-holder">
	    <strong><gdb:localize key="dataUploader.summaryTextLabel"/></strong>
	  </div>
	  <div class="holder">
	    <div class="row-holder">
	    	 <table class="table table-bordered" style="font-size:inherit;"> 
	         	<thead> 
	         		<tr>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.type.label"/></th>
	         		</tr>
	         	</thead>
	         	<tbody>	
	         		<tr ng-repeat="field in sheet.fields" ng-if="ctrl.isValid(field) && field.type === 'TEXT'">
	         			<td>{{field.label}}</td>
	         			<td>
	            			<div><gdb:localize key="dataUploader.text"/></div>
	          			</td>          
	         		</tr>
	         	</tbody>
	         </table>
	    </div>
	  </div>
  </div>

  <div ng-if="ctrl.hasFieldType('CATEGORY')">
	  <div class="label-holder">
	    <strong><gdb:localize key="dataUploader.summaryCategoryLabel"/></strong>
	  </div>
	  <div class="holder">
	    <div class="row-holder">         
	         <table class="table table-bordered" style="font-size:inherit;"> 
	         	<thead> 
	         		<tr>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.type.label"/></th>
	         		</tr>
	         	</thead>
	         	<tbody>	
	         		<tr ng-repeat="field in sheet.fields" ng-if="ctrl.isValid(field) && field.type === 'CATEGORY'">
	         			<td>{{field.label}}</td>
	         			<td>
	            			<div><gdb:localize key="dataUploader.category"/></div>
	          			</td>          
	         		</tr>
	         	</tbody>
	         </table>
	    </div>
	  </div>
  </div>
  
  <div ng-if="ctrl.hasFieldType('DOUBLE') || ctrl.hasFieldType('LONG')">
	  <div class="label-holder">
	    <strong><gdb:localize key="dataUploader.summaryNumberLabel"/></strong>
	  </div>
	  <div class="holder">
	    <div class="row-holder">    
	         <table class="table table-bordered" style="font-size:inherit;"> 
	         	<thead> 
	         		<tr>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.type.label"/></th>
	         		</tr>
	         	</thead>
	         	<tbody>	
	         		<tr ng-repeat="field in sheet.fields | orderBy:'type'" ng-if="ctrl.isValid(field) && field.type === 'DOUBLE' || field.type === 'LONG'">
	         			<td>{{field.label}}</td>
	         			<td ng-switch on="field.type">
	            			<div ng-switch-when="LONG"><gdb:localize key="dataUploader.long"/></div>
	            			<div ng-switch-when="DOUBLE"><gdb:localize key="dataUploader.double"/></div>
	          			</td>          
	         		</tr>
	         	</tbody>
	         </table>
	      </div>
	  </div>
  </div>
   
  <div ng-if="ctrl.hasFieldType('BOOLEAN')">
	  <div class="label-holder">
	    <strong><gdb:localize key="dataUploader.summaryBooleanLabel"/></strong>
	  </div>
	  <div class="holder">
	    <div class="row-holder">   
	         <table class="table table-bordered" style="font-size:inherit;"> 
	         	<thead> 
	         		<tr>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.type.label"/></th>
	         		</tr>
	         	</thead>
	         	<tbody>	
	         		<tr ng-repeat="field in sheet.fields" ng-if="ctrl.isValid(field) && field.type === 'BOOLEAN'">
	         			<td>{{field.label}}</td>
	         			<td>
	            			<div><gdb:localize key="dataUploader.boolean"/></div>
	          			</td>          
	         		</tr>
	         	</tbody>
	         </table>
	    </div>
	  </div>
  </div>

  <div ng-if="ctrl.hasFieldType('DATE')">
	  <div class="label-holder">
	    <strong><gdb:localize key="dataUploader.summaryDateLabel"/></strong>
	  </div>
	  <div class="holder">
	    <div class="row-holder">            
	         <table class="table table-bordered" style="font-size:inherit;"> 
	         	<thead> 
	         		<tr>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.type.label"/></th>
	         		</tr>
	         	</thead>
	         	<tbody>	
	         		<tr ng-repeat="field in sheet.fields" ng-if="ctrl.isValid(field) && field.type === 'DATE'">
	         			<td>{{field.label}}</td>
	         			<td>
	            			<div><gdb:localize key="dataUploader.date"/></div>
	          			</td>          
	         		</tr>
	         	</tbody>
	         </table>
	    </div>
	  </div>
  </div>
   
  <div ng-if="ctrl.hasFieldType('LOCATION')">
	  <div class="label-holder">
	    <strong><gdb:localize key="dataUploader.summaryTextLocationLabel"/></strong>
	  </div>
	  <div class="holder">
	    <div class="row-holder">         
	         <table class="table table-bordered" style="font-size:inherit;"> 
	         	<thead> 
	         		<tr>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.type.label"/></th>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.textLocRefFields.label"/></th>
	         		</tr>
	         	</thead>
	         	<tbody>	
	         	
	         		<!-- TEXT BASED LOCATION FIELDS -->
	       			<tr ng-repeat="id in sheet.attributes.ids" ng-init="attribute = sheet.attributes.values[id]">
	          			<td>{{attribute.label}}</td>
	          			<td><gdb:localize key="dataUploader.summary.textLocation.label"/></td>
	          			<td>
			          		<table class="table table-bordered location-summary-sub-table"> 
			          			<tbody>
			          			    <tr ng-repeat="universal in universals" ng-if="attribute.fields[universal.value] != null && attribute.fields[universal.value] != 'EXCLUDE'">
	              						<td>{{attribute.fields[universal.value]}}</td>
	              					</tr> 
			          			</tbody>
			          		</table>
	          			</td>
	        		</tr>  
	         	</tbody>
	         </table>
	      </div>
	   </div>
  </div>
    
  <div ng-if="ctrl.hasFieldType('LATITUDE') && ctrl.hasFieldType('LONGITUDE')">
	  <div class="label-holder">
	    <strong><gdb:localize key="dataUploader.summaryCoordinateLabel"/></strong>
	  </div>
	  <div class="holder">
	    <div class="row-holder">        
	         <table class="table table-bordered" style="font-size:inherit;"> 
	         	<thead> 
	         		<tr>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.type.label"/></th>
	         			<th><gdb:localize key="dataUploader.summary.tableHeading.coordLocRefFields.label"/></th>
	         		</tr>
	         	</thead>
	         	<tbody>	
	         	
					<!-- COORDINATE LOCATION FIELDS -->
			        <tr ng-repeat="id in sheet.coordinates.ids" ng-init="coordinate = sheet.coordinates.values[id]">
			          	<td>{{coordinate.label}}</td>
			          	<td><gdb:localize key="dataUploader.summary.coordinateLocation.label"/></td>
			          	<td> 
			          		<table class="table table-bordered location-summary-sub-table"> 
					         	<thead> 
					         		<tr>
					         			<th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
					         			<th><gdb:localize key="dataUploader.summary.tableHeading.refField.label"/></th>
					         		</tr>
					         	</thead>
			          			<tbody>
				          			<tr><td><gdb:localize key="dataUploader.latitude"/></td><td>{{coordinate.latitude}}</td></tr>
					            	<tr><td><gdb:localize key="dataUploader.longitude"/></td><td>{{coordinate.longitude}}</td></tr>
					            	<tr><td><gdb:localize key="dataUploader.featureLabel"/></td><td>{{coordinate.featureLabel}}</td></tr>
					            	<tr ng-if="coordinate.location != 'DERIVE'"><td><gdb:localize key="dataUploader.locationAttribute"/></td><td>{{coordinate.location}}</td></tr>
					            	<tr ng-if="coordinate.location == 'DERIVE'"><td><gdb:localize key="dataUploader.locationAttribute"/></td><td><gdb:localize key="dataUploader.deriveLocation"/></td></tr>
			          				<tr ng-if="coordinate.location == 'DERIVE'"><td><gdb:localize key="dataUploader.associatedUniversal"/></td><td>{{labels[coordinate.universal]}}</td></tr>
			          			</tbody>
			          		</table>
				        </td>      
			        </tr>        
	         	</tbody>
	         </table>
	      </div>
	  </div>
  </div>
</div>
