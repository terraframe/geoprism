<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Sales Package Relationship"/>
<dl>
  <mjl:form id="org.ideorg.iq.SalesPackageRelationship.form.id" name="org.ideorg.iq.SalesPackageRelationship.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <dt>
        <label>
          Sales Package
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="org.ideorg.iq.SalesPackage.form.view.link" action="org.ideorg.iq.SalesPackageController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
      <dt>
        <label>
          Sales Transaction
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="org.ideorg.iq.SalesTransaction.form.view.link" action="org.ideorg.iq.SalesTransactionController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.SalesPackageRelationship.form.edit.button" value="Edit" action="org.ideorg.iq.SalesPackageRelationshipController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.SalesPackageRelationship.viewAll.link" action="org.ideorg.iq.SalesPackageRelationshipController.viewAll.mojo">
  View All
</mjl:commandLink>
