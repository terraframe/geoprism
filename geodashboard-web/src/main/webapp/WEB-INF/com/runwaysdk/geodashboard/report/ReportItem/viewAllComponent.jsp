<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<gdb:localize var="page_title" key="report.viewAllTitle"/>

<mjl:table var="item" query="${query}">
  <mjl:context action="com.runwaysdk.geodashboard.report.ReportItemController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="cacheDocument">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="design">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="document">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="outputFormat">
      <mjl:row>
        <ul>
          <c:forEach items="${item.outputFormatEnumNames}" var="enumName">
            <li>
              ${item.outputFormatMd.enumItems[enumName]}
            </li>
          </c:forEach>
        </ul>
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="outputFormatIndex">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="reportLabel">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="reportName">
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="com.runwaysdk.geodashboard.report.ReportItemController.view.mojo">
          <gdb:localize key="view"/>
          <mjl:property name="id" value="${item.id}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
  </mjl:columns>
  <mjl:pagination>
    <mjl:page />
  </mjl:pagination>
</mjl:table>
<br />
<mjl:commandLink name="ReportItemController.newInstance" action="com.runwaysdk.geodashboard.report.ReportItemController.newInstance.mojo">
  <gdb:localize key="report.createLink"/>
</mjl:commandLink>
