<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Greater Than Or Equal"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqual.form.id" name="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqual.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="attributeValue">
        ${item.attributeValue}
      </mjl:dt>
      <mjl:dt attribute="styleReference">
        ${item.styleReference.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqual.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqualController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqual.viewAll.link" action="com.runwaysdk.geodashboard.gis.DashboardGreaterThanOrEqualController.viewAll.mojo">
  View All
</mjl:commandLink>
