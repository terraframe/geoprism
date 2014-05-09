<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>

<gdb:localize var="page_title" key="universal.editTitle"/>
  <mjl:form classes="submit-form" id="com.runwaysdk.system.gis.geo.Universal.form.id" name="com.runwaysdk.system.gis.geo.Universal.form.name" method="POST">
    <%@include file="form.jsp" %>
    <!--
    <mjl:command name="com.runwaysdk.system.gis.geo.Universal.form.update.button" value="Update" action="com.runwaysdk.system.gis.geo.UniversalController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.Universal.form.delete.button" value="Delete" action="com.runwaysdk.system.gis.geo.UniversalController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.Universal.form.cancel.button" value="Cancel" action="com.runwaysdk.system.gis.geo.UniversalController.cancel.mojo" />
    -->
  </mjl:form>
