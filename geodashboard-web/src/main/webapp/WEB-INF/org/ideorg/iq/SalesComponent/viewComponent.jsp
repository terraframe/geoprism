<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Sales Component"/>
<dl>
  <mjl:form id="org.ideorg.iq.SalesComponent.form.id" name="org.ideorg.iq.SalesComponent.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="cost">
        ${item.cost}
      </mjl:dt>
      <mjl:dt attribute="idePackage">
        ${item.idePackage.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.SalesComponent.form.edit.button" value="Edit" action="org.ideorg.iq.SalesComponentController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.SalesComponent.viewAll.link" action="org.ideorg.iq.SalesComponentController.viewAll.mojo">
  View All
</mjl:commandLink>
