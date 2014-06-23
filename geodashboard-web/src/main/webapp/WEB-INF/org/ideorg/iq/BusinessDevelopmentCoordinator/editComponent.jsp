<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Business Development Coordinator"/>
<dl>
  <mjl:form id="org.ideorg.iq.BusinessDevelopmentCoordinator.form.id" name="org.ideorg.iq.BusinessDevelopmentCoordinator.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.BusinessDevelopmentCoordinator.form.update.button" value="Update" action="org.ideorg.iq.BusinessDevelopmentCoordinatorController.update.mojo" />
    <mjl:command name="org.ideorg.iq.BusinessDevelopmentCoordinator.form.delete.button" value="Delete" action="org.ideorg.iq.BusinessDevelopmentCoordinatorController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.BusinessDevelopmentCoordinator.form.cancel.button" value="Cancel" action="org.ideorg.iq.BusinessDevelopmentCoordinatorController.cancel.mojo" />
  </mjl:form>
</dl>
