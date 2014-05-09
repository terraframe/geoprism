<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Thematic Attribute"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardThematicStyle.form.id" name="com.runwaysdk.geodashboard.gis.DashboardThematicStyle.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardThematicStyle.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.DashboardThematicStyleController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardThematicStyle.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.DashboardThematicStyleController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardThematicStyle.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.DashboardThematicStyleController.cancel.mojo" />
  </mjl:form>
</dl>
