<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Super type"/>
<dl>
  <mjl:form id="com.test.SuperType.form.id" name="com.test.SuperType.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.test.SuperType.form.create.button" value="Create" action="com.test.SuperTypeController.create.mojo" />
  </mjl:form>
</dl>
