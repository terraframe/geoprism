<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="businessAgent">
    <mjl:select param="businessAgent" items="${_businessAgent}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="businessDevelopmentCoordinator">
    <mjl:select param="businessDevelopmentCoordinator" items="${_businessDevelopmentCoordinator}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="customer">
    <mjl:select param="customer" items="${_customer}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="customerType">
    <mjl:select param="customerType" items="${_customerType}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="deliveryDate">
    <mjl:input param="deliveryDate" type="text" />
  </mjl:dt>
  <mjl:dt attribute="enterprise">
    <mjl:select param="enterprise" items="${_enterprise}" var="current" valueAttribute="id">
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
  <mjl:dt attribute="numberOfUnits">
    <mjl:input param="numberOfUnits" type="text" />
  </mjl:dt>
  <mjl:dt attribute="salesType">
    <mjl:select param="salesType" items="${_salesType}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
</mjl:component>
