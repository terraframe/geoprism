<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Customer"/>
<dl>
  <mjl:form id="org.ideorg.iq.Customer.form.id" name="org.ideorg.iq.Customer.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
    </mjl:component>
    <mjl:command name="org.ideorg.iq.Customer.form.edit.button" value="Edit" action="org.ideorg.iq.CustomerController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.Customer.viewAll.link" action="org.ideorg.iq.CustomerController.viewAll.mojo">
  View All
</mjl:commandLink>
