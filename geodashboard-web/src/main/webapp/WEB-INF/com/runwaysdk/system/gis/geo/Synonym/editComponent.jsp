<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Synonym"/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.Synonym.form.id" name="com.runwaysdk.system.gis.geo.Synonym.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.gis.geo.Synonym.form.update.button" value="Update" action="com.runwaysdk.system.gis.geo.SynonymController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.Synonym.form.delete.button" value="Delete" action="com.runwaysdk.system.gis.geo.SynonymController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.Synonym.form.cancel.button" value="Cancel" action="com.runwaysdk.system.gis.geo.SynonymController.cancel.mojo" />
  </mjl:form>
</dl>
