<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Sales Package"/>
<dl>
  <mjl:form id="org.ideorg.iq.SalesPackage.form.id" name="org.ideorg.iq.SalesPackage.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.SalesPackage.form.update.button" value="Update" action="org.ideorg.iq.SalesPackageController.update.mojo" />
    <mjl:command name="org.ideorg.iq.SalesPackage.form.delete.button" value="Delete" action="org.ideorg.iq.SalesPackageController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.SalesPackage.form.cancel.button" value="Cancel" action="org.ideorg.iq.SalesPackageController.cancel.mojo" />
  </mjl:form>
</dl>
