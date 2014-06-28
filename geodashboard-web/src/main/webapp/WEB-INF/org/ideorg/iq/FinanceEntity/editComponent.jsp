<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Finance Entity"/>
<dl>
  <mjl:form id="org.ideorg.iq.FinanceEntity.form.id" name="org.ideorg.iq.FinanceEntity.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.FinanceEntity.form.update.button" value="Update" action="org.ideorg.iq.FinanceEntityController.update.mojo" />
    <mjl:command name="org.ideorg.iq.FinanceEntity.form.delete.button" value="Delete" action="org.ideorg.iq.FinanceEntityController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.FinanceEntity.form.cancel.button" value="Cancel" action="org.ideorg.iq.FinanceEntityController.cancel.mojo" />
  </mjl:form>
</dl>
