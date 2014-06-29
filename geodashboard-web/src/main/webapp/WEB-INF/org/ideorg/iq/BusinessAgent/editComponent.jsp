<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Business Agent"/>
<dl>
  <mjl:form id="org.ideorg.iq.BusinessAgent.form.id" name="org.ideorg.iq.BusinessAgent.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.BusinessAgent.form.update.button" value="Update" action="org.ideorg.iq.BusinessAgentController.update.mojo" />
    <mjl:command name="org.ideorg.iq.BusinessAgent.form.delete.button" value="Delete" action="org.ideorg.iq.BusinessAgentController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.BusinessAgent.form.cancel.button" value="Cancel" action="org.ideorg.iq.BusinessAgentController.cancel.mojo" />
  </mjl:form>
</dl>
