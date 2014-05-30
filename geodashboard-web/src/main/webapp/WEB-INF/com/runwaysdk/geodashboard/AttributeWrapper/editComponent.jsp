<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Attribute Wrapper"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.AttributeWrapper.form.id" name="com.runwaysdk.geodashboard.AttributeWrapper.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.AttributeWrapper.form.update.button" value="Update" action="com.runwaysdk.geodashboard.AttributeWrapperController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.AttributeWrapper.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.AttributeWrapperController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.AttributeWrapper.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.AttributeWrapperController.cancel.mojo" />
  </mjl:form>
</dl>
