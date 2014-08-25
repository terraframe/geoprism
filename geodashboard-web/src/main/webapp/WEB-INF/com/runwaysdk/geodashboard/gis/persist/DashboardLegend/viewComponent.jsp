<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Legend"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.DashboardLegend.form.id" name="com.runwaysdk.geodashboard.gis.persist.DashboardLegend.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="legendXPosition">
        ${item.legendXPosition}
      </mjl:dt>
      <mjl:dt attribute="legendYPosition">
        ${item.legendYPosition}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLegend.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.persist.DashboardLegendController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.persist.DashboardLegend.viewAll.link" action="com.runwaysdk.geodashboard.gis.persist.DashboardLegendController.viewAll.mojo">
  View All
</mjl:commandLink>
