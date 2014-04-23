<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Exact"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardExact.form.id" name="com.runwaysdk.geodashboard.gis.DashboardExact.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardExact.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.DashboardExactController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardExact.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.DashboardExactController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardExact.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.DashboardExactController.cancel.mojo" />
  </mjl:form>
</dl>
