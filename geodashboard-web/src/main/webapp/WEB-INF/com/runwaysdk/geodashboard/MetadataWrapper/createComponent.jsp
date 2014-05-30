<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Dashboard Wrapper"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.MetadataWrapper.form.id" name="com.runwaysdk.geodashboard.MetadataWrapper.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.MetadataWrapper.form.create.button" value="Create" action="com.runwaysdk.geodashboard.MetadataWrapperController.create.mojo" />
  </mjl:form>
</dl>
