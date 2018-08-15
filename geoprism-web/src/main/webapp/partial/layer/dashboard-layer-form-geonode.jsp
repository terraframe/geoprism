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
<%@ taglib uri="../../WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
       
       
<div id="geonode-holder" class="row-holder">
  <div class="label-holder style03">
    <strong><gdb:localize key="DashboardThematicLayer.form.defineGeoNode"/></strong>
  </div>
  <div class="holder add">
    <div class="box">
      <label for="geonode-select"><gdb:localize key="DashboardThematicLayer.form.geoNode"/></label>
      <styled-select options="dynamicDataModel.geoNodes" model="layerModel.geoNode" value="oid" label="displayLabel"></styled-select>
    </div>
  </div>
</div>
