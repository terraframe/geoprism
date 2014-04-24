<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Layer"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardLayer.form.id" name="com.runwaysdk.geodashboard.gis.DashboardLayer.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardLayer.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.DashboardLayerController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardLayer.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.DashboardLayerController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardLayer.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.DashboardLayerController.cancel.mojo" />
  </mjl:form>
</dl>
