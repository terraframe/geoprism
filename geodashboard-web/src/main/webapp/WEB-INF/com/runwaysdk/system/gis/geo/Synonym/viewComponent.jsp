<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Synonym"/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.Synonym.form.id" name="com.runwaysdk.system.gis.geo.Synonym.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="displayLabel">
        ${item.displayLabel}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.system.gis.geo.Synonym.form.edit.button" value="Edit" action="com.runwaysdk.system.gis.geo.SynonymController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
</dl>
<mjl:commandLink name="com.runwaysdk.system.gis.geo.Synonym.viewAll.link" action="com.runwaysdk.system.gis.geo.SynonymController.viewAll.mojo">
  View All
</mjl:commandLink>
