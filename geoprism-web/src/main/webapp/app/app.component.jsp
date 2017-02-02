<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

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
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<div id="innerFrameHtml">
    <loading-bar></loading-bar>
    
    <!-- HEADER AND NAVBAR -->
    <header>
      <nav class="navbar navbar-default">
        <div class="container">
          <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/menu" title="<gdb:localize key="userMenu.menuTooltip"/>"><img src="${pageContext.request.contextPath}/net/geoprism/images/splash_logo_icon.png" /></a>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/prism/management"><gdb:localize key="data.management.title"/></a>
          </div>
          
          <ul class="nav navbar-nav navbar-right">
            <li dropdown>
              <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" dropdownToggle>
                <gdb:localize key="Data_Management"/><span class="caret"></span>
              </a>            
              <ul dropdownMenu class="dropdown-menu">
                <li role="menuitem"><a class="dropdown-item" routerLink="/datasets" routerLinkActive="active"><gdb:localize key="Data_Sets"/></a></li>
                <li role="menuitem"><a class="dropdown-item" routerLink="/categories" routerLinkActive="active"><i></i><gdb:localize key="Categories"/></a></li>
              </ul>
            </li>
          </ul>
        </div>
      </nav>
    </header>
        
    <!-- MAIN CONTENT AND INJECTED VIEWS -->
    <div id="main" class="new-admin-design-main">
      <router-outlet></router-outlet>
    </div>
</div>