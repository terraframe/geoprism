<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Component"/>
<dl>
  <mjl:form id="org.ideorg.iq.Component.form.id" name="org.ideorg.iq.Component.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.Component.form.update.button" value="Update" action="org.ideorg.iq.ComponentController.update.mojo" />
    <mjl:command name="org.ideorg.iq.Component.form.delete.button" value="Delete" action="org.ideorg.iq.ComponentController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.Component.form.cancel.button" value="Cancel" action="org.ideorg.iq.ComponentController.cancel.mojo" />
  </mjl:form>
</dl>
