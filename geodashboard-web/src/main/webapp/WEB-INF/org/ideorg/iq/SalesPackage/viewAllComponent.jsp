<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all Sales Package"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="org.ideorg.iq.SalesPackageController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="cost">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="idePackage">
      <mjl:row>
        ${item.idePackage.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="org.ideorg.iq.SalesPackageController.view.mojo">
          View
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
<mjl:commandLink name="SalesPackageController.newInstance" action="org.ideorg.iq.SalesPackageController.newInstance.mojo">
  Create a new Sales Package
</mjl:commandLink>
