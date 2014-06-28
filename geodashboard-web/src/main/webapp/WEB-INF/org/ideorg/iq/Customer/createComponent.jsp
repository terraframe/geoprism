<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Customer"/>
<dl>
  <mjl:form id="org.ideorg.iq.Customer.form.id" name="org.ideorg.iq.Customer.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.Customer.form.create.button" value="Create" action="org.ideorg.iq.CustomerController.create.mojo" />
  </mjl:form>
</dl>
