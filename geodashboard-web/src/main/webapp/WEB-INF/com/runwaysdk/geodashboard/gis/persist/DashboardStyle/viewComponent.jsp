<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Style"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.DashboardStyle.form.id" name="com.runwaysdk.geodashboard.gis.persist.DashboardStyle.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="enableLabel">
        ${item.enableLabel ? item.enableLabelMd.positiveDisplayLabel : item.enableLabelMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="enableValue">
        ${item.enableValue ? item.enableValueMd.positiveDisplayLabel : item.enableValueMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="labelColor">
        ${item.labelColor}
      </mjl:dt>
      <mjl:dt attribute="labelFont">
        ${item.labelFont}
      </mjl:dt>
      <mjl:dt attribute="labelHalo">
        ${item.labelHalo}
      </mjl:dt>
      <mjl:dt attribute="labelSize">
        ${item.labelSize}
      </mjl:dt>
      <mjl:dt attribute="labelWidth">
        ${item.labelWidth}
      </mjl:dt>
      <mjl:dt attribute="name">
        ${item.name}
      </mjl:dt>
      <mjl:dt attribute="pointFill">
        ${item.pointFill}
      </mjl:dt>
      <mjl:dt attribute="pointOpacity">
        ${item.pointOpacity}
      </mjl:dt>
      <mjl:dt attribute="pointRotation">
        ${item.pointRotation}
      </mjl:dt>
      <mjl:dt attribute="pointSize">
        ${item.pointSize}
      </mjl:dt>
      <mjl:dt attribute="pointStroke">
        ${item.pointStroke}
      </mjl:dt>
      <mjl:dt attribute="pointStrokeWidth">
        ${item.pointStrokeWidth}
      </mjl:dt>
      <mjl:dt attribute="polygonFill">
        ${item.polygonFill}
      </mjl:dt>
      <mjl:dt attribute="polygonStroke">
        ${item.polygonStroke}
      </mjl:dt>
      <mjl:dt attribute="polygonStrokeWidth">
        ${item.polygonStrokeWidth}
      </mjl:dt>
      <mjl:dt attribute="polygoneFillOpacity">
        ${item.polygoneFillOpacity}
      </mjl:dt>
      <mjl:dt attribute="polygoneStrokeOpacity">
        ${item.polygoneStrokeOpacity}
      </mjl:dt>
      <mjl:dt attribute="valueColor">
        ${item.valueColor}
      </mjl:dt>
      <mjl:dt attribute="valueFont">
        ${item.valueFont}
      </mjl:dt>
      <mjl:dt attribute="valueHalo">
        ${item.valueHalo}
      </mjl:dt>
      <mjl:dt attribute="valueSize">
        ${item.valueSize}
      </mjl:dt>
      <mjl:dt attribute="valueWidth">
        ${item.valueWidth}
      </mjl:dt>
      <mjl:dt attribute="wellKnownName">
        ${item.wellKnownName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardStyle.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.persist.DashboardStyleController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
  <dt>
    <label>
      Child Relationships
    </label>
  </dt>
  <dd>
    <ul>
      <li>
        <mjl:commandLink name="com.runwaysdk.geodashboard.gis.persist.HasStyle.childQuery.link" action="com.runwaysdk.geodashboard.gis.persist.HasStyleController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.persist.DashboardStyle.viewAll.link" action="com.runwaysdk.geodashboard.gis.persist.DashboardStyleController.viewAll.mojo">
  View All
</mjl:commandLink>
