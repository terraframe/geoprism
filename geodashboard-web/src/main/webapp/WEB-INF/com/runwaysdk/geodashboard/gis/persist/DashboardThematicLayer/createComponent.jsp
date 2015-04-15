<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="page_title" scope="request" value="Create a new Layer" />
<dl>
	<mjl:form
		id="com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.form.id"
		name="com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.form.name"
		method="POST"  classes="modal-form">
		<%@include file="form.jsp"%>

	</mjl:form>
</dl>
