<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Enterprise"/>
<dl>
  <mjl:form id="org.ideorg.iq.Enterprise.form.id" name="org.ideorg.iq.Enterprise.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.Enterprise.form.update.button" value="Update" action="org.ideorg.iq.EnterpriseController.update.mojo" />
    <mjl:command name="org.ideorg.iq.Enterprise.form.delete.button" value="Delete" action="org.ideorg.iq.EnterpriseController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.Enterprise.form.cancel.button" value="Cancel" action="org.ideorg.iq.EnterpriseController.cancel.mojo" />
  </mjl:form>
</dl>
