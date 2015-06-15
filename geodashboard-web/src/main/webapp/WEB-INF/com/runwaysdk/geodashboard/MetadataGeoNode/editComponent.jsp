<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Wrapper Geo node"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.MetadataGeoNode.form.id" name="com.runwaysdk.geodashboard.MetadataGeoNode.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.MetadataGeoNode.form.update.button" value="Update" action="com.runwaysdk.geodashboard.MetadataGeoNodeController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.MetadataGeoNode.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.MetadataGeoNodeController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.MetadataGeoNode.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.MetadataGeoNodeController.cancel.mojo" />
  </mjl:form>
</dl>
