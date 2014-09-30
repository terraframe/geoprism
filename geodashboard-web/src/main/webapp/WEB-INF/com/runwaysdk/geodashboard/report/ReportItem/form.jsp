<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<fieldset class="com-runwaysdk-geodashboard-FormList">
  <section class="form-container">
    <mjl:component param="dto" item="${item}">
      <div class="field-row clearfix">
        <label for="reportLabel">* ${item.reportLabelMd.displayLabel}</label>
        <mjl:input param="reportLabel" type="text" id="reportLabel" />
        <div id="reportLabel-error" class="error-message"></div>
      </div>
      <div class="field-row clearfix">
        <label for="dashboard">* ${item.dashboardMd.displayLabel}</label>
        <mjl:select param="dashboard" items="${dashboards}" var="current" valueAttribute="id" id="dashboard">
          <mjl:option selected="${(item.dashboardId != null && current.id == item.dashboardId) ? 'selected' : 'false'}">
            ${current.displayLabel.value}
          </mjl:option>
        </mjl:select>
      </div>      
      <c:if test="${!item.newInstance}">
        <div class="field-row clearfix">
          <label for="reportLabel">* ${item.reportNameMd.displayLabel}</label>
          <label id="reportName"> ${item.reportName}</label>
          <div id="reportName-error" class="error-message"></div>
        </div>  
      </c:if>
    </mjl:component>
    <div class="field-row clearfix">
      <label for="design">* ${item.designMd.displayLabel}</label>
      <mjl:input param="design" type="file" id="design" />
      <div id="design-error" class="error-message"></div>
    </div>      
  </section>
</fieldset>
