<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Cambodia LB Enterprise"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.CambodiaLBEnterpriseController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="salesCumulative">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="activeSalesAgents">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="businessDevelopmentCoordinator">
      <mjl:row>
        ${item.businessDevelopmentCoordinator.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="capitalInvestmentsTotalValue">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="contactNumber">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="coordinatesMultiPolygon">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="coordinatesPoint">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="email">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="enterpriseCreateDate">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="enterpriseCreatedBy">
      <mjl:row>
        ${item.enterpriseCreatedBy.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="gender">
      <mjl:row>
        ${item.gender.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="geoLocation">
      <mjl:row>
        ${item.geoLocation.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="initiateDate">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="lastModifiedBy">
      <mjl:row>
        ${item.lastModifiedBy.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="lastModifiedDate">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="manager">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="mostRecentProfileDate">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="ownerName">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="permissionToShareContactDetails">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="primaryBusiness">
      <mjl:row>
        ${item.primaryBusiness.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="sellingStatus">
      <mjl:row>
        ${item.sellingStatus.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="signedMOU">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="yearOfBirth">
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="org.ideorg.iq.CambodiaLBEnterpriseController.view.mojo">
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
<mjl:commandLink name="CambodiaLBEnterpriseController.newInstance" action="org.ideorg.iq.CambodiaLBEnterpriseController.newInstance.mojo">
  Create a new Cambodia LB Enterprise
</mjl:commandLink>
