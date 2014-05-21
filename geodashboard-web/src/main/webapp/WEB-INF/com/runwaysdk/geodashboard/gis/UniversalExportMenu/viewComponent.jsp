<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Export Universals"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.UniversalExportMenu.form.id" name="com.runwaysdk.geodashboard.gis.UniversalExportMenu.form.name" method="POST">
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
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.gis.UniversalExportMenu.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.gis.UniversalExportMenuController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.gis.UniversalExportMenu.viewAll.link" action="com.runwaysdk.geodashboard.gis.UniversalExportMenuController.viewAll.mojo">
  View All
</mjl:commandLink>
