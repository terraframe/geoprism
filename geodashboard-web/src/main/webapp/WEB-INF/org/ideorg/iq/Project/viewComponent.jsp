<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Project"/>
<dl>
  <mjl:form id="org.ideorg.iq.Project.form.id" name="org.ideorg.iq.Project.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="amount">
        ${item.amount}
      </mjl:dt>
      <mjl:dt attribute="endPeriod">
        ${item.endPeriod}
      </mjl:dt>
      <mjl:dt attribute="geoLocation">
        ${item.geoLocation.keyName}
      </mjl:dt>
      <mjl:dt attribute="name">
        ${item.name}
      </mjl:dt>
      <mjl:dt attribute="startPeriod">
        ${item.startPeriod}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.Project.form.edit.button" value="Edit" action="org.ideorg.iq.ProjectController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.Project.viewAll.link" action="org.ideorg.iq.ProjectController.viewAll.mojo">
  View All
</mjl:commandLink>
