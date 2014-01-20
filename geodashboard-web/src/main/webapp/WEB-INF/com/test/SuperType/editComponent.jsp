<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Super type"/>
<dl>
  <mjl:form id="com.test.SuperType.form.id" name="com.test.SuperType.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.test.SuperType.form.update.button" value="Update" action="com.test.SuperTypeController.update.mojo" />
    <mjl:command name="com.test.SuperType.form.delete.button" value="Delete" action="com.test.SuperTypeController.delete.mojo" />
    <mjl:command name="com.test.SuperType.form.cancel.button" value="Cancel" action="com.test.SuperTypeController.cancel.mojo" />
  </mjl:form>
</dl>
