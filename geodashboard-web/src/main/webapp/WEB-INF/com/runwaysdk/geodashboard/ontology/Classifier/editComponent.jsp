<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Classifier"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.ontology.Classifier.form.id" name="com.runwaysdk.geodashboard.ontology.Classifier.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.ontology.Classifier.form.update.button" value="Update" action="com.runwaysdk.geodashboard.ontology.ClassifierController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.ontology.Classifier.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.ontology.ClassifierController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.ontology.Classifier.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.ontology.ClassifierController.cancel.mojo" />
  </mjl:form>
</dl>
