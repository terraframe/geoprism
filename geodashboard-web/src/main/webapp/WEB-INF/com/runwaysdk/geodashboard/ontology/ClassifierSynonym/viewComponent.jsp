<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Classifier Synonym"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.ontology.ClassifierSynonym.form.id" name="com.runwaysdk.geodashboard.ontology.ClassifierSynonym.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="displayLabel">
        ${item.displayLabel}
      </mjl:dt>
      <mjl:dt attribute="synonymId">
        ${item.synonymId}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.ontology.ClassifierSynonym.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.ontology.ClassifierSynonymController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.ontology.ClassifierSynonym.viewAll.link" action="com.runwaysdk.geodashboard.ontology.ClassifierSynonymController.viewAll.mojo">
  View All
</mjl:commandLink>
