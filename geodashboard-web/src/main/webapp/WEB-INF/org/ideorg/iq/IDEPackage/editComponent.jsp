<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Package"/>
<dl>
  <mjl:form id="org.ideorg.iq.IDEPackage.form.id" name="org.ideorg.iq.IDEPackage.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.IDEPackage.form.update.button" value="Update" action="org.ideorg.iq.IDEPackageController.update.mojo" />
    <mjl:command name="org.ideorg.iq.IDEPackage.form.delete.button" value="Delete" action="org.ideorg.iq.IDEPackageController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.IDEPackage.form.cancel.button" value="Cancel" action="org.ideorg.iq.IDEPackageController.cancel.mojo" />
  </mjl:form>
</dl>
