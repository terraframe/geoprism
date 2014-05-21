<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Export GeoEntities"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.GeoEntityExportMenu.form.id" name="com.runwaysdk.geodashboard.gis.GeoEntityExportMenu.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="fileFormat">
        <ul>
          <c:forEach items="${item.fileFormatEnumNames}" var="enumName">
            <li>
              ${item.fileFormatMd.enumItems[enumName]}
            </li>
          </c:forEach>
        </ul>
      </mjl:dt>
      <mjl:dt attribute="includeGIS">
        ${item.includeGIS ? item.includeGISMd.positiveDisplayLabel : item.includeGISMd.negativeDisplayLabel}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.GeoEntityExportMenu.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.GeoEntityExportMenu.viewAll.link" action="com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.viewAll.mojo">
  View All
</mjl:commandLink>
