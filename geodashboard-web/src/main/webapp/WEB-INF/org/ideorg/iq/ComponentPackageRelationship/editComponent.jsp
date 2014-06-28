<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Component Package Relationship"/>
<dl>
  <mjl:form id="org.ideorg.iq.ComponentPackageRelationship.form.id" name="org.ideorg.iq.ComponentPackageRelationship.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.ComponentPackageRelationship.form.update.button" value="Update" action="org.ideorg.iq.ComponentPackageRelationshipController.update.mojo" />
    <mjl:command name="org.ideorg.iq.ComponentPackageRelationship.form.delete.button" value="Delete" action="org.ideorg.iq.ComponentPackageRelationshipController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.ComponentPackageRelationship.form.cancel.button" value="Cancel" action="org.ideorg.iq.ComponentPackageRelationshipController.cancel.mojo" />
  </mjl:form>
</dl>
