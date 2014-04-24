<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Layer Type"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.LayerType.form.id" name="com.runwaysdk.geodashboard.gis.persist.LayerType.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.LayerType.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.persist.LayerTypeController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.LayerType.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.persist.LayerTypeController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.LayerType.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.LayerTypeController.cancel.mojo" />
  </mjl:form>
</dl>
