<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Project"/>
<dl>
  <mjl:form id="org.ideorg.iq.Project.form.id" name="org.ideorg.iq.Project.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.Project.form.create.button" value="Create" action="org.ideorg.iq.ProjectController.create.mojo" />
  </mjl:form>
</dl>
