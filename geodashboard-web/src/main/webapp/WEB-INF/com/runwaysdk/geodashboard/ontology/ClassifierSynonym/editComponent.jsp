<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Classifier Synonym"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.ontology.ClassifierSynonym.form.id" name="com.runwaysdk.geodashboard.ontology.ClassifierSynonym.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.ontology.ClassifierSynonym.form.update.button" value="Update" action="com.runwaysdk.geodashboard.ontology.ClassifierSynonymController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.ontology.ClassifierSynonym.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.ontology.ClassifierSynonymController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.ontology.ClassifierSynonym.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.ontology.ClassifierSynonymController.cancel.mojo" />
  </mjl:form>
</dl>
