<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Enterprise Survey Relationship"/>
<dl>
  <mjl:form id="org.ideorg.iq.EnterpriseSurveyRelationship.form.id" name="org.ideorg.iq.EnterpriseSurveyRelationship.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.EnterpriseSurveyRelationship.form.update.button" value="Update" action="org.ideorg.iq.EnterpriseSurveyRelationshipController.update.mojo" />
    <mjl:command name="org.ideorg.iq.EnterpriseSurveyRelationship.form.delete.button" value="Delete" action="org.ideorg.iq.EnterpriseSurveyRelationshipController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.EnterpriseSurveyRelationship.form.cancel.button" value="Cancel" action="org.ideorg.iq.EnterpriseSurveyRelationshipController.cancel.mojo" />
  </mjl:form>
</dl>
