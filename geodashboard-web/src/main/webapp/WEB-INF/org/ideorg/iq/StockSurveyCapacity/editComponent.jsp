<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Stock Survey Capacity"/>
<dl>
  <mjl:form id="org.ideorg.iq.StockSurveyCapacity.form.id" name="org.ideorg.iq.StockSurveyCapacity.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.StockSurveyCapacity.form.update.button" value="Update" action="org.ideorg.iq.StockSurveyCapacityController.update.mojo" />
    <mjl:command name="org.ideorg.iq.StockSurveyCapacity.form.delete.button" value="Delete" action="org.ideorg.iq.StockSurveyCapacityController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.StockSurveyCapacity.form.cancel.button" value="Cancel" action="org.ideorg.iq.StockSurveyCapacityController.cancel.mojo" />
  </mjl:form>
</dl>
