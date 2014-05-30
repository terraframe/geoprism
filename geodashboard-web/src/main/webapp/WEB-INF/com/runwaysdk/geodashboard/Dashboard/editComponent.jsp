<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Dashboard"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.Dashboard.form.id" name="com.runwaysdk.geodashboard.Dashboard.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.Dashboard.form.update.button" value="Update" action="com.runwaysdk.geodashboard.DashboardController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.Dashboard.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.DashboardController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.Dashboard.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.DashboardController.cancel.mojo" />
  </mjl:form>
</dl>
