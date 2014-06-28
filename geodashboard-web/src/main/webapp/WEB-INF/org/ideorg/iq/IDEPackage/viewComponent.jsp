<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Package"/>
<dl>
  <mjl:form id="org.ideorg.iq.IDEPackage.form.id" name="org.ideorg.iq.IDEPackage.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="cost">
        ${item.cost}
      </mjl:dt>
      <mjl:dt attribute="item">
        ${item.item.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.IDEPackage.form.edit.button" value="Edit" action="org.ideorg.iq.IDEPackageController.edit.mojo" />
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
        <mjl:commandLink name="org.ideorg.iq.ComponentPackageRelationship.childQuery.link" action="org.ideorg.iq.ComponentPackageRelationshipController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="org.ideorg.iq.IDEPackage.viewAll.link" action="org.ideorg.iq.IDEPackageController.viewAll.mojo">
  View All
</mjl:commandLink>
