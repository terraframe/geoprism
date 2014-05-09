<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Thematic Attribute"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.DashboardThematicStyle.form.id" name="com.runwaysdk.geodashboard.gis.DashboardThematicStyle.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="mdAttribute">
        ${item.mdAttribute.keyName}
      </mjl:dt>
      <mjl:dt attribute="styleCondition">
        ${item.styleCondition.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.DashboardThematicStyle.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.DashboardThematicStyleController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.DashboardThematicStyle.viewAll.link" action="com.runwaysdk.geodashboard.gis.DashboardThematicStyleController.viewAll.mojo">
  View All
</mjl:commandLink>
