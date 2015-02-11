<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Equals"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqualController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="comparisonValue">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="definingMdAttribute">
      <mjl:row>
        ${item.definingMdAttribute.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="parentCondition">
      <mjl:row>
        ${item.parentCondition.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="rootCondition">
      <mjl:row>
        ${item.rootCondition.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqualController.view.mojo">
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
<mjl:commandLink name="DashboardNotEqualController.newInstance" action="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqualController.newInstance.mojo">
  Create a new Equals
</mjl:commandLink>
