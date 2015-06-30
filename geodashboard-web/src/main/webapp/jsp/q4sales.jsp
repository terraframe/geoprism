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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.io.*" %>
<%@ page import="com.runwaysdk.controller.JSPFetcher"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>${page_title}</title>
 
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>

<%@page import="com.runwaysdk.constants.DeployProperties" %>
<%
  String webappRoot = request.getContextPath() + "/";
%>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/log4js.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/errorcatch.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/Util.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ClassFramework.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/Structure.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_Core.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_DTO.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_GIS.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_Inspector.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/RunwaySDK_UI.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/Geodashboard.js"></script>


  <!-- set the encoding of your site -->
  <!-- include the site stylesheet -->
  <link media="all" rel="stylesheet" href="<% out.print(webappRoot); %>bootstrap/bootstrap.min.css">
  <link rel="stylesheet" type="text/css" href="<% out.print(webappRoot); %>jquery/ui/themes/lightness.css">
  <link media="all" rel="stylesheet" href="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/css/all.css">
  <!-- include jQuery library -->
  <!--
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
  -->
  <script type="text/javascript" src="<% out.print(webappRoot); %>jquery/jquery-1.8.3.min.js"></script>
  <!-- Bootstrap must be loaded before JQuery-UI or else jquery-ui gets screwy -->
  <script type="text/javascript" src="<% out.print(webappRoot); %>bootstrap/bootstrap.min.js"></script>
  <!-- include custom JavaScript -->
  <script type="text/javascript" src="<% out.print(webappRoot); %>psd2html.jcf.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>jquery/jquery.datepicker.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>jquery/ui/js/jquery-ui.min.js"></script>
  <link rel="stylesheet" href="<% out.print(webappRoot); %>jquery/ui/themes/jquery-ui.min.css" ></link>
  <!-- include HTML5 IE enabling script for IE -->
  <!--[if IE 8]><script type="text/javascript" src="./../../../../../ie.js"></script><![endif]-->
</head>


<body>

<div class="pageContent">

<header id="header">
  <h1>Q4 Sales Engagement</h1>
</header>

<!-- submit form -->
      <form class="submit-form" action="#">
        <fieldset>
          <legend>Submit form</legend>
          <section class="form-container">
            <h2>Content group title</h2>
            <div class="field-row clearfix">
              <label for="field1">Input Field Default</label>
              <input id="field1" type="text" placeholder="Display Help Text Here">
            </div>
            <div class="field-row clearfix">
              <label for="field2">inFocus - no typing</label>
              <input id="field2" type="text" placeholder="Display Help Text Here">
            </div>
            <div class="field-row clearfix">
              <label for="field3">typing</label>
              <input id="field3" type="text" placeholder="Typing">
            </div>
            <div class="field-row clearfix field-error">
              <label for="field4">error Input Field</label>
              <input id="field4" type="text" placeholder="Typing the wrong thing">
              <div class="error-message">Display error message here</div>
            </div>
            <div class="field-row clearfix">
              <label for="field5">Inactive Input Field</label>
              <input disabled id="field5" type="text" placeholder="Display Help Text Here">
            </div>
            <div class="field-row clearfix">
              <label for="select-field">Select List</label>
              <select id="select-field">
                <option>Item one</option>
                <option>Item two</option>
                <option>Item three</option>
                <option>Item Four</option>
                <option>Item Five</option>
              </select>
            </div>
            <div class="field-row clearfix">
              <span class="label-text">Radio Button Group</span>
              <div class="checks-frame">
                <input checked id="radio1" name="radio-group" type="radio">
                <label for="radio1">option 1</label>
                <input id="radio2" name="radio-group" type="radio">
                <label for="radio2">option 2</label>
                <input id="radio3" name="radio-group" type="radio">
                <label for="radio3">option 3</label>
              </div>
            </div>
          </section>
          <section class="form-container">
            <h2>Content group title</h2>
            <div class="field-row clearfix">
              <span class="label-text">Date Picker</span>
              <!-- datapicker block -->
              <div class="data-block">
                <div class="col">
                  <label for="from-field">From</label>
                  <input class="checkin" id="from-field" type="text" placeholder="Jan 2012">
                  <a href="#" class="datapicker-opener">datapicker</a>
                </div>
                <div class="col">
                  <label for="to-field">To</label>
                  <input class="checkout" id="to-field" type="text" placeholder="Jan 2013">
                  <a href="#" class="datapicker-opener">datapicker</a>
                </div>
              </div>
              <!-- element with popover -->
              <button class="btn-popover" data-content="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eleifend scelerisque elit, convallis bibendum tortor accumsan sed." data-placement="top" data-toggle="popover" data-container="body" type="button">?</button>
            </div>
            <div class="field-row clearfix">
              <span class="label-text">Checkbox Group</span>
              <div class="checks-frame">
                <input checked id="check1" type="checkbox">
                <label for="check1">option 1</label>
                <input checked id="check2" type="checkbox">
                <label for="check2">option 2</label>
                <input id="check3" type="checkbox">
                <label for="check3">option 3</label>
              </div>
            </div>
          </section>
          <div class="collapsed-container">
            <!-- slide block -->
            <div class="collapsed-block">
              <h2><a data-toggle="collapse" href="#collapse1">Collapsed Content group title</a></h2>
              <div id="collapse1" class="panel-collapse collapse">
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eleifend scelerisque elit, convallis bibendum tortor accumsan sed.</p>
              </div>
            </div>
            <!-- slide block -->
            <div class="collapsed-block">
              <h2><a data-toggle="collapse" href="#collapse2">Collapsed Content group title <span class="hidden">collapse2</span></a></h2>
              <div id="collapse2" class="panel-collapse collapse">
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eleifend scelerisque elit, convallis bibendum tortor accumsan sed.</p>
              </div>
            </div>
          </div>
          <footer class="bottom-bar">
            <input class="btn btn-primary" type="submit" value="Save">
            <input class="btn" type="reset" value="Cancel">
            <a href="#"><img class="brand" src="images/img-brand.png" width="126" height="15" alt="Product Branding"></a>
          </footer>
        </fieldset>
      </form>

</div>

</body>

