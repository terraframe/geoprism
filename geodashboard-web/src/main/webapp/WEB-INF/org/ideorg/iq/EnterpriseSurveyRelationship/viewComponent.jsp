<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Enterprise Survey Relationship"/>
<dl>
  <mjl:form id="org.ideorg.iq.EnterpriseSurveyRelationship.form.id" name="org.ideorg.iq.EnterpriseSurveyRelationship.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <dt>
        <label>
          Enterprise Survey
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="org.ideorg.iq.EnterpriseSurvey.form.view.link" action="org.ideorg.iq.EnterpriseSurveyController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
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
    </mjl:component>
    <mjl:command name="org.ideorg.iq.EnterpriseSurveyRelationship.form.edit.button" value="Edit" action="org.ideorg.iq.EnterpriseSurveyRelationshipController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.EnterpriseSurveyRelationship.viewAll.link" action="org.ideorg.iq.EnterpriseSurveyRelationshipController.viewAll.mojo">
  View All
</mjl:commandLink>
