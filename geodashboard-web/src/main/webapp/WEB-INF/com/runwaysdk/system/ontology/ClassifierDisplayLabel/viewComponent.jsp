<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Display Label"/>
<dl>
  <mjl:form id="com.runwaysdk.system.ontology.ClassifierDisplayLabel.form.id" name="com.runwaysdk.system.ontology.ClassifierDisplayLabel.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="defaultLocale">
        ${item.defaultLocale}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.system.ontology.ClassifierDisplayLabel.form.edit.button" value="Edit" action="com.runwaysdk.system.ontology.ClassifierDisplayLabelController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.system.ontology.ClassifierDisplayLabel.viewAll.link" action="com.runwaysdk.system.ontology.ClassifierDisplayLabelController.viewAll.mojo">
  View All
</mjl:commandLink>
