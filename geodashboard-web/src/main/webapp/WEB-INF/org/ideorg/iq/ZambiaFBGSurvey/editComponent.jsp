<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Zambia FBG Survey"/>
<dl>
  <mjl:form id="org.ideorg.iq.ZambiaFBGSurvey.form.id" name="org.ideorg.iq.ZambiaFBGSurvey.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.ZambiaFBGSurvey.form.update.button" value="Update" action="org.ideorg.iq.ZambiaFBGSurveyController.update.mojo" />
    <mjl:command name="org.ideorg.iq.ZambiaFBGSurvey.form.delete.button" value="Delete" action="org.ideorg.iq.ZambiaFBGSurveyController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.ZambiaFBGSurvey.form.cancel.button" value="Cancel" action="org.ideorg.iq.ZambiaFBGSurveyController.cancel.mojo" />
  </mjl:form>
</dl>
