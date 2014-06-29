<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Sales Transaction"/>
<dl>
  <mjl:form id="org.ideorg.iq.SalesTransaction.form.id" name="org.ideorg.iq.SalesTransaction.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.SalesTransaction.form.update.button" value="Update" action="org.ideorg.iq.SalesTransactionController.update.mojo" />
    <mjl:command name="org.ideorg.iq.SalesTransaction.form.delete.button" value="Delete" action="org.ideorg.iq.SalesTransactionController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.SalesTransaction.form.cancel.button" value="Cancel" action="org.ideorg.iq.SalesTransactionController.cancel.mojo" />
  </mjl:form>
</dl>
