<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Condition"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition.form.id" name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionController.cancel.mojo" />
  </mjl:form>
</dl>
