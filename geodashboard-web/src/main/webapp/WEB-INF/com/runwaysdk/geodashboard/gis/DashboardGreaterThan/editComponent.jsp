<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Greater Than"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardGreaterThan.form.id" name="com.runwaysdk.geodashboard.gis.DashboardGreaterThan.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardGreaterThan.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.DashboardGreaterThanController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardGreaterThan.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.DashboardGreaterThanController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardGreaterThan.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.DashboardGreaterThanController.cancel.mojo" />
  </mjl:form>
</dl>
