<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Greater Than Or Equal"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqual.form.id" name="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqual.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqual.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqualController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqual.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqualController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqual.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqualController.cancel.mojo" />
  </mjl:form>
</dl>
