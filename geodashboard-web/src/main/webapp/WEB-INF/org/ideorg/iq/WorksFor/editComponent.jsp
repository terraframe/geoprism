<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Consults For"/>
<dl>
  <mjl:form id="org.ideorg.iq.WorksFor.form.id" name="org.ideorg.iq.WorksFor.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.WorksFor.form.update.button" value="Update" action="org.ideorg.iq.WorksForController.update.mojo" />
    <mjl:command name="org.ideorg.iq.WorksFor.form.delete.button" value="Delete" action="org.ideorg.iq.WorksForController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.WorksFor.form.cancel.button" value="Cancel" action="org.ideorg.iq.WorksForController.cancel.mojo" />
  </mjl:form>
</dl>
