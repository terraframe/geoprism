<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing "/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.DashboardDisplayLabel.form.id" name="com.runwaysdk.geodashboard.DashboardDisplayLabel.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.DashboardDisplayLabel.form.update.button" value="Update" action="com.runwaysdk.geodashboard.DashboardDisplayLabelController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.DashboardDisplayLabel.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.DashboardDisplayLabelController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.DashboardDisplayLabel.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.DashboardDisplayLabelController.cancel.mojo" />
  </mjl:form>
</dl>
