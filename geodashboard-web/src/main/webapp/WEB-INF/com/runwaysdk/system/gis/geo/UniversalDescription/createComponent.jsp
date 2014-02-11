<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Description"/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.UniversalDescription.form.id" name="com.runwaysdk.system.gis.geo.UniversalDescription.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.gis.geo.UniversalDescription.form.create.button" value="Create" action="com.runwaysdk.system.gis.geo.UniversalDescriptionController.create.mojo" />
  </mjl:form>
</dl>
