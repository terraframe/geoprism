<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Has Style"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.HasStyle.form.id" name="com.runwaysdk.geodashboard.gis.HasStyle.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <dt>
        <label>
          Layer
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="com.runwaysdk.geodashboard.gis.DashboardLayer.form.view.link" action="com.runwaysdk.geodashboard.gis.DashboardLayerController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
      <dt>
        <label>
          Style
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="com.runwaysdk.geodashboard.gis.DashboardStyle.form.view.link" action="com.runwaysdk.geodashboard.gis.DashboardStyleController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.HasStyle.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.HasStyleController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.HasStyle.viewAll.link" action="com.runwaysdk.geodashboard.gis.HasStyleController.viewAll.mojo">
  View All
</mjl:commandLink>
