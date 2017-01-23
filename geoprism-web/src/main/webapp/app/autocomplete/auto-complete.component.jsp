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

<div class="auto-complete">
  <!-- keyword input -->
  <input #autoCompleteInput class="keyword"
         placeholder="{{placeholder}}"
         (focus)="showDropdownList()"
         (blur)="hideDropdownList()"
         (keydown)="inputElKeyHandler($event)"
         (input)="reloadListInDelay()"
         [(ngModel)]="keyword" />
         
  <!-- dropdown that user can select -->
  <ul *ngIf="dropdownVisible"
      [style.bottom]="inputEl.style.height"
      [style.position]="closeToBottom ? 'absolute': ''">
      
    <li *ngIf="isLoading" class="loading"><gdb:localize key="autocomplete.loading"/></li>
    
    <li *ngIf="minCharsEntered && !isLoading && !filteredList.length"
         (mousedown)="selectOne('')"
         class="blank-item"><gdb:localize key="autocomplete.noMatchFound"/></li>
        
    <li class="item"
        *ngFor="let item of filteredList; let i=index"
        (mousedown)="selectOne(item)"
        [ngClass]="{selected: i === itemIndex}">
      <span>{{data[this.displayPropertyName]}}</span>        
    </li>
  </ul>
</div>