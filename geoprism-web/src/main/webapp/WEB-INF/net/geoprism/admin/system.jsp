<%--

    Copyright (c) 2022 TerraFrame, Inc. All rights reserved.

    This file is part of Geoprism(tm).

    Geoprism(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Geoprism(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>

<head>

<gdb:localize var="page_title" key="system.title"/>

<!-- User account CSS -->
<jwr:style src="/bundles/datatable.css" useRandomParam="false"/>  
<jwr:style src="/net/geoprism/userstable/UsersTable.css" useRandomParam="false"/>  

<!-- User account Javascript -->
<jwr:script src="/bundles/system.js" useRandomParam="false"/>
<script type="text/javascript">${js}</script>

<!-- These 2 forms will be moved by the System.js -->
<form enctype="multipart/form-data" method="POST" name="form.name" action="/../net.geoprism.SystemLogoSingletonController.uploadBanner.mojo" style="display: none;" id="form.banner" class="submit-form">
  <fieldset class="net-geoprism-FormList">
    <section class="form-container">
        <c:if test="${not empty bannerFilePath}" >
          <div class="field-row clearfix">
            <label for="preview"><gdb:localize key="net.geoprism.system.SystemForm.banner_uploadPreviewLabel" /></label>
            <img id="preview" style="width: 250px;" src="${bannerFilePath}" alt="Uploaded Banner" />
            <div id="preview-error" class="error-message"></div>
          </div>
        </c:if>
        <c:if test="${not empty bannerFileName}" >
          <div class="field-row clearfix">
            <label for="bannerNameLabel"><gdb:localize key="net.geoprism.system.SystemForm.banner_fileNameLabel" /></label>
            <label id="bannerName">${bannerFileName}</label>
            <div id="bannerName-error" class="error-message"></div>
          </div>
        </c:if>
      <div class="field-row clearfix">
        <label for="bannerImage">* <gdb:localize key="net.geoprism.system.SystemForm.banner_imageLabel" /></label>
        <mjl:input param="banner" type="file" id="bannerImage" />
        <div id="bannerImage-error" class="error-message"></div>
      </div>
    </section>
  </fieldset>
  <input name="net.geoprism.SystemLogoSingletonController.uploadBanner.button" id="8kgsqzqjkm1djn9esg4kiquus9aw075x" style="float:none;" class="btn btn-primary" type="submit" value="Upload">
</form>
<form enctype="multipart/form-data" method="POST" name="form.name" action="/../net.geoprism.SystemLogoSingletonController.uploadMiniLogo.mojo" style="display: none;" id="form.miniLogo" class="submit-form">
  <fieldset class="net-geoprism-FormList">
    <section class="form-container">
        <c:if test="${not empty miniLogoFilePath}" >
          <div class="field-row clearfix">
            <label for="preview"><gdb:localize key="net.geoprism.system.SystemForm.miniLogo_uploadPreviewLabel" /></label>
            <img id="preview" src="${miniLogoFilePath}" style="height: 30px;" alt="Uploaded Mini Logo" />
            <div id="preview-error" class="error-message"></div>
          </div>
        </c:if>
        <c:if test="${not empty miniLogoFileName}" >
          <div class="field-row clearfix">
            <label for="miniLogoNameLabel"><gdb:localize key="net.geoprism.system.SystemForm.miniLogo_fileNameLabel" /></label>
            <label id="miniLogoName">${miniLogoFileName}</label>
            <div id="miniLogoName-error" class="error-message"></div>
          </div>
        </c:if>
      <div class="field-row clearfix">
        <label for="miniLogoImage">* <gdb:localize key="net.geoprism.system.SystemForm.miniLogo_imageLabel" /></label>
        <mjl:input param="miniLogo" type="file" id="miniLogoImage" />
        <div id="miniLogoImage-error" class="error-message"></div>
      </div>      
    </section>
  </fieldset>
  <input name="net.geoprism.SystemLogoSingletonController.uploadBanner.button" id="8kgsqzqjkm1djn9esg4kiquus9aw075x" style="float:none;" class="btn btn-primary" type="submit" value="Upload">
</form>


</head>

<body id="body">

<div id="systemForm"></div>

<script type="text/javascript">
  com.runwaysdk.ui.Manager.setFactory("JQuery");
  
  var emailSetting = new net.geoprism.EmailSetting(${emailSetting}.returnValue[0]);
  var user = new net.geoprism.GeoprismUser(${user}.returnValue[0]);
  
  var sf = new net.geoprism.system.SystemForm(emailSetting, user);
  sf.render("#systemForm");
</script>

</body>
