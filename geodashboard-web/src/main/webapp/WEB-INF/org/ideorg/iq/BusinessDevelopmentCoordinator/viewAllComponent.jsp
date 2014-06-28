<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Business Development Coordinator"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.BusinessDevelopmentCoordinatorController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="active">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="agriculture">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="name">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="phoneNumber">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="workingArea">
      <mjl:row>
        ${item.workingArea.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="workingStation">
      <mjl:row>
        ${item.workingStation.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="org.ideorg.iq.BusinessDevelopmentCoordinatorController.view.mojo">
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
<mjl:commandLink name="BusinessDevelopmentCoordinatorController.newInstance" action="org.ideorg.iq.BusinessDevelopmentCoordinatorController.newInstance.mojo">
  Create a new Business Development Coordinator
</mjl:commandLink>
