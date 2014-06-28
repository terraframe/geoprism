<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Finance Entity"/>
<dl>
  <mjl:form id="org.ideorg.iq.FinanceEntity.form.id" name="org.ideorg.iq.FinanceEntity.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.FinanceEntity.form.create.button" value="Create" action="org.ideorg.iq.FinanceEntityController.create.mojo" />
  </mjl:form>
</dl>
