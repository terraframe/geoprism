<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing "/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabel.form.id" name="com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabel.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabel.form.update.button" value="Update" action="com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabelController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabel.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabelController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabel.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabelController.cancel.mojo" />
  </mjl:form>
</dl>
