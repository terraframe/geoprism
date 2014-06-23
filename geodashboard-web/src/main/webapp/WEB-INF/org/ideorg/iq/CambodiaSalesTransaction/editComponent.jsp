<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Cambodia Sales Transaction"/>
<dl>
  <mjl:form id="org.ideorg.iq.CambodiaSalesTransaction.form.id" name="org.ideorg.iq.CambodiaSalesTransaction.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.CambodiaSalesTransaction.form.update.button" value="Update" action="org.ideorg.iq.CambodiaSalesTransactionController.update.mojo" />
    <mjl:command name="org.ideorg.iq.CambodiaSalesTransaction.form.delete.button" value="Delete" action="org.ideorg.iq.CambodiaSalesTransactionController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.CambodiaSalesTransaction.form.cancel.button" value="Cancel" action="org.ideorg.iq.CambodiaSalesTransactionController.cancel.mojo" />
  </mjl:form>
</dl>
