<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Sales Transaction"/>
<dl>
  <mjl:form id="org.ideorg.iq.SalesTransaction.form.id" name="org.ideorg.iq.SalesTransaction.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="businessAgent">
        ${item.businessAgent.keyName}
      </mjl:dt>
      <mjl:dt attribute="businessDevelopmentCoordinator">
        ${item.businessDevelopmentCoordinator.keyName}
      </mjl:dt>
      <mjl:dt attribute="customer">
        ${item.customer.keyName}
      </mjl:dt>
      <mjl:dt attribute="customerType">
        ${item.customerType.keyName}
      </mjl:dt>
      <mjl:dt attribute="deliveryDate">
        ${item.deliveryDate}
      </mjl:dt>
      <mjl:dt attribute="enterprise">
        ${item.enterprise.keyName}
      </mjl:dt>
      <mjl:dt attribute="geoLocation">
        ${item.geoLocation.keyName}
      </mjl:dt>
      <mjl:dt attribute="numberOfUnits">
        ${item.numberOfUnits}
      </mjl:dt>
      <mjl:dt attribute="salesType">
        ${item.salesType.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.SalesTransaction.form.edit.button" value="Edit" action="org.ideorg.iq.SalesTransactionController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
  <dt>
    <label>
      Child Relationships
    </label>
  </dt>
  <dd>
    <ul>
      <li>
        <mjl:commandLink name="org.ideorg.iq.SalesPackageRelationship.childQuery.link" action="org.ideorg.iq.SalesPackageRelationshipController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="org.ideorg.iq.SalesTransaction.viewAll.link" action="org.ideorg.iq.SalesTransactionController.viewAll.mojo">
  View All
</mjl:commandLink>
