<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new "/>
<dl>
  <mjl:form id="com.test.geodashboard.StateInfo.form.id" name="com.test.geodashboard.StateInfo.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.test.geodashboard.StateInfo.form.create.button" value="Create" action="com.test.geodashboard.StateInfoController.create.mojo" />
  </mjl:form>
</dl>
