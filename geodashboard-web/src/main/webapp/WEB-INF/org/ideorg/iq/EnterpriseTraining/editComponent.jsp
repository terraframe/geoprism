<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Enterprise Training"/>
<dl>
  <mjl:form id="org.ideorg.iq.EnterpriseTraining.form.id" name="org.ideorg.iq.EnterpriseTraining.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.EnterpriseTraining.form.update.button" value="Update" action="org.ideorg.iq.EnterpriseTrainingController.update.mojo" />
    <mjl:command name="org.ideorg.iq.EnterpriseTraining.form.delete.button" value="Delete" action="org.ideorg.iq.EnterpriseTrainingController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.EnterpriseTraining.form.cancel.button" value="Cancel" action="org.ideorg.iq.EnterpriseTrainingController.cancel.mojo" />
  </mjl:form>
</dl>
