<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Display Label"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabel.form.id" name="com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabel.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabel.form.update.button" value="Update" action="com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabelController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabel.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabelController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabel.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabelController.cancel.mojo" />
  </mjl:form>
</dl>
