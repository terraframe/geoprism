<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Cambodia LB Enterprise"/>
<dl>
  <mjl:form id="org.ideorg.iq.CambodiaLBEnterprise.form.id" name="org.ideorg.iq.CambodiaLBEnterprise.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.CambodiaLBEnterprise.form.update.button" value="Update" action="org.ideorg.iq.CambodiaLBEnterpriseController.update.mojo" />
    <mjl:command name="org.ideorg.iq.CambodiaLBEnterprise.form.delete.button" value="Delete" action="org.ideorg.iq.CambodiaLBEnterpriseController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.CambodiaLBEnterprise.form.cancel.button" value="Cancel" action="org.ideorg.iq.CambodiaLBEnterpriseController.cancel.mojo" />
  </mjl:form>
</dl>
