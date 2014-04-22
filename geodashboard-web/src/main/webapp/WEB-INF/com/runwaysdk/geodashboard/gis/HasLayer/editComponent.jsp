<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Has Layer"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.HasLayer.form.id" name="com.runwaysdk.geodashboard.gis.HasLayer.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.HasLayer.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.HasLayerController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.HasLayer.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.HasLayerController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.HasLayer.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.HasLayerController.cancel.mojo" />
  </mjl:form>
</dl>
