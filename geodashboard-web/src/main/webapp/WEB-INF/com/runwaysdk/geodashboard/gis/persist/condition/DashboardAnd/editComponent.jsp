<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing And"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.condition.DashboardAnd.form.id" name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardAnd.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardAnd.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardAndController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardAnd.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardAndController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardAnd.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardAndController.cancel.mojo" />
  </mjl:form>
</dl>
