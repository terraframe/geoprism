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
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="net.geoprism.localization.LocalizationFacadeDTO"%>
<%@page import="net.geoprism.GeodashboardUserDTO"%>
<%@page import="java.util.List"%>
<%@page import="com.runwaysdk.web.WebClientSession"%>
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.geodashboard.sidebar.XMLMenuProvider"%>
<%@page import="com.runwaysdk.geodashboard.sidebar.MenuItem"%>
<%@page import="com.runwaysdk.geodashboard.sidebar.ActivePageWriter"%>

<%@page import="com.runwaysdk.constants.DeployProperties"%>
<%
//   String webappRoot = request.getContextPath() + "/";

  ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);

  ActivePageWriter writer = new ActivePageWriter(request, out);
  XMLMenuProvider provider = new XMLMenuProvider();
%>

<!-- BEGIN Generated Bootstrap Sidebar Menu BEGIN -->
<aside id="sidebar">
  <!-- Account Info -->
  <div class="widget">
    <h3>
<% 
    try {
      out.print(((WebClientSession)session.getAttribute(ClientConstants.CLIENTSESSION)).getRequest().getSessionUser().getValue("username"));
    }
    catch (Throwable t) {
      out.print("Anonymous");
    }
%>
    </h3>
    <ul class="links-list">
<% 
      List<MenuItem> links = provider.getLinks();

      for (MenuItem item : links)
      { 
        if(item.hasAccess(clientRequest))
        {
          writer.writeLiA(LocalizationFacadeDTO.getFromBundles(clientRequest, item.getName()), item.getURL(), item.getClasses(), item.isSynch());  
        }
      }
%>
    </ul>
  </div>
  <!-- Generated from MenuItems List -->
  <nav class="aside-nav">
    <ul>
<% 
      List<MenuItem> items = provider.getMenu();
      
      int num = 100;
      
      for (MenuItem item : items) {
        
        if(item.hasAccess(clientRequest))
        {
          if (item.hasChildren())
          {
            String rootName = LocalizationFacadeDTO.getFromBundles(clientRequest, item.getName());

            out.print("<li><a data-toggle=\"collapse\" id=\"expander" + num + "\" class=\"gdb-links-expander\" href=\"#collapse" + num + "\">" + rootName + "</a>");
            out.print("<div id=\"collapse" + num + "\" class=\"panel-collapse gdb-link-container ");
              
            if (item.handlesUri(request.getRequestURI(), request.getContextPath()))
            {
              out.print("in");
            }
            else
            {
              out.print("collapse");
            }
            out.print("\">");
              
            out.print("<ul>");
              
            List<MenuItem> children = item.getChildren();
            for (MenuItem child : children)
            {
              String childName = LocalizationFacadeDTO.getFromBundles(clientRequest, child.getName());            
              writer.writeLiA(childName, child.getURL(), false);
            }
              
            out.print("</ul>");
            out.print("</div>");
            out.print("</li>");              
          }
          else
          {              
              String menuName = LocalizationFacadeDTO.getFromBundles(clientRequest, item.getName());
              writer.writeLiA(menuName, item.getURL(), false);            
          }
        }
        
        num++;
      }
      %>
    </ul>
  </nav>

  <!-- element with tooltip -->
  <a class="btn-tooltip" data-placement="top" data-toggle="tooltip"
    data-original-title="New map layer" href="#">tooltip</a>
</aside>
<!-- END Generated Bootstrap Sidebar Menu END -->


<script type="text/javascript">
  activeLink = null;

  function activateLinks(clickedLink) {
      if (clickedLink != null && activeLink != null && clickedLink[0] === activeLink[0]) {
        return;
      }
      activeLink = clickedLink;
  
    // deactivate any active links to start fresh  
    clearLinks();
    clearBusySpinner()
  
    if (clickedLink.hasClass("gdb-links-expander")) {
      if (clickedLink.next(".gdb-link-container") && window.location.hash) {
        $(".gdb-link-container a").each(function() {
          if ($(this).attr("href") === window.location.pathname + window.location.hash) {
            $(this).addClass("link-active");
            clearBusySpinner()
          }
        });
      }
    } else {
      clickedLink.addClass("link-active");  
      $("section#main").append("<div class=\"gdb-maincontent-busy\"></div>");
    }
  
    var thisParentContainer = clickedLink.parents(".gdb-link-container");
  
    if (thisParentContainer) {
      // expand the dropdown if not expanded already
      if (!thisParentContainer.hasClass("in")) {
        thisParentContainer.addClass("in");
      }
    }
  }

  function clearLinks() {
    $("a.link-active").each(function() {
      $(this).removeClass("link-active");
    });
  }

  function clearBusySpinner() {
    $(".gdb-maincontent-busy").each(function() {
        $(this).remove();
    });
  }

  function activateLinksOnLoad() {
    // check for hash because only the home page has no hash
    // if hash exists set the active link relative to the current
    if (window.location.hash) {
      $("a").each(function() {
        if ($(this).attr("href") === window.location.pathname + window.location.hash) {
          activateLinks($(this));
        }
      });
    }
  }

  activateLinksOnLoad();
  
  // Keep the element styled like the hover when dropdown is expanded
  $("a").click(function() {
    activateLinks($(this));
  });
</script>
