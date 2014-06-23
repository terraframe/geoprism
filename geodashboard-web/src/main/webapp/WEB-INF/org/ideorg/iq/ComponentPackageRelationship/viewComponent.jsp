<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Component Package Relationship"/>
<dl>
  <mjl:form id="org.ideorg.iq.ComponentPackageRelationship.form.id" name="org.ideorg.iq.ComponentPackageRelationship.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <dt>
        <label>
          Component
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="org.ideorg.iq.Component.form.view.link" action="org.ideorg.iq.ComponentController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
      <dt>
        <label>
          Package
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="org.ideorg.iq.IDEPackage.form.view.link" action="org.ideorg.iq.IDEPackageController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.ComponentPackageRelationship.form.edit.button" value="Edit" action="org.ideorg.iq.ComponentPackageRelationshipController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.ComponentPackageRelationship.viewAll.link" action="org.ideorg.iq.ComponentPackageRelationshipController.viewAll.mojo">
  View All
</mjl:commandLink>
