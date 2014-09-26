<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="mdss" uri="/WEB-INF/tlds/mdssLib.tld" %>
<c:set scope="request" var="page_title" value="Edit_ReportItem" />
<mjl:messages>
  <mjl:message />
</mjl:messages>
<dl>
  <mjl:form id="form.id" name="form.name" method="POST"  enctype="multipart/form-data">
    <%@include file="form.jsp" %>
    <mdss:localize var="Update_Localize" key="Update" />
    <mjl:command name="dss.vector.solutions.report.ReportItem.form.update.button" value="${Update_Localize}" action="dss.vector.solutions.report.ReportItemController.update.mojo" />
    <mdss:localize var="Delete_Localize" key="Delete" />
    <mjl:command name="dss.vector.solutions.report.ReportItem.form.delete.button" value="${Delete_Localize}" action="dss.vector.solutions.report.ReportItemController.delete.mojo" />
    <mdss:localize var="Cancel_Localize" key="Cancel" />
    <mjl:command name="dss.vector.solutions.report.ReportItem.form.cancel.button" value="${Cancel_Localize}" action="dss.vector.solutions.report.ReportItemController.cancel.mojo" />
  </mjl:form>
</dl>
