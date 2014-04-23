<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Layer"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.model.persist.DashboardLayer.form.id" name="com.runwaysdk.geodashboard.gis.model.persist.DashboardLayer.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="name">
        ${item.name}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.persist.DashboardLayer.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.model.persist.DashboardLayerController.edit.mojo" />
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
        <mjl:commandLink name="com.runwaysdk.geodashboard.gis.model.persist.HasStyle.parentQuery.link" action="com.runwaysdk.geodashboard.gis.model.persist.HasStyleController.parentQuery.mojo">
          
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
        <mjl:commandLink name="com.runwaysdk.geodashboard.gis.model.persist.HasLayer.childQuery.link" action="com.runwaysdk.geodashboard.gis.model.persist.HasLayerController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.model.persist.DashboardLayer.viewAll.link" action="com.runwaysdk.geodashboard.gis.model.persist.DashboardLayerController.viewAll.mojo">
  View All
</mjl:commandLink>
