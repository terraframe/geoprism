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

<ul *ngIf="list.length > 0" class="ui-front ui-menu ui-widget ui-widget-content ui-corner-all search-results" style="margin-left: 0px;">
  <li *ngFor="let item of list; let i = index" class="ui-menu-item" role="presentation" style="margin-left: 0px;">
    <a class="ui-corner-all" [ngClass]="{'search-hover' : item.selected}" tabindex="-1" (mouseenter)="onMouseEnter(item, i)" (mouseleave)="onMouseLeave(item, i)" (click)="onClick(item)">{{item.text}}</a>
  </li>
</ul>
