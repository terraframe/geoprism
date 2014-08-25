<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Legend"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.DashboardLegend.form.id" name="com.runwaysdk.geodashboard.gis.persist.DashboardLegend.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLegend.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.persist.DashboardLegendController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLegend.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.persist.DashboardLegendController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLegend.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.DashboardLegendController.cancel.mojo" />
  </mjl:form>
</dl>
