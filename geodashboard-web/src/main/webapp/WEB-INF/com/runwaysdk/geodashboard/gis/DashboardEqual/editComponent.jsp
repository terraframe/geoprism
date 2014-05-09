<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Equals"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardEqual.form.id" name="com.runwaysdk.geodashboard.gis.DashboardEqual.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardEqual.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.DashboardEqualController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardEqual.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.DashboardEqualController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardEqual.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.DashboardEqualController.cancel.mojo" />
  </mjl:form>
</dl>
