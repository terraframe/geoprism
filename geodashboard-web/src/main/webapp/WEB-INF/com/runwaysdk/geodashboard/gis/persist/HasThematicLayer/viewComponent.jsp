<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Has Thematic Layer"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.HasThematicLayer.form.id" name="com.runwaysdk.geodashboard.gis.persist.HasThematicLayer.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <dt>
        <label>
          Map
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="com.runwaysdk.geodashboard.gis.persist.DashboardMap.form.view.link" action="com.runwaysdk.geodashboard.gis.persist.DashboardMapController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
      <dt>
        <label>
          Layer
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.form.view.link" action="com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
      <mjl:dt attribute="layerIndex">
        ${item.layerIndex}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.HasThematicLayer.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.persist.HasThematicLayerController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.persist.HasThematicLayer.viewAll.link" action="com.runwaysdk.geodashboard.gis.persist.HasThematicLayerController.viewAll.mojo">
  View All
</mjl:commandLink>
