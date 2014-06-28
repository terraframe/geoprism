<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Consults For"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.WorksForController.viewPage.mojo" />
  <mjl:columns>
    <mjl:freeColumn>
      <mjl:header>
        Enterprise
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="parent.link" action="org.ideorg.iq.EnterpriseController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
    <mjl:freeColumn>
      <mjl:header>
        Business Agent
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="child.link" action="org.ideorg.iq.BusinessAgentController.view.mojo">
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
        <mjl:commandLink name="view.link" action="org.ideorg.iq.WorksForController.view.mojo">
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
<mjl:commandLink name="WorksForController.newRelationship" action="org.ideorg.iq.WorksForController.newRelationship.mojo">
  Create a new Consults For
</mjl:commandLink>
