<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Greater Than Or Equal"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqual.form.id" name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqual.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqual.form.create.button" value="Create" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqualController.create.mojo" />
  </mjl:form>
</dl>
