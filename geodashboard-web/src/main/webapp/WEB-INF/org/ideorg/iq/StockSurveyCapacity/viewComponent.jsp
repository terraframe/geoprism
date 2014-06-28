<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Stock Survey Capacity"/>
<dl>
  <mjl:form id="org.ideorg.iq.StockSurveyCapacity.form.id" name="org.ideorg.iq.StockSurveyCapacity.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="capacityType">
        ${item.capacityType.keyName}
      </mjl:dt>
      <mjl:dt attribute="quantity">
        ${item.quantity}
      </mjl:dt>
      <mjl:dt attribute="survey">
        ${item.survey.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="org.ideorg.iq.StockSurveyCapacity.form.edit.button" value="Edit" action="org.ideorg.iq.StockSurveyCapacityController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="org.ideorg.iq.StockSurveyCapacity.viewAll.link" action="org.ideorg.iq.StockSurveyCapacityController.viewAll.mojo">
  View All
</mjl:commandLink>
