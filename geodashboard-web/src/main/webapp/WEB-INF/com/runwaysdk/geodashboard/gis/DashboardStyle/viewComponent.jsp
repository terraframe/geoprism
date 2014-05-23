<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Style"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardStyle.form.id" name="com.runwaysdk.geodashboard.gis.DashboardStyle.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardStyle.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.DashboardStyleController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.DashboardStyle.viewAll.link" action="com.runwaysdk.geodashboard.gis.DashboardStyleController.viewAll.mojo">
  View All
</mjl:commandLink>
