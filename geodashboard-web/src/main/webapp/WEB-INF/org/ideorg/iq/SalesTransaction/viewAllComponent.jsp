<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Sales Transaction"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.SalesTransactionController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="businessAgent">
      <mjl:row>
        ${item.businessAgent.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="businessDevelopmentCoordinator">
      <mjl:row>
        ${item.businessDevelopmentCoordinator.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="customer">
      <mjl:row>
        ${item.customer.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="customerType">
      <mjl:row>
        ${item.customerType.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="deliveryDate">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="enterprise">
      <mjl:row>
        ${item.enterprise.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="geoLocation">
      <mjl:row>
        ${item.geoLocation.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="numberOfUnits">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="salesType">
      <mjl:row>
        ${item.salesType.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="org.ideorg.iq.SalesTransactionController.view.mojo">
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
<mjl:commandLink name="SalesTransactionController.newInstance" action="org.ideorg.iq.SalesTransactionController.newInstance.mojo">
  Create a new Sales Transaction
</mjl:commandLink>
