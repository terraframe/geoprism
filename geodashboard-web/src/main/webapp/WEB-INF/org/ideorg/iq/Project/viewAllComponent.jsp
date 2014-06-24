<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Project"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.ProjectController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="amount">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="endPeriod">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="geoLocation">
      <mjl:row>
        ${item.geoLocation.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="name">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="startPeriod">
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="org.ideorg.iq.ProjectController.view.mojo">
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
<mjl:commandLink name="ProjectController.newInstance" action="org.ideorg.iq.ProjectController.newInstance.mojo">
  Create a new Project
</mjl:commandLink>
