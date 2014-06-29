<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Sales Package"/>
<dl>
  <mjl:form id="org.ideorg.iq.SalesPackage.form.id" name="org.ideorg.iq.SalesPackage.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="cost">
        ${item.cost}
      </mjl:dt>
      <mjl:dt attribute="idePackage">
        ${item.idePackage.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.SalesPackage.form.edit.button" value="Edit" action="org.ideorg.iq.SalesPackageController.edit.mojo" />
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
        <mjl:commandLink name="org.ideorg.iq.SalesPackageRelationship.parentQuery.link" action="org.ideorg.iq.SalesPackageRelationshipController.parentQuery.mojo">
          
          <mjl:property name="parentId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="org.ideorg.iq.SalesPackage.viewAll.link" action="org.ideorg.iq.SalesPackageController.viewAll.mojo">
  View All
</mjl:commandLink>
