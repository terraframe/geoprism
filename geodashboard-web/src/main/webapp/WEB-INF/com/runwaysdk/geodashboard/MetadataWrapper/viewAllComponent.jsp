<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Dashboard Wrapper"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="com.runwaysdk.geodashboard.MetadataWrapperController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="mdClass">
      <mjl:row>
        ${item.mdClass.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="com.runwaysdk.geodashboard.MetadataWrapperController.view.mojo">
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
<mjl:commandLink name="MetadataWrapperController.newInstance" action="com.runwaysdk.geodashboard.MetadataWrapperController.newInstance.mojo">
  Create a new Dashboard Wrapper
</mjl:commandLink>
