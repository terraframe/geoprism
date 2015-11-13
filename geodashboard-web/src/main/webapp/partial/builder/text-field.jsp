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

<div class="field-row clearfix">
  <label class="com-runwaysdk-ui-factory-runway-Label com-runwaysdk-ui-factory-runway-Widget">{{field.label}}</label>
  <input ng-model="model[field.name]" class="com-runwaysdk-ui-factory-runway-TextInput com-runwaysdk-ui-factory-runway-FormInput com-runwaysdk-ui-factory-runway-Widget" type="text">
  <div class="error-message">{{field.message}}</div>
</div>
