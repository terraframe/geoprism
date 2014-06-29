<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Sales Component"/>
<dl>
  <mjl:form id="org.ideorg.iq.SalesComponent.form.id" name="org.ideorg.iq.SalesComponent.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.SalesComponent.form.update.button" value="Update" action="org.ideorg.iq.SalesComponentController.update.mojo" />
    <mjl:command name="org.ideorg.iq.SalesComponent.form.delete.button" value="Delete" action="org.ideorg.iq.SalesComponentController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.SalesComponent.form.cancel.button" value="Cancel" action="org.ideorg.iq.SalesComponentController.cancel.mojo" />
  </mjl:form>
</dl>
