<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Stock Survey Assets"/>
<dl>
  <mjl:form id="org.ideorg.iq.StockSurveyAssets.form.id" name="org.ideorg.iq.StockSurveyAssets.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="assetType">
        ${item.assetType.keyName}
      </mjl:dt>
      <mjl:dt attribute="paid">
        ${item.paid}
      </mjl:dt>
      <mjl:dt attribute="survey">
        ${item.survey.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.StockSurveyAssets.form.edit.button" value="Edit" action="org.ideorg.iq.StockSurveyAssetsController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.StockSurveyAssets.viewAll.link" action="org.ideorg.iq.StockSurveyAssetsController.viewAll.mojo">
  View All
</mjl:commandLink>
