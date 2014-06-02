<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Classifier"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.ontology.Classifier.form.id" name="com.runwaysdk.geodashboard.ontology.Classifier.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="classifierId">
        ${item.classifierId}
      </mjl:dt>
      <mjl:dt attribute="classifierPackage">
        ${item.classifierPackage}
      </mjl:dt>
      <mjl:dt attribute="displayLabel">
        ${item.displayLabel}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.ontology.Classifier.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.ontology.ClassifierController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.ontology.Classifier.viewAll.link" action="com.runwaysdk.geodashboard.ontology.ClassifierController.viewAll.mojo">
  View All
</mjl:commandLink>
