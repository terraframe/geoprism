<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Sales Package Relationship"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.SalesPackageRelationshipController.viewPage.mojo" />
  <mjl:columns>
    <mjl:freeColumn>
      <mjl:header>
        Sales Package
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="parent.link" action="org.ideorg.iq.SalesPackageController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
    <mjl:freeColumn>
      <mjl:header>
        Sales Transaction
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="child.link" action="org.ideorg.iq.SalesTransactionController.view.mojo">
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
        <mjl:commandLink name="view.link" action="org.ideorg.iq.SalesPackageRelationshipController.view.mojo">
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
<mjl:commandLink name="SalesPackageRelationshipController.newRelationship" action="org.ideorg.iq.SalesPackageRelationshipController.newRelationship.mojo">
  Create a new Sales Package Relationship
</mjl:commandLink>
