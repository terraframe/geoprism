<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="bank">
    <mjl:boolean param="bank" />
  </mjl:dt>
  <mjl:dt attribute="bankLoanAmount">
    <mjl:input param="bankLoanAmount" type="text" />
  </mjl:dt>
  <mjl:dt attribute="capitalInvestmentsTotalValue">
    <mjl:input param="capitalInvestmentsTotalValue" type="text" />
  </mjl:dt>
  <mjl:dt attribute="collectionCoordinator">
    <mjl:select param="collectionCoordinator" items="${_collectionCoordinator}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="collectionDate">
    <mjl:input param="collectionDate" type="text" />
  </mjl:dt>
  <mjl:dt attribute="comission">
    <mjl:input param="comission" type="text" />
  </mjl:dt>
  <mjl:dt attribute="currentCasualLabor">
    <mjl:input param="currentCasualLabor" type="text" />
  </mjl:dt>
  <mjl:dt attribute="currentFullTimeLabor">
    <mjl:input param="currentFullTimeLabor" type="text" />
  </mjl:dt>
  <mjl:dt attribute="currentPartTimeLabor">
    <mjl:input param="currentPartTimeLabor" type="text" />
  </mjl:dt>
  <mjl:dt attribute="microFinanceInstitution">
    <mjl:boolean param="microFinanceInstitution" />
  </mjl:dt>
  <mjl:dt attribute="microFinanceLoanAmount">
    <mjl:input param="microFinanceLoanAmount" type="text" />
  </mjl:dt>
  <mjl:dt attribute="sellingStatus">
    <mjl:select param="sellingStatus" items="${_sellingStatus}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
</mjl:component>
