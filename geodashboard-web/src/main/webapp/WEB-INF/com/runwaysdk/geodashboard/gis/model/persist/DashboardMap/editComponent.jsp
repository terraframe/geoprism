<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Map"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.model.persist.DashboardMap.form.id" name="com.runwaysdk.geodashboard.gis.model.persist.DashboardMap.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.persist.DashboardMap.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.model.persist.DashboardMapController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.persist.DashboardMap.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.model.persist.DashboardMapController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.persist.DashboardMap.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.model.persist.DashboardMapController.cancel.mojo" />
  </mjl:form>
</dl>
