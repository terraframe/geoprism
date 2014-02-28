<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing GeoEntity"/>
  <mjl:form classes="submit-form" id="com.runwaysdk.system.gis.geo.GeoEntity.form.id" name="com.runwaysdk.system.gis.geo.GeoEntity.form.name" method="POST">
    <%@include file="form.jsp" %>
    <!--
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntity.form.update.button" value="Update" action="com.runwaysdk.system.gis.geo.GeoEntityController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntity.form.delete.button" value="Delete" action="com.runwaysdk.system.gis.geo.GeoEntityController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntity.form.cancel.button" value="Cancel" action="com.runwaysdk.system.gis.geo.GeoEntityController.cancel.mojo" />
    -->
  </mjl:form>
