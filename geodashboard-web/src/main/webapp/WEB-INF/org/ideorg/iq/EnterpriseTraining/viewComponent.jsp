<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Enterprise Training"/>
<dl>
  <mjl:form id="org.ideorg.iq.EnterpriseTraining.form.id" name="org.ideorg.iq.EnterpriseTraining.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="enterprise">
        ${item.enterprise.keyName}
      </mjl:dt>
      <mjl:dt attribute="trainingDate">
        ${item.trainingDate}
      </mjl:dt>
      <mjl:dt attribute="trainingReceived">
        ${item.trainingReceived.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.EnterpriseTraining.form.edit.button" value="Edit" action="org.ideorg.iq.EnterpriseTrainingController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.EnterpriseTraining.viewAll.link" action="org.ideorg.iq.EnterpriseTrainingController.viewAll.mojo">
  View All
</mjl:commandLink>
