<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Thematic Attribute"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle.form.id" name="com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleController.cancel.mojo" />
  </mjl:form>
</dl>
