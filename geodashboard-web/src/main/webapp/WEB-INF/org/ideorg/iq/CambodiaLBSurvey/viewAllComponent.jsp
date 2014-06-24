<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Cambodia LB Survey"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.CambodiaLBSurveyController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="lboServiceInstallRings">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="lboServiceOther">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="lboServiceOtherPrice">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="lboServiceOtherSpecify">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="lboServicePitDiggingPrice">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="servicePitDigging">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="bank">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="bankLoanAmount">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="capitalInvestmentsTotalValue">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="collectionCoordinator">
      <mjl:row>
        ${item.collectionCoordinator.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="collectionDate">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="comission">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="currentCasualLabor">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="currentFullTimeLabor">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="currentPartTimeLabor">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="microFinanceInstitution">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="microFinanceLoanAmount">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="sellingStatus">
      <mjl:row>
        ${item.sellingStatus.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="org.ideorg.iq.CambodiaLBSurveyController.view.mojo">
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
<mjl:commandLink name="CambodiaLBSurveyController.newInstance" action="org.ideorg.iq.CambodiaLBSurveyController.newInstance.mojo">
  Create a new Cambodia LB Survey
</mjl:commandLink>
