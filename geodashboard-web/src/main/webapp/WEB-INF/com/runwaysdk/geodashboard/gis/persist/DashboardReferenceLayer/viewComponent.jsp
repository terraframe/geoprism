<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Layer"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.form.id" name="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="BBoxIncluded">
        ${item.BBoxIncluded ? item.BBoxIncludedMd.positiveDisplayLabel : item.BBoxIncludedMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="activeByDefault">
        ${item.activeByDefault ? item.activeByDefaultMd.positiveDisplayLabel : item.activeByDefaultMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="dashboardLegend">
        <mjl:struct param="dashboardLegend">
          <mjl:dt attribute="groupedInLegend">
            ${item.dashboardLegend.groupedInLegend ? item.dashboardLegend.groupedInLegendMd.positiveDisplayLabel : item.dashboardLegend.groupedInLegendMd.negativeDisplayLabel}
          </mjl:dt>
          <mjl:dt attribute="legendXPosition">
            ${item.dashboardLegend.legendXPosition}
          </mjl:dt>
          <mjl:dt attribute="legendYPosition">
            ${item.dashboardLegend.legendYPosition}
          </mjl:dt>
        </mjl:struct>
      </mjl:dt>
      <mjl:dt attribute="dashboardMap">
        ${item.dashboardMap.keyName}
      </mjl:dt>
      <mjl:dt attribute="displayInLegend">
        ${item.displayInLegend ? item.displayInLegendMd.positiveDisplayLabel : item.displayInLegendMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="geoEntity">
        ${item.geoEntity.keyName}
      </mjl:dt>
      <mjl:dt attribute="lastPublishDate">
        ${item.lastPublishDate}
      </mjl:dt>
      <mjl:dt attribute="layerEnabled">
        ${item.layerEnabled ? item.layerEnabledMd.positiveDisplayLabel : item.layerEnabledMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="layerType">
        <ul>
          <c:forEach items="${item.layerTypeEnumNames}" var="enumName">
            <li>
              ${item.layerTypeMd.enumItems[enumName]}
            </li>
          </c:forEach>
        </ul>
      </mjl:dt>
      <mjl:dt attribute="name">
        ${item.name}
      </mjl:dt>
      <mjl:dt attribute="universal">
        ${item.universal.keyName}
      </mjl:dt>
      <mjl:dt attribute="viewName">
        ${item.viewName}
      </mjl:dt>
      <mjl:dt attribute="virtual">
        ${item.virtual ? item.virtualMd.positiveDisplayLabel : item.virtualMd.negativeDisplayLabel}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
  <dt>
    <label>
      Parent Relationships
    </label>
  </dt>
  <dd>
    <ul>
      <li>
        <mjl:commandLink name="com.runwaysdk.geodashboard.gis.persist.HasStyle.parentQuery.link" action="com.runwaysdk.geodashboard.gis.persist.HasStyleController.parentQuery.mojo">
          
          <mjl:property name="parentId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
  <dt>
    <label>
      Child Relationships
    </label>
  </dt>
  <dd>
    <ul>
      <li>
        <mjl:commandLink name="com.runwaysdk.geodashboard.gis.persist.HasLayer.childQuery.link" action="com.runwaysdk.geodashboard.gis.persist.HasLayerController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.viewAll.link" action="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController.viewAll.mojo">
  View All
</mjl:commandLink>
