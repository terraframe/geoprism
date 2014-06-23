<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Business Development Coordinator"/>
<dl>
  <mjl:form id="org.ideorg.iq.BusinessDevelopmentCoordinator.form.id" name="org.ideorg.iq.BusinessDevelopmentCoordinator.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="active">
        ${item.active ? item.activeMd.positiveDisplayLabel : item.activeMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="agriculture">
        ${item.agriculture ? item.agricultureMd.positiveDisplayLabel : item.agricultureMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="name">
        ${item.name}
      </mjl:dt>
      <mjl:dt attribute="phoneNumber">
        ${item.phoneNumber}
      </mjl:dt>
      <mjl:dt attribute="workingArea">
        ${item.workingArea.keyName}
      </mjl:dt>
      <mjl:dt attribute="workingStation">
        ${item.workingStation.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.BusinessDevelopmentCoordinator.form.edit.button" value="Edit" action="org.ideorg.iq.BusinessDevelopmentCoordinatorController.edit.mojo" />
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
        <mjl:commandLink name="org.ideorg.iq.ConsultsFor.childQuery.link" action="org.ideorg.iq.ConsultsForController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="org.ideorg.iq.BusinessDevelopmentCoordinator.viewAll.link" action="org.ideorg.iq.BusinessDevelopmentCoordinatorController.viewAll.mojo">
  View All
</mjl:commandLink>
