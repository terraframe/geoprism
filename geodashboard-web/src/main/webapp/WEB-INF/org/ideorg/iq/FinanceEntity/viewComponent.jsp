<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Finance Entity"/>
<dl>
  <mjl:form id="org.ideorg.iq.FinanceEntity.form.id" name="org.ideorg.iq.FinanceEntity.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="name">
        ${item.name}
      </mjl:dt>
      <mjl:dt attribute="project">
        ${item.project.keyName}
      </mjl:dt>
      <mjl:dt attribute="url">
        ${item.url}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.FinanceEntity.form.edit.button" value="Edit" action="org.ideorg.iq.FinanceEntityController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.FinanceEntity.viewAll.link" action="org.ideorg.iq.FinanceEntityController.viewAll.mojo">
  View All
</mjl:commandLink>
