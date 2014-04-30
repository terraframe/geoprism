<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Display Label"/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.SynonymDisplayLabel.form.id" name="com.runwaysdk.system.gis.geo.SynonymDisplayLabel.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.gis.geo.SynonymDisplayLabel.form.update.button" value="Update" action="com.runwaysdk.system.gis.geo.SynonymDisplayLabelController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.SynonymDisplayLabel.form.delete.button" value="Delete" action="com.runwaysdk.system.gis.geo.SynonymDisplayLabelController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.SynonymDisplayLabel.form.cancel.button" value="Cancel" action="com.runwaysdk.system.gis.geo.SynonymDisplayLabelController.cancel.mojo" />
  </mjl:form>
</dl>
