<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Select Consults For Participants"/>
<mjl:form id="org.ideorg.iq.WorksFor.form.id" name="org.ideorg.iq.WorksFor.form.name" method="POST">
  <dl>
    <dt>
      <label>
        Enterprise
      </label>
    </dt>
    <dd>
      <mjl:select param="parentId" items="${parentList}" var="current" valueAttribute="id">
        <mjl:option>
          ${current.keyName}
        </mjl:option>
      </mjl:select>
    </dd>
    <dt>
      <label>
        Business Agent
      </label>
    </dt>
    <dd>
      <mjl:select param="childId" items="${childList}" var="current" valueAttribute="id">
        <mjl:option>
          ${current.keyName}
        </mjl:option>
      </mjl:select>
    </dd>
    <mjl:command name="org.ideorg.iq.WorksFor.form.newInstance.button" value="New Instance" action="org.ideorg.iq.WorksForController.newInstance.mojo" />
  </dl>
</mjl:form>
