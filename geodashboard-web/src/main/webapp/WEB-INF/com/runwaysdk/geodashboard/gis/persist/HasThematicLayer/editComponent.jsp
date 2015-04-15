<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Has Thematic Layer"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.HasThematicLayer.form.id" name="com.runwaysdk.geodashboard.gis.persist.HasThematicLayer.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.HasThematicLayer.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.persist.HasThematicLayerController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.HasThematicLayer.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.persist.HasThematicLayerController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.HasThematicLayer.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.HasThematicLayerController.cancel.mojo" />
  </mjl:form>
</dl>
