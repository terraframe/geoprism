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
<!-- Tell Runway what the application context path is. -->
<script>
window.com = window.com || {};
window.com.runwaysdk = window.com.runwaysdk || {};
window.com.runwaysdk.__applicationContextPath = "<%=request.getContextPath()%>";
</script>

<body>
  <noscript><div>Javascript must be enabled for the correct page display</div></noscript>
<!-- allow a user to go to the main content of the page -->
  <div class="skip">
    <a accesskey="N" tabindex="1" href="#main">Skip to Content</a>
  </div>
  <!-- main container of all the page elements -->
  <div id="wrapper">
    <section id="main">
