<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing "/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.GeoEntityEntityName.form.id" name="com.runwaysdk.system.gis.geo.GeoEntityEntityName.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntityEntityName.form.update.button" value="Update" action="com.runwaysdk.system.gis.geo.GeoEntityEntityNameController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntityEntityName.form.delete.button" value="Delete" action="com.runwaysdk.system.gis.geo.GeoEntityEntityNameController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntityEntityName.form.cancel.button" value="Cancel" action="com.runwaysdk.system.gis.geo.GeoEntityEntityNameController.cancel.mojo" />
  </mjl:form>
</dl>
