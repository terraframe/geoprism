<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Dashboard"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.Dashboard.form.id" name="com.runwaysdk.geodashboard.Dashboard.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="displayLabel">
        ${item.displayLabel}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.Dashboard.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.DashboardController.edit.mojo" />
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
        <mjl:commandLink name="com.runwaysdk.geodashboard.DashboardMetadata.parentQuery.link" action="com.runwaysdk.geodashboard.DashboardMetadataController.parentQuery.mojo">
          
          <mjl:property name="parentId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.Dashboard.viewAll.link" action="com.runwaysdk.geodashboard.DashboardController.viewAll.mojo">
  View All
</mjl:commandLink>
