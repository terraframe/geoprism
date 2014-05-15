<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Aggregation Type"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.gis.persist.AggregationType.form.id" name="com.runwaysdk.geodashboard.gis.persist.AggregationType.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.AggregationType.form.update.button" value="Update" action="com.runwaysdk.geodashboard.gis.persist.AggregationTypeController.update.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.AggregationType.form.delete.button" value="Delete" action="com.runwaysdk.geodashboard.gis.persist.AggregationTypeController.delete.mojo" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.AggregationType.form.cancel.button" value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.AggregationTypeController.cancel.mojo" />
  </mjl:form>
</dl>
