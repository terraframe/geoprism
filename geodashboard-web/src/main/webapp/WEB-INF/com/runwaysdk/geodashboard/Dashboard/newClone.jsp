<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>

<gdb:localize var="cloneLabel" key="dashboard.clone.label"/>
<gdb:localize var="dashboardLabel" key="dashboard.label"/>

<div id="clone-dialog" class="modal-content" title="${cloneLabel} ${dashboard.displayLabel}">
  <form class="submit-form clone-form" action="#">
    <input id="clone-dashboard-id" type="hidden" value="${dashboard.id}">
    <fieldset>
      <section class="form-container">
        <div class="field-row clearfix">
          <label for="clone-label">${dashboardLabel}</label>
          <input id="clone-label" type="text" placeholder="${dashboardLabel}">
        </div>
      </section>
    </fieldset>
  </form>
</div>


