<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>

<gdb:localize var="page_title" key="report.editTitle"/>

<dl>
  <mjl:form id="com.runwaysdk.geodashboard.report.ReportItem.form.id" name="com.runwaysdk.geodashboard.report.ReportItem.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.report.ReportItem.form.update.button" value="Update" action="com.runwaysdk.geodashboard.report.ReportItemController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.report.ReportItem.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.report.ReportItemController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.report.ReportItem.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.report.ReportItemController.cancel.mojo" />
  </mjl:form>
</dl>
