<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all GeoEntity"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="com.runwaysdk.system.gis.geo.GeoEntityController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="entityName">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="geoId">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="geoMultiPolygon">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="geoPoint">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="universal">
      <mjl:row>
        ${item.universal.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="wkt">
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="com.runwaysdk.system.gis.geo.GeoEntityController.view.mojo">
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
<mjl:commandLink name="GeoEntityController.newInstance" action="com.runwaysdk.system.gis.geo.GeoEntityController.newInstance.mojo">
  Create a new GeoEntity
</mjl:commandLink>
