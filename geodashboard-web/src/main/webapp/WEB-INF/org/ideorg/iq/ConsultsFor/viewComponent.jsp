<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Consults For"/>
<dl>
  <mjl:form id="org.ideorg.iq.ConsultsFor.form.id" name="org.ideorg.iq.ConsultsFor.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <dt>
        <label>
          Enterprise
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="org.ideorg.iq.Enterprise.form.view.link" action="org.ideorg.iq.EnterpriseController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
      <dt>
        <label>
          Business Development Coordinator
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="org.ideorg.iq.BusinessDevelopmentCoordinator.form.view.link" action="org.ideorg.iq.BusinessDevelopmentCoordinatorController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.ConsultsFor.form.edit.button" value="Edit" action="org.ideorg.iq.ConsultsForController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.ConsultsFor.viewAll.link" action="org.ideorg.iq.ConsultsForController.viewAll.mojo">
  View All
</mjl:commandLink>
