<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Zambia Sales Transaction"/>
<dl>
  <mjl:form id="org.ideorg.iq.ZambiaSalesTransaction.form.id" name="org.ideorg.iq.ZambiaSalesTransaction.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.ZambiaSalesTransaction.form.update.button" value="Update" action="org.ideorg.iq.ZambiaSalesTransactionController.update.mojo" />
    <mjl:command name="org.ideorg.iq.ZambiaSalesTransaction.form.delete.button" value="Delete" action="org.ideorg.iq.ZambiaSalesTransactionController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.ZambiaSalesTransaction.form.cancel.button" value="Cancel" action="org.ideorg.iq.ZambiaSalesTransactionController.cancel.mojo" />
  </mjl:form>
</dl>
