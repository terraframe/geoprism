<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a "/>
<dl>
  <mjl:form id="com.test.geodashboard.StateInfo.form.id" name="com.test.geodashboard.StateInfo.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
    </mjl:component>
    <mjl:command name="com.test.geodashboard.StateInfo.form.edit.button" value="Edit" action="com.test.geodashboard.StateInfoController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.test.geodashboard.StateInfo.viewAll.link" action="com.test.geodashboard.StateInfoController.viewAll.mojo">
  View All
</mjl:commandLink>
