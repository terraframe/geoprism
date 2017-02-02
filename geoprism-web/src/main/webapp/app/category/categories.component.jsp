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

<div id="app-container" class="container">

  <error-message ></error-message>  

  <h2> <gdb:localize key="category.management.title"/> </h2>
  
  <div class="list-table-wrapper">
    <table id="manage-categories-table" class="list-table table table-bordered table-striped">        
      <tbody>
        <tr *ngFor="let category of categories" class="fade-ngRepeat-item">
          <td class="button-column">
            <a class="fa fa-tasks ico-edit" (click)="edit(category)" title="<gdb:localize key="category.management.editTooltip"/>"></a>                             
            <a class="fa fa-trash-o ico-remove" (click)="remove(category)" title="<gdb:localize key="category.management.removeTooltip"/>"></a>                       
          </td>
          <td class="label-column"> {{category.label}} </td>
        </tr>
        <tr>
          <td class="button-column">
            <a class="fa fa-plus" *ngIf="!instance.active" (click)="newInstance()" title="<gdb:localize key="category.management.createCategoryOptionTooltip"/>"></a>
          </td>                 
          <td class="submit-form" *ngIf="instance.active">
            <form #form="ngForm" novalidate (ngSubmit)="form.valid && create()">
              <input class="list-table-input" type="text" [(ngModel)]="instance.label" name="descendant-label" (keyup.esc)="cancel()" required />
            </form>                                
          </td>                
        </tr>        
      </tbody>    
    </table>
   </div>  
</div>
