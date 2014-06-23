<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Sales Package"/>
<dl>
  <mjl:form id="org.ideorg.iq.SalesPackage.form.id" name="org.ideorg.iq.SalesPackage.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.SalesPackage.form.create.button" value="Create" action="org.ideorg.iq.SalesPackageController.create.mojo" />
  </mjl:form>
</dl>
