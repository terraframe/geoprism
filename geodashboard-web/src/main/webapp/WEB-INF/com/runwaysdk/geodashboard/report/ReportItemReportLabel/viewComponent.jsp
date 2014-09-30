<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Report label"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.report.ReportItemReportLabel.form.id" name="com.runwaysdk.geodashboard.report.ReportItemReportLabel.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="defaultLocale">
        ${item.defaultLocale}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.report.ReportItemReportLabel.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.report.ReportItemReportLabelController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.report.ReportItemReportLabel.viewAll.link" action="com.runwaysdk.geodashboard.report.ReportItemReportLabelController.viewAll.mojo">
  View All
</mjl:commandLink>
