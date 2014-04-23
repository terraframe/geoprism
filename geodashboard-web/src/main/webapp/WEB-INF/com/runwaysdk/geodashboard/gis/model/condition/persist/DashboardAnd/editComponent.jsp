<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing And"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAnd.form.id" name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAnd.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAnd.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAndController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAnd.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAndController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAnd.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAndController.cancel.mojo" />
  </mjl:form>
</dl>
