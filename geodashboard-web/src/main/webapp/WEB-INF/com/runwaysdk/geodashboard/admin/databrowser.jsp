<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>

<head>

<gdb:localize var="page_title" key="databrowser.title"/>

<script type="text/javascript" src="${pageContext.request.contextPath}/jquerytree/tree.jquery.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/jquerytree/jqtree.css" ></link>

<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/datatables/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/jquery/datatables/css/jquery.dataTables.css" ></link>
<link rel="stylesheet" href="${pageContext.request.contextPath}/jquery/datatables/css/jquery.dataTables_themeroller.css" ></link>

<!-- Runway Factory -->
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/runway.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/widget/Widget.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/list/List.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/form/Form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/contextmenu/ContextMenu.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/button/Button.js"></script>

<!-- Generic -->
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/generic/datatable/datasource/DataSourceIF.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/generic/datatable/datasource/Events.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/generic/datatable/datasource/DataSourceFactory.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/generic/datatable/datasource/BaseServerDataSource.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/generic/datatable/datasource/ServerDataSource.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/generic/datatable/DataTable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/generic/datatable/Column.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/generic/datatable/Events.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/generic/datatable/Row.js"></script>

<!-- JQuery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/jquery/Factory.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/jquery/Dialog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/jquery/datatable/datasource/ServerDataSource.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/jquery/datatable/datasource/DataSourceFactory.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/jquery/datatable/DataTable.js"></script>

<!-- Runway Generic -->
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/databrowser/DataBrowserQueryDataSource.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/databrowser/DataBrowser.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/databrowser/databrowser.css" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/default.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/generic/datatable/DataTable.css" />

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
