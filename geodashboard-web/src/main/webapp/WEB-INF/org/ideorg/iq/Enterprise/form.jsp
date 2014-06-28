<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="activeSalesAgents">
    <mjl:input param="activeSalesAgents" type="text" />
  </mjl:dt>
  <mjl:dt attribute="businessDevelopmentCoordinator">
    <mjl:select param="businessDevelopmentCoordinator" items="${_businessDevelopmentCoordinator}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="capitalInvestmentsTotalValue">
    <mjl:input param="capitalInvestmentsTotalValue" type="text" />
  </mjl:dt>
  <mjl:dt attribute="contactNumber">
    <mjl:input param="contactNumber" type="text" />
  </mjl:dt>
  <mjl:dt attribute="coordinatesMultiPolygon">
    <mjl:input param="coordinatesMultiPolygon" type="text" />
  </mjl:dt>
  <mjl:dt attribute="coordinatesPoint">
    <mjl:input param="coordinatesPoint" type="text" />
  </mjl:dt>
  <mjl:dt attribute="email">
    <mjl:input param="email" type="text" />
  </mjl:dt>
  <mjl:dt attribute="enterpriseCreateDate">
    <mjl:input param="enterpriseCreateDate" type="text" />
  </mjl:dt>
  <mjl:dt attribute="enterpriseCreatedBy">
    <mjl:select param="enterpriseCreatedBy" items="${_enterpriseCreatedBy}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="gender">
    <mjl:select param="gender" items="${_gender}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="geoLocation">
    <mjl:select param="geoLocation" items="${_geoLocation}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="initiateDate">
    <mjl:input param="initiateDate" type="text" />
  </mjl:dt>
  <mjl:dt attribute="lastModifiedBy">
    <mjl:select param="lastModifiedBy" items="${_lastModifiedBy}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="lastModifiedDate">
    <mjl:input param="lastModifiedDate" type="text" />
  </mjl:dt>
  <mjl:dt attribute="manager">
    <mjl:input param="manager" type="text" />
  </mjl:dt>
  <mjl:dt attribute="mostRecentProfileDate">
    <mjl:input param="mostRecentProfileDate" type="text" />
  </mjl:dt>
  <mjl:dt attribute="ownerName">
    <mjl:input param="ownerName" type="text" />
  </mjl:dt>
  <mjl:dt attribute="permissionToShareContactDetails">
    <mjl:boolean param="permissionToShareContactDetails" />
  </mjl:dt>
  <mjl:dt attribute="primaryBusiness">
    <mjl:select param="primaryBusiness" items="${_primaryBusiness}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="sellingStatus">
    <mjl:select param="sellingStatus" items="${_sellingStatus}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="signedMOU">
    <mjl:boolean param="signedMOU" />
  </mjl:dt>
  <mjl:dt attribute="yearOfBirth">
    <mjl:input param="yearOfBirth" type="text" />
  </mjl:dt>
</mjl:component>
