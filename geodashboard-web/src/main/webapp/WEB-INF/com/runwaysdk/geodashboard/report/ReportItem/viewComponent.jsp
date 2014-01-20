<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Report item"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.report.ReportItem.form.id" name="com.runwaysdk.geodashboard.report.ReportItem.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="cacheDocument">
        ${item.cacheDocument ? item.cacheDocumentMd.positiveDisplayLabel : item.cacheDocumentMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="design">
        ${item.design}
      </mjl:dt>
      <mjl:dt attribute="document">
        ${item.document}
      </mjl:dt>
      <mjl:dt attribute="outputFormat">
        <ul>
          <c:forEach items="${item.outputFormatEnumNames}" var="enumName">
            <li>
              ${item.outputFormatMd.enumItems[enumName]}
            </li>
          </c:forEach>
        </ul>
      </mjl:dt>
      <mjl:dt attribute="outputFormatIndex">
        ${item.outputFormatIndex}
      </mjl:dt>
      <mjl:dt attribute="reportLabel">
        ${item.reportLabel}
      </mjl:dt>
      <mjl:dt attribute="reportName">
        ${item.reportName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.report.ReportItem.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.report.ReportItemController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.report.ReportItem.viewAll.link" action="com.runwaysdk.geodashboard.report.ReportItemController.viewAll.mojo">
  View All
</mjl:commandLink>
