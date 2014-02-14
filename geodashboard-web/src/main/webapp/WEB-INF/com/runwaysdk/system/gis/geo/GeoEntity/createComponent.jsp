<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new GeoEntity"/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.GeoEntity.form.id" name="com.runwaysdk.system.gis.geo.GeoEntity.form.name" method="POST">
    <%@include file="form.jsp" %>
    <!--
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntity.form.create.button" value="Create" action="com.runwaysdk.system.gis.geo.GeoEntityController.create.mojo" />
    -->
  </mjl:form>
</dl>
