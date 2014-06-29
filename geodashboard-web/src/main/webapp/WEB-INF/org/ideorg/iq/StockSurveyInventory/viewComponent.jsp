<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Stock Survey Inventory"/>
<dl>
  <mjl:form id="org.ideorg.iq.StockSurveyInventory.form.id" name="org.ideorg.iq.StockSurveyInventory.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="inventoryType">
        ${item.inventoryType.keyName}
      </mjl:dt>
      <mjl:dt attribute="quantity">
        ${item.quantity}
      </mjl:dt>
      <mjl:dt attribute="survey">
        ${item.survey.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.StockSurveyInventory.form.edit.button" value="Edit" action="org.ideorg.iq.StockSurveyInventoryController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.StockSurveyInventory.viewAll.link" action="org.ideorg.iq.StockSurveyInventoryController.viewAll.mojo">
  View All
</mjl:commandLink>
