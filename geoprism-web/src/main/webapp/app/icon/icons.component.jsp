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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<div id="app-container" class="container" >

  <error-message ></error-message>
  
  <div>
    <span>
      <h2> <gdb:localize key="category.icon.title"/> </h2>
    </span>
  </div>
  
  <div class="list-table-wrapper">
    <table id="manage-icons-table" class="list-table table table-bordered table-striped">        
      <thead>
        <tr>
          <th></th>
          <th class="label-column"><gdb:localize key='category.icon.label'/></th>
          <th class="label-column"><gdb:localize key='category.icon.preview'/></th>
        </tr>
      </thead>    
      <tbody>
        <tr *ngFor="let icon of icons" class="fade-ngRepeat-item">
          <td class="button-column">
            <span>
              <a class="fa fa-pencil ico-edit" (click)="edit(icon)" title="<gdb:localize key="category.icon.editTooltip"/>"></a>                     
              <a class="fa fa-trash-o ico-remove" title="<gdb:localize key="category.icon.removeTooltip"/>"
               confirm-modal 
               message="<gdb:localize key="category.icon.removeContent"/>"
               (onConfirm)="remove(icon)"></a>
            </span>
          </td>
          <td class="label-column">{{ icon.label }}</td>
          <td class="icon-thumbnail-column">
            <img *ngIf="icon.id && icon.id.length > 0" style="width:42px;height:42px;" class="thumb" [src]="'${pageContext.request.contextPath}/iconimage/getCategoryIconImage?id=' + icon.id" alt="Icon">
          </td>
        </tr>      
        <tr>
          <td class="button-column">
            <a class="fa fa-plus" (click)="add()" title="<gdb:localize key="category.icon.addTooltip"/>"></a>
          </td>                 
          <td colspan="2">
          </td>                 
        </tr>       
      </tbody>    
    </table>
  </div>
  
  <div *ngIf="icons === null"><gdb:localize key='dataset.loadingData'/></div>
  <div *ngIf="icons != null && icons.length === 0">
    <p><gdb:localize key='category.icon.emtpy'/></p>
  </div>  
</div>
