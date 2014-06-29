<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Cambodia LB Survey"/>
<dl>
  <mjl:form id="org.ideorg.iq.CambodiaLBSurvey.form.id" name="org.ideorg.iq.CambodiaLBSurvey.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.CambodiaLBSurvey.form.create.button" value="Create" action="org.ideorg.iq.CambodiaLBSurveyController.create.mojo" />
  </mjl:form>
</dl>
