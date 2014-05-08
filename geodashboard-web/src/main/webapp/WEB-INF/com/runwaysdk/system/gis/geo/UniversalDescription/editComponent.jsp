<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Description"/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.UniversalDescription.form.id" name="com.runwaysdk.system.gis.geo.UniversalDescription.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.gis.geo.UniversalDescription.form.update.button" value="Update" action="com.runwaysdk.system.gis.geo.UniversalDescriptionController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.UniversalDescription.form.delete.button" value="Delete" action="com.runwaysdk.system.gis.geo.UniversalDescriptionController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.UniversalDescription.form.cancel.button" value="Cancel" action="com.runwaysdk.system.gis.geo.UniversalDescriptionController.cancel.mojo" />
  </mjl:form>
</dl>
