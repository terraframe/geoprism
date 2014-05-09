<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>

<gdb:localize var="page_title" key="universal.createTitle"/>

  <mjl:form classes="submit-form" id="com.runwaysdk.system.gis.geo.Universal.form.id" name="com.runwaysdk.system.gis.geo.Universal.form.name" method="POST">
    <%@include file="form.jsp" %>
    <!--
    <mjl:command name="com.runwaysdk.system.gis.geo.Universal.form.create.button" value="Create" action="com.runwaysdk.system.gis.geo.UniversalController.create.mojo" />
    -->
  </mjl:form>
