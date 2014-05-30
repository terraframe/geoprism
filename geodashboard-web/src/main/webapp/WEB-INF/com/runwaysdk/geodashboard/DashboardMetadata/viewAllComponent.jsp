<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all "/>
<mjl:table var="item" query="${query}">
  <mjl:context action="com.runwaysdk.geodashboard.DashboardMetadataController.viewPage.mojo" />
  <mjl:columns>
    <mjl:freeColumn>
      <mjl:header>
        Dashboard
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="parent.link" action="com.runwaysdk.geodashboard.DashboardController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
    <mjl:freeColumn>
      <mjl:header>
        Dashboard Wrapper
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="child.link" action="com.runwaysdk.geodashboard.MetadataWrapperController.view.mojo">
          ${item.child.keyName}
          <mjl:property name="id" value="${item.childId}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="com.runwaysdk.geodashboard.DashboardMetadataController.view.mojo">
          View
          <mjl:property name="id" value="${item.id}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
  </mjl:columns>
  <mjl:pagination>
    <mjl:page />
  </mjl:pagination>
</mjl:table>
<br />
<mjl:commandLink name="DashboardMetadataController.newRelationship" action="com.runwaysdk.geodashboard.DashboardMetadataController.newRelationship.mojo">
  Create a new 
</mjl:commandLink>
