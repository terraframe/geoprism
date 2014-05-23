<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing "/>
<dl>
  <mjl:form id="com.test.geodashboard.StateInfo.form.id" name="com.test.geodashboard.StateInfo.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.test.geodashboard.StateInfo.form.update.button" value="Update" action="com.test.geodashboard.StateInfoController.update.mojo" />
    <mjl:command name="com.test.geodashboard.StateInfo.form.delete.button" value="Delete" action="com.test.geodashboard.StateInfoController.delete.mojo" />
    <mjl:command name="com.test.geodashboard.StateInfo.form.cancel.button" value="Cancel" action="com.test.geodashboard.StateInfoController.cancel.mojo" />
  </mjl:form>
</dl>
