<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Cambodia LB Survey"/>
<dl>
  <mjl:form id="org.ideorg.iq.CambodiaLBSurvey.form.id" name="org.ideorg.iq.CambodiaLBSurvey.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.CambodiaLBSurvey.form.update.button" value="Update" action="org.ideorg.iq.CambodiaLBSurveyController.update.mojo" />
    <mjl:command name="org.ideorg.iq.CambodiaLBSurvey.form.delete.button" value="Delete" action="org.ideorg.iq.CambodiaLBSurveyController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.CambodiaLBSurvey.form.cancel.button" value="Cancel" action="org.ideorg.iq.CambodiaLBSurveyController.cancel.mojo" />
  </mjl:form>
</dl>
