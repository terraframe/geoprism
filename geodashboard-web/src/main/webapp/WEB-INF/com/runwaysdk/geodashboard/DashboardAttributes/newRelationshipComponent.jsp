<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Select Dashboard Attributes Participants"/>
<mjl:form id="com.runwaysdk.geodashboard.DashboardAttributes.form.id" name="com.runwaysdk.geodashboard.DashboardAttributes.form.name" method="POST">
  <dl>
    <dt>
      <label>
        Dashboard Wrapper
      </label>
    </dt>
    <dd>
      <mjl:select param="parentId" items="${parentList}" var="current" valueAttribute="id">
        <mjl:option>
          ${current.keyName}
        </mjl:option>
      </mjl:select>
    </dd>
    <dt>
      <label>
        Attribute Wrapper
      </label>
    </dt>
    <dd>
      <mjl:select param="childId" items="${childList}" var="current" valueAttribute="id">
        <mjl:option>
          ${current.keyName}
        </mjl:option>
      </mjl:select>
    </dd>
    <mjl:command name="com.runwaysdk.geodashboard.DashboardAttributes.form.newInstance.button" value="New Instance" action="com.runwaysdk.geodashboard.DashboardAttributesController.newInstance.mojo" />
  </dl>
</mjl:form>
