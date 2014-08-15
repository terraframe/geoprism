<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Map"/>

  <mjl:form id="com.runwaysdk.geodashboard.Dashboard.form.id" 
  name="com.runwaysdk.geodashboard.Dashboard.form.name" method="POST" classes="modal-form" >
    <%@include file="form.jsp" %>
  </mjl:form>

