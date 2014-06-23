<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Enterprise Training"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.EnterpriseTrainingController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="enterprise">
      <mjl:row>
        ${item.enterprise.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="trainingDate">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="trainingReceived">
      <mjl:row>
        ${item.trainingReceived.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="org.ideorg.iq.EnterpriseTrainingController.view.mojo">
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
<mjl:commandLink name="EnterpriseTrainingController.newInstance" action="org.ideorg.iq.EnterpriseTrainingController.newInstance.mojo">
  Create a new Enterprise Training
</mjl:commandLink>
