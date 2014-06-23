<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Enterprise Survey"/>
<dl>
  <mjl:form id="org.ideorg.iq.EnterpriseSurvey.form.id" name="org.ideorg.iq.EnterpriseSurvey.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="bank">
        ${item.bank ? item.bankMd.positiveDisplayLabel : item.bankMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="bankLoanAmount">
        ${item.bankLoanAmount}
      </mjl:dt>
      <mjl:dt attribute="capitalInvestmentsTotalValue">
        ${item.capitalInvestmentsTotalValue}
      </mjl:dt>
      <mjl:dt attribute="collectionCoordinator">
        ${item.collectionCoordinator.keyName}
      </mjl:dt>
      <mjl:dt attribute="collectionDate">
        ${item.collectionDate}
      </mjl:dt>
      <mjl:dt attribute="comission">
        ${item.comission}
      </mjl:dt>
      <mjl:dt attribute="currentCasualLabor">
        ${item.currentCasualLabor}
      </mjl:dt>
      <mjl:dt attribute="currentFullTimeLabor">
        ${item.currentFullTimeLabor}
      </mjl:dt>
      <mjl:dt attribute="currentPartTimeLabor">
        ${item.currentPartTimeLabor}
      </mjl:dt>
      <mjl:dt attribute="microFinanceInstitution">
        ${item.microFinanceInstitution ? item.microFinanceInstitutionMd.positiveDisplayLabel : item.microFinanceInstitutionMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="microFinanceLoanAmount">
        ${item.microFinanceLoanAmount}
      </mjl:dt>
      <mjl:dt attribute="sellingStatus">
        ${item.sellingStatus.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.EnterpriseSurvey.form.edit.button" value="Edit" action="org.ideorg.iq.EnterpriseSurveyController.edit.mojo" />
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
        <mjl:commandLink name="org.ideorg.iq.EnterpriseSurveyRelationship.parentQuery.link" action="org.ideorg.iq.EnterpriseSurveyRelationshipController.parentQuery.mojo">
          
          <mjl:property name="parentId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="org.ideorg.iq.EnterpriseSurvey.viewAll.link" action="org.ideorg.iq.EnterpriseSurveyController.viewAll.mojo">
  View All
</mjl:commandLink>
