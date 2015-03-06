<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<head>

<gdb:localize var="page_title" key="databrowser.title"/>

<!-- Databrowser CSS -->
<jwr:style src="/bundles/datatable.css" useRandomParam="false"/>  
<jwr:style src="/com/runwaysdk/geodashboard/databrowser/databrowser.css" useRandomParam="false"/>  

<!-- Databrowser Javascript -->
<jwr:script src="/bundles/datatable.js" useRandomParam="false"/>
<jwr:script src="/bundles/databrowser.js" useRandomParam="false"/>

<!-- Runway Generic -->
<script type="text/javascript">
<%=request.getAttribute("js")%>
</script>

</head>

<div id="databrowser"></div>

<script type="text/javascript">
  com.runwaysdk.ui.Manager.setFactory("JQuery");
  
  var db = new com.runwaysdk.geodashboard.databrowser.DataBrowser({
    types: com.runwaysdk.DTOUtil.convertToType(<%=request.getAttribute("response")%>.returnValue[0]).getResultSet()
  });
  db.render("#databrowser");
</script>
