<%--

    Copyright (c) 2013 TerraFrame, Inc. All rights reserved.

    This file is part of Runway SDK(tm).

    Runway SDK(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Runway SDK(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>

<%@page import="com.runwaysdk.constants.DeployProperties" %>
<%
  String webappRoot = "/" + DeployProperties.getAppName() + "/";
%>

<!-- Begin footer -->
    </section> 
<!-- contain sidebar of the page -->
    <aside id="sidebar">
      <!-- sidebar widget -->
      <div class="widget">
        <h3>Luke Skywalker</h3>
        <ul class="links-list">
          <li><a href="#">Log out</a></li>
          <li><a href="#">Account</a></li>
          <li><a href="#" class="link-viewer">Dashboard Viewer</a></li>
        </ul>
      </div>
      <!-- sidebar widget -->
      <div class="widget">
        <h3 class="marked">Manage Dashboards</h3>
        <ul class="links-list">
          <li><a href="#">Annual Imunization Data</a></li>
          <li class="active"><a href="#">Q4 Sales Engagment</a></li>
          <!-- slide block -->
          <li><a data-toggle="collapse" href="#collapse3">New Dashboard <span class="hidden">collapse3</span></a>
            <ul id="collapse3" class="panel-collapse collapse">
              <li><a href="#">Q4 Sales Leads</a></li>
              <li><a href="#">Regional Marketing Programs</a></li>
              <li><a href="#">New Dashboard</a></li>
            </ul>
          </li>
        </ul>
      </div>
      <nav class="aside-nav">
        <ul>
          <!-- slide block -->
          <li><a data-toggle="collapse" href="#collapse4">Acount Management</a>
            <div id="collapse4" class="panel-collapse collapse">
              <ul>
                <li><a href="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/jsp/account.jsp">User Accounts</a></li>
                <li><a href="#">Roles</a></li>
              </ul>
            </div>
          </li>
          <!-- slide block -->
          <li><a data-toggle="collapse" href="#collapse5">Datatype Management</a>
            <div id="collapse5" class="panel-collapse collapse">
              <ul>
                <li><a href="#">Term Ontology Administration</a></li>
                <li><a href="#">Data Browser</a></li>
              </ul>
            </div>
          </li>
          <li><a href="#">Sales Force Import Manager</a></li>
          <!-- slide block -->
          <li><a data-toggle="collapse" href="#collapse6">GIS Management</a>
            <div id="collapse6" class="panel-collapse collapse">
              <ul>
                <li><a href="#">Universal Management</a></li>
                <li><a href="#">Geoentity Maintanence</a></li>
              </ul>
            </div>
          </li>
        </ul>
      </nav>
      <!-- element with tooltip -->
      <a class="btn-tooltip" data-placement="top" data-toggle="tooltip" data-original-title="New map layer" href="#">tooltip</a>
    </aside>
    
  </div> <!-- end wrapper -->
  <!-- allow a user to go to the top of the page -->
  <div class="skip">
    <a href="#wrapper">Back to top</a>
  </div>
</body>
</html>
