<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Equals"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqual.form.id" name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqual.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqual.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqualController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqual.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqualController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqual.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqualController.cancel.mojo" />
  </mjl:form>
</dl>
