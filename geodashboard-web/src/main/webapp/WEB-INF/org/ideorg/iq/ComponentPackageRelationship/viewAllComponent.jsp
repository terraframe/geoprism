<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Component Package Relationship"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.ComponentPackageRelationshipController.viewPage.mojo" />
  <mjl:columns>
    <mjl:freeColumn>
      <mjl:header>
        Component
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="parent.link" action="org.ideorg.iq.ComponentController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
    <mjl:freeColumn>
      <mjl:header>
        Package
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="child.link" action="org.ideorg.iq.IDEPackageController.view.mojo">
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
        <mjl:commandLink name="view.link" action="org.ideorg.iq.ComponentPackageRelationshipController.view.mojo">
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
<mjl:commandLink name="ComponentPackageRelationshipController.newRelationship" action="org.ideorg.iq.ComponentPackageRelationshipController.newRelationship.mojo">
  Create a new Component Package Relationship
</mjl:commandLink>
