<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Stock Survey Inventory"/>
<dl>
  <mjl:form id="org.ideorg.iq.StockSurveyInventory.form.id" name="org.ideorg.iq.StockSurveyInventory.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.StockSurveyInventory.form.update.button" value="Update" action="org.ideorg.iq.StockSurveyInventoryController.update.mojo" />
    <mjl:command name="org.ideorg.iq.StockSurveyInventory.form.delete.button" value="Delete" action="org.ideorg.iq.StockSurveyInventoryController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.StockSurveyInventory.form.cancel.button" value="Cancel" action="org.ideorg.iq.StockSurveyInventoryController.cancel.mojo" />
  </mjl:form>
</dl>
