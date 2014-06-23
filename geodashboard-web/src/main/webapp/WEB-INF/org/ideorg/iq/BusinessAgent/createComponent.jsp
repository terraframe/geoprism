<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Business Agent"/>
<dl>
  <mjl:form id="org.ideorg.iq.BusinessAgent.form.id" name="org.ideorg.iq.BusinessAgent.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.BusinessAgent.form.create.button" value="Create" action="org.ideorg.iq.BusinessAgentController.create.mojo" />
  </mjl:form>
</dl>
