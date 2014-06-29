<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Component"/>
<dl>
  <mjl:form id="org.ideorg.iq.Component.form.id" name="org.ideorg.iq.Component.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="cost">
        ${item.cost}
      </mjl:dt>
      <mjl:dt attribute="idePackage">
        ${item.idePackage.keyName}
      </mjl:dt>
      <mjl:dt attribute="item">
        ${item.item.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.Component.form.edit.button" value="Edit" action="org.ideorg.iq.ComponentController.edit.mojo" />
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
        <mjl:commandLink name="org.ideorg.iq.ComponentPackageRelationship.parentQuery.link" action="org.ideorg.iq.ComponentPackageRelationshipController.parentQuery.mojo">
          
          <mjl:property name="parentId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="org.ideorg.iq.Component.viewAll.link" action="org.ideorg.iq.ComponentController.viewAll.mojo">
  View All
</mjl:commandLink>
