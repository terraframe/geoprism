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

<form #form="ngForm" name="form" class="modal-form">
  <div>
    <fieldset>
      <section class="form-container">



<div>
  <div class="label-holder">
    <strong><localize key="dataUploader.uploadBeginningMessageRequirements"></localize></strong>
  </div>
  <div class="holder">
    <div class="row-holder">
      <p><localize key="dataUploader.uploadBeginningRequirementsMessage"></localize></p>
      <ol>
        <li><localize key="dataUploader.uploadBeginningRequirement1"></localize></li>
        <li><localize key="dataUploader.uploadBeginningRequirement2"></localize></li>
        <li><localize key="dataUploader.uploadBeginningRequirement3"></localize></li>
        <li><localize key="dataUploader.uploadBeginningRequirement4"></localize></li>
        <li><localize key="dataUploader.uploadBeginningRequirement5"></localize></li>
        <li><localize key="dataUploader.uploadBeginningRequirement6"></localize></li>
        <li><localize key="dataUploader.uploadBeginningRequirement7"></localize></li>
        <li><localize key="dataUploader.uploadBeginningRequirement8"></localize></li>
        <li><localize key="dataUploader.uploadBeginningRequirement9"></localize></li>
    </ol>
    </div>  
  </div>
  
  <div class="label-holder">
    <strong><localize key="dataUploader.uploadBeginningExampleLabel"></localize></strong>
  </div>
  <div class="holder">
    <div class="row-holder">
      <table class="table table-bordered">
        <thead>
          <tr>
            <th><localize key="dataUploader.uploadBeginningSpreadsheetExampleHeader1"></localize></th>
            <th><localize key="dataUploader.uploadBeginningSpreadsheetExampleHeader2"></localize></th>
            <th><localize key="dataUploader.uploadBeginningSpreadsheetExampleHeader3"></localize></th>
            <th><localize key="dataUploader.uploadBeginningSpreadsheetExampleHeader4"></localize></th>
            <th><localize key="dataUploader.uploadBeginningSpreadsheetExampleHeader5"></localize></th>
            <th><localize key="dataUploader.uploadBeginningSpreadsheetExampleHeader6"></localize></th>
            <th><localize key="dataUploader.uploadBeginningSpreadsheetExampleHeader7"></localize></th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent1_1"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent1_2"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent1_3"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent1_4"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent1_5"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent1_6"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent1_7"></localize></td>
          </tr>
          <tr>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent2_1"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent2_2"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent2_3"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent2_4"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent2_5"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent2_6"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent2_7"></localize></td>
          </tr>
          <tr>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent3_1"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent3_2"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent3_3"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent3_4"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent3_5"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent3_6"></localize></td>
            <td><localize key="dataUploader.uploadBeginningSpreadsheetExampleContent3_7"></localize></td>
          </tr>
        </tbody>
      </table>
    </div>  
  </div> 
</div>


      </section>        
    </fieldset>        
    
    <paging [form]="form" [page]="page"></paging>
  </div>
</form>