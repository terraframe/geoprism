<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Report label"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.report.ReportItemReportLabel.form.id" name="com.runwaysdk.geodashboard.report.ReportItemReportLabel.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.report.ReportItemReportLabel.form.create.button" value="Create" action="com.runwaysdk.geodashboard.report.ReportItemReportLabelController.create.mojo" />
  </mjl:form>
</dl>
