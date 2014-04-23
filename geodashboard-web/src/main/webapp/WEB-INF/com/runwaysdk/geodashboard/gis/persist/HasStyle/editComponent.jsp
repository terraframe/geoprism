<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Has Style"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.HasStyle.form.id" name="com.runwaysdk.geodashboard.gis.persist.HasStyle.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.HasStyle.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.persist.HasStyleController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.HasStyle.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.persist.HasStyleController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.HasStyle.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.HasStyleController.cancel.mojo" />
  </mjl:form>
</dl>
