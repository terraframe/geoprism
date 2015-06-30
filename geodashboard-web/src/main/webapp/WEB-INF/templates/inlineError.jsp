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
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="alert alertbox">
<p>

<c:if test="${errorMessage != null && errorMessage != ''}">
  <str:replace replace="\\\\n" with="<br />NL" newlineToken="NL"><str:escape>${errorMessage}</str:escape></str:replace> 
</c:if>

<c:if test="${errorMessageArray != null}">
  <str:escape><str:join separator="<br />" items="${errorMessageArray}"></str:join></str:escape>
</c:if>

<c:if test="${(errorMessage != null && errorMessage != '') || errorMessageArray != null}">
</c:if>

</p>
</div>
