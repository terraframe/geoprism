<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Super type"/>
<dl>
  <mjl:form id="com.test.SuperType.form.id" name="com.test.SuperType.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="testCharacter">
        ${item.testCharacter}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.test.SuperType.form.edit.button" value="Edit" action="com.test.SuperTypeController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.test.SuperType.viewAll.link" action="com.test.SuperTypeController.viewAll.mojo">
  View All
</mjl:commandLink>
