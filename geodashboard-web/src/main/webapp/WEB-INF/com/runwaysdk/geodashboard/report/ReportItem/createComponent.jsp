<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>

<gdb:localize var="page_title" key="report.createTitle"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.report.ReportItem.form.id" name="com.runwaysdk.geodashboard.report.ReportItem.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.report.ReportItem.form.create.button" value="Create" action="com.runwaysdk.geodashboard.report.ReportItemController.create.mojo" />
  </mjl:form>
</dl>
