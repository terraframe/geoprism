<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Equals"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardEqual.form.id" name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardEqual.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardEqual.form.create.button" value="Create" action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardEqualController.create.mojo" />
  </mjl:form>
</dl>
