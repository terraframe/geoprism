<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing "/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.GeoEntityDisplayLabel.form.id" name="com.runwaysdk.system.gis.geo.GeoEntityDisplayLabel.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntityDisplayLabel.form.update.button" value="Update" action="com.runwaysdk.system.gis.geo.GeoEntityDisplayLabelController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntityDisplayLabel.form.delete.button" value="Delete" action="com.runwaysdk.system.gis.geo.GeoEntityDisplayLabelController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntityDisplayLabel.form.cancel.button" value="Cancel" action="com.runwaysdk.system.gis.geo.GeoEntityDisplayLabelController.cancel.mojo" />
  </mjl:form>
</dl>
