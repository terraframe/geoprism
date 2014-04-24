<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Primitive"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardPrimitive.form.id" name="com.runwaysdk.geodashboard.gis.DashboardPrimitive.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardPrimitive.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.DashboardPrimitiveController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardPrimitive.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.DashboardPrimitiveController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardPrimitive.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.DashboardPrimitiveController.cancel.mojo" />
  </mjl:form>
</dl>
