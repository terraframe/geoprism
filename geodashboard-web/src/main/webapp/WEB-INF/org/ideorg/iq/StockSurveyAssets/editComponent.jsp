<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Stock Survey Assets"/>
<dl>
  <mjl:form id="org.ideorg.iq.StockSurveyAssets.form.id" name="org.ideorg.iq.StockSurveyAssets.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="org.ideorg.iq.StockSurveyAssets.form.update.button" value="Update" action="org.ideorg.iq.StockSurveyAssetsController.update.mojo" />
    <mjl:command name="org.ideorg.iq.StockSurveyAssets.form.delete.button" value="Delete" action="org.ideorg.iq.StockSurveyAssetsController.delete.mojo" />
    <mjl:command name="org.ideorg.iq.StockSurveyAssets.form.cancel.button" value="Cancel" action="org.ideorg.iq.StockSurveyAssetsController.cancel.mojo" />
  </mjl:form>
</dl>
