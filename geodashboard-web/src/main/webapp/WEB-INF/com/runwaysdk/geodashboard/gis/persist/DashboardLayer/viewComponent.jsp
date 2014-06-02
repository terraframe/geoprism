<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Layer"/>

  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.DashboardLayer.form.id" 
  name="com.runwaysdk.geodashboard.gis.persist.DashboardLayer.form.name" method="POST" classes="modal-form">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLayer.form.create.button" value="Map It" action="com.runwaysdk.geodashboard.gis.persist.DashboardLayerController.create.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLayer.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.DashboardLayerController.cancel.mojo" />
  </mjl:form>

