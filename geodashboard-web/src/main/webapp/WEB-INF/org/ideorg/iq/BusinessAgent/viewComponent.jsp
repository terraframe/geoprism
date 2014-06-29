<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Business Agent"/>
<dl>
  <mjl:form id="org.ideorg.iq.BusinessAgent.form.id" name="org.ideorg.iq.BusinessAgent.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="dateOfBirth">
        ${item.dateOfBirth}
      </mjl:dt>
      <mjl:dt attribute="education">
        ${item.education.keyName}
      </mjl:dt>
      <mjl:dt attribute="firstName">
        ${item.firstName}
      </mjl:dt>
      <mjl:dt attribute="gender">
        ${item.gender.keyName}
      </mjl:dt>
      <mjl:dt attribute="lastName">
        ${item.lastName}
      </mjl:dt>
      <mjl:dt attribute="phoneNumber">
        ${item.phoneNumber}
      </mjl:dt>
      <mjl:dt attribute="role">
        ${item.role.keyName}
      </mjl:dt>
      <mjl:dt attribute="startDate">
        ${item.startDate}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.BusinessAgent.form.edit.button" value="Edit" action="org.ideorg.iq.BusinessAgentController.edit.mojo" />
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
        <mjl:commandLink name="org.ideorg.iq.WorksFor.childQuery.link" action="org.ideorg.iq.WorksForController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="org.ideorg.iq.BusinessAgent.viewAll.link" action="org.ideorg.iq.BusinessAgentController.viewAll.mojo">
  View All
</mjl:commandLink>
