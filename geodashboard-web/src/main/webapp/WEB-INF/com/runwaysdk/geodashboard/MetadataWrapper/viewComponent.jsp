<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Dashboard Wrapper"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.MetadataWrapper.form.id" name="com.runwaysdk.geodashboard.MetadataWrapper.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="mdClass">
        ${item.mdClass.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.MetadataWrapper.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.MetadataWrapperController.edit.mojo" />
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
        <mjl:commandLink name="com.runwaysdk.geodashboard.DashboardMetadata.childQuery.link" action="com.runwaysdk.geodashboard.DashboardMetadataController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.MetadataWrapper.viewAll.link" action="com.runwaysdk.geodashboard.MetadataWrapperController.viewAll.mojo">
  View All
</mjl:commandLink>
