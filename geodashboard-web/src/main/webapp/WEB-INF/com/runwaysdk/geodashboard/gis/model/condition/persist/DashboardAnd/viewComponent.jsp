<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a And"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAnd.form.id" name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAnd.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="leftCondition">
        ${item.leftCondition.keyName}
      </mjl:dt>
      <mjl:dt attribute="rightCondition">
        ${item.rightCondition.keyName}
      </mjl:dt>
      <mjl:dt attribute="styleReference">
        ${item.styleReference.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAnd.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAndController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAnd.viewAll.link" action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardAndController.viewAll.mojo">
  View All
</mjl:commandLink>
