<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Composite"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardComposite.form.id" name="com.runwaysdk.geodashboard.gis.DashboardComposite.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardComposite.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.DashboardCompositeController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardComposite.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.DashboardCompositeController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardComposite.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.DashboardCompositeController.cancel.mojo" />
  </mjl:form>
</dl>
