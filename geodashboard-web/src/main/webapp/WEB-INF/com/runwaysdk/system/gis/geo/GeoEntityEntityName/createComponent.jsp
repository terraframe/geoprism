<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new "/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.GeoEntityEntityName.form.id" name="com.runwaysdk.system.gis.geo.GeoEntityEntityName.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntityEntityName.form.create.button" value="Create" action="com.runwaysdk.system.gis.geo.GeoEntityEntityNameController.create.mojo" />
  </mjl:form>
</dl>
