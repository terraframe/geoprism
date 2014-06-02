<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Dashboard Attributes"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.DashboardAttributes.form.id" name="com.runwaysdk.geodashboard.DashboardAttributes.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <dt>
        <label>
          Dashboard Wrapper
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="com.runwaysdk.geodashboard.MetadataWrapper.form.view.link" action="com.runwaysdk.geodashboard.MetadataWrapperController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
      <dt>
        <label>
          Attribute Wrapper
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="com.runwaysdk.geodashboard.AttributeWrapper.form.view.link" action="com.runwaysdk.geodashboard.AttributeWrapperController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.DashboardAttributes.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.DashboardAttributesController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.DashboardAttributes.viewAll.link" action="com.runwaysdk.geodashboard.DashboardAttributesController.viewAll.mojo">
  View All
</mjl:commandLink>
