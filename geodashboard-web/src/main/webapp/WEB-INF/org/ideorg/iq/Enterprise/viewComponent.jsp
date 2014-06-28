<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Enterprise"/>
<dl>
  <mjl:form id="org.ideorg.iq.Enterprise.form.id" name="org.ideorg.iq.Enterprise.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="activeSalesAgents">
        ${item.activeSalesAgents}
      </mjl:dt>
      <mjl:dt attribute="businessDevelopmentCoordinator">
        ${item.businessDevelopmentCoordinator.keyName}
      </mjl:dt>
      <mjl:dt attribute="capitalInvestmentsTotalValue">
        ${item.capitalInvestmentsTotalValue}
      </mjl:dt>
      <mjl:dt attribute="contactNumber">
        ${item.contactNumber}
      </mjl:dt>
      <mjl:dt attribute="coordinatesMultiPolygon">
        ${item.coordinatesMultiPolygon}
      </mjl:dt>
      <mjl:dt attribute="coordinatesPoint">
        ${item.coordinatesPoint}
      </mjl:dt>
      <mjl:dt attribute="email">
        ${item.email}
      </mjl:dt>
      <mjl:dt attribute="enterpriseCreateDate">
        ${item.enterpriseCreateDate}
      </mjl:dt>
      <mjl:dt attribute="enterpriseCreatedBy">
        ${item.enterpriseCreatedBy.keyName}
      </mjl:dt>
      <mjl:dt attribute="gender">
        ${item.gender.keyName}
      </mjl:dt>
      <mjl:dt attribute="geoLocation">
        ${item.geoLocation.keyName}
      </mjl:dt>
      <mjl:dt attribute="initiateDate">
        ${item.initiateDate}
      </mjl:dt>
      <mjl:dt attribute="lastModifiedBy">
        ${item.lastModifiedBy.keyName}
      </mjl:dt>
      <mjl:dt attribute="lastModifiedDate">
        ${item.lastModifiedDate}
      </mjl:dt>
      <mjl:dt attribute="manager">
        ${item.manager}
      </mjl:dt>
      <mjl:dt attribute="mostRecentProfileDate">
        ${item.mostRecentProfileDate}
      </mjl:dt>
      <mjl:dt attribute="ownerName">
        ${item.ownerName}
      </mjl:dt>
      <mjl:dt attribute="permissionToShareContactDetails">
        ${item.permissionToShareContactDetails ? item.permissionToShareContactDetailsMd.positiveDisplayLabel : item.permissionToShareContactDetailsMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="primaryBusiness">
        ${item.primaryBusiness.keyName}
      </mjl:dt>
      <mjl:dt attribute="sellingStatus">
        ${item.sellingStatus.keyName}
      </mjl:dt>
      <mjl:dt attribute="signedMOU">
        ${item.signedMOU ? item.signedMOUMd.positiveDisplayLabel : item.signedMOUMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="yearOfBirth">
        ${item.yearOfBirth}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.Enterprise.form.edit.button" value="Edit" action="org.ideorg.iq.EnterpriseController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
  <dt>
    <label>
      Parent Relationships
    </label>
  </dt>
  <dd>
    <ul>
      <li>
        <mjl:commandLink name="org.ideorg.iq.WorksFor.parentQuery.link" action="org.ideorg.iq.WorksForController.parentQuery.mojo">
          
          <mjl:property name="parentId" value="${item.id}" />
        </mjl:commandLink>
      </li>
      <li>
        <mjl:commandLink name="org.ideorg.iq.ConsultsFor.parentQuery.link" action="org.ideorg.iq.ConsultsForController.parentQuery.mojo">
          
          <mjl:property name="parentId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
  <dt>
    <label>
      Child Relationships
    </label>
  </dt>
  <dd>
    <ul>
      <li>
        <mjl:commandLink name="org.ideorg.iq.EnterpriseSurveyRelationship.childQuery.link" action="org.ideorg.iq.EnterpriseSurveyRelationshipController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="org.ideorg.iq.Enterprise.viewAll.link" action="org.ideorg.iq.EnterpriseController.viewAll.mojo">
  View All
</mjl:commandLink>
