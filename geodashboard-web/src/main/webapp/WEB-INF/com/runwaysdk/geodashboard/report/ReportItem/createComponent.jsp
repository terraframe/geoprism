<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Report item"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.report.ReportItem.form.id" name="com.runwaysdk.geodashboard.report.ReportItem.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.report.ReportItem.form.create.button" value="Create" action="com.runwaysdk.geodashboard.report.ReportItemController.create.mojo" />
  </mjl:form>
</dl>
