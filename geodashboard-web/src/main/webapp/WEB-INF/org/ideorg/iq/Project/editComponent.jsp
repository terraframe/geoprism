<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Project"/>
<dl>
  <mjl:form id="org.ideorg.iq.Project.form.id" name="org.ideorg.iq.Project.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.Project.form.update.button" value="Update" action="org.ideorg.iq.ProjectController.update.mojo" />
    <mjl:command name="org.ideorg.iq.Project.form.delete.button" value="Delete" action="org.ideorg.iq.ProjectController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.Project.form.cancel.button" value="Cancel" action="org.ideorg.iq.ProjectController.cancel.mojo" />
  </mjl:form>
</dl>
