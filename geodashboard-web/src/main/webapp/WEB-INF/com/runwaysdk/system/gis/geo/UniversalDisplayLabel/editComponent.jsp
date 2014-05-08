<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Display label"/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.UniversalDisplayLabel.form.id" name="com.runwaysdk.system.gis.geo.UniversalDisplayLabel.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.gis.geo.UniversalDisplayLabel.form.update.button" value="Update" action="com.runwaysdk.system.gis.geo.UniversalDisplayLabelController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.UniversalDisplayLabel.form.delete.button" value="Delete" action="com.runwaysdk.system.gis.geo.UniversalDisplayLabelController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.UniversalDisplayLabel.form.cancel.button" value="Cancel" action="com.runwaysdk.system.gis.geo.UniversalDisplayLabelController.cancel.mojo" />
  </mjl:form>
</dl>
