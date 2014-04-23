<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Primitive"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.condition.DashboardPrimitive.form.id" name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardPrimitive.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardPrimitive.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardPrimitiveController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardPrimitive.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardPrimitiveController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardPrimitive.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardPrimitiveController.cancel.mojo" />
  </mjl:form>
</dl>
