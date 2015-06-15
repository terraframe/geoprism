<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Wrapper Geo node"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.MetadataGeoNode.form.id" name="com.runwaysdk.geodashboard.MetadataGeoNode.form.name" method="POST">
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
          Geo node
        </label>
      </dt>
      <dd>
        ${item.parent.keyName}
      </dd>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.MetadataGeoNode.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.MetadataGeoNodeController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.MetadataGeoNode.viewAll.link" action="com.runwaysdk.geodashboard.MetadataGeoNodeController.viewAll.mojo">
  View All
</mjl:commandLink>
