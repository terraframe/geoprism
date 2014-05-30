<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Attribute Wrapper"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="com.runwaysdk.geodashboard.AttributeWrapperController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="wrappedMdAttribute">
      <mjl:row>
        ${item.wrappedMdAttribute.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="com.runwaysdk.geodashboard.AttributeWrapperController.view.mojo">
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
<mjl:commandLink name="AttributeWrapperController.newInstance" action="com.runwaysdk.geodashboard.AttributeWrapperController.newInstance.mojo">
  Create a new Attribute Wrapper
</mjl:commandLink>
