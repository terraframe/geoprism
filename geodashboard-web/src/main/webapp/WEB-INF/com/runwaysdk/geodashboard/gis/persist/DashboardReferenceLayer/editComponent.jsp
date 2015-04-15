<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Layer"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.form.id" name="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController.cancel.mojo" />
  </mjl:form>
</dl>
