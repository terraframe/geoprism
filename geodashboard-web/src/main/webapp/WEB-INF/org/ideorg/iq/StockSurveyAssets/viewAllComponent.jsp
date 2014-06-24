<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Stock Survey Assets"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.StockSurveyAssetsController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="assetType">
      <mjl:row>
        ${item.assetType.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="paid">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="survey">
      <mjl:row>
        ${item.survey.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="org.ideorg.iq.StockSurveyAssetsController.view.mojo">
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
<mjl:commandLink name="StockSurveyAssetsController.newInstance" action="org.ideorg.iq.StockSurveyAssetsController.newInstance.mojo">
  Create a new Stock Survey Assets
</mjl:commandLink>
