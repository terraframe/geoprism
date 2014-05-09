<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Or"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardOr.form.id" name="com.runwaysdk.geodashboard.gis.DashboardOr.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardOr.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.DashboardOrController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardOr.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.DashboardOrController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardOr.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.DashboardOrController.cancel.mojo" />
  </mjl:form>
</dl>
