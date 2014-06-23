<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Sales Package Relationship"/>
<dl>
  <mjl:form id="org.ideorg.iq.SalesPackageRelationship.form.id" name="org.ideorg.iq.SalesPackageRelationship.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.SalesPackageRelationship.form.update.button" value="Update" action="org.ideorg.iq.SalesPackageRelationshipController.update.mojo" />
    <mjl:command name="org.ideorg.iq.SalesPackageRelationship.form.delete.button" value="Delete" action="org.ideorg.iq.SalesPackageRelationshipController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.SalesPackageRelationship.form.cancel.button" value="Cancel" action="org.ideorg.iq.SalesPackageRelationshipController.cancel.mojo" />
  </mjl:form>
</dl>
