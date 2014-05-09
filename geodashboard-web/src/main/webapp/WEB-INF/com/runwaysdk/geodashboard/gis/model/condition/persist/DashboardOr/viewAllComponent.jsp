<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Or"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardOrController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="leftCondition">
      <mjl:row>
        ${item.leftCondition.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="rightCondition">
      <mjl:row>
        ${item.rightCondition.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="styleReference">
      <mjl:row>
        ${item.styleReference.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardOrController.view.mojo">
          View
          <mjl:property name="id" value="${item.id}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
  </mjl:columns>
  <mjl:pagination>
    <mjl:page />
  </mjl:pagination>
</mjl:table>
<br />
<mjl:commandLink name="DashboardOrController.newInstance" action="com.runwaysdk.geodashboard.gis.model.condition.persist.DashboardOrController.newInstance.mojo">
  Create a new Or
</mjl:commandLink>
