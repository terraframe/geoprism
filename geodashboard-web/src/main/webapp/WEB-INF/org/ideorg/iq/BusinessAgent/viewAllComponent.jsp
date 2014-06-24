<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Business Agent"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.BusinessAgentController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="dateOfBirth">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="education">
      <mjl:row>
        ${item.education.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="firstName">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="gender">
      <mjl:row>
        ${item.gender.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="lastName">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="phoneNumber">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="role">
      <mjl:row>
        ${item.role.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="startDate">
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="org.ideorg.iq.BusinessAgentController.view.mojo">
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
<mjl:commandLink name="BusinessAgentController.newInstance" action="org.ideorg.iq.BusinessAgentController.newInstance.mojo">
  Create a new Business Agent
</mjl:commandLink>
