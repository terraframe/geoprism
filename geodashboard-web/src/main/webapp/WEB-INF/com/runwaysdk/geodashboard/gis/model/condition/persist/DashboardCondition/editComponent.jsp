<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Condition"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardCondition.form.id" name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardCondition.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardCondition.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardConditionController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardCondition.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardConditionController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardCondition.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardConditionController.cancel.mojo" />
  </mjl:form>
</dl>
