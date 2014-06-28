<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Enterprise Training"/>
<dl>
  <mjl:form id="org.ideorg.iq.EnterpriseTraining.form.id" name="org.ideorg.iq.EnterpriseTraining.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.EnterpriseTraining.form.create.button" value="Create" action="org.ideorg.iq.EnterpriseTrainingController.create.mojo" />
  </mjl:form>
</dl>
