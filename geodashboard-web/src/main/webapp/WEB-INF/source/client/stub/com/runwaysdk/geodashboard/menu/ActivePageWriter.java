package com.runwaysdk.geodashboard.menu;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import com.runwaysdk.constants.DeployProperties;

public class ActivePageWriter
{
  String uri;
  JspWriter out;
  String webappRoot;
  
  public ActivePageWriter(HttpServletRequest request, JspWriter out) {
    this.uri = request.getRequestURI();
    this.out = out;
    this.webappRoot = "/" + DeployProperties.getAppName() + "/";
//    this.pageName = uri.substring(uri.lastIndexOf("/")+1);
  }
  
  public boolean isActive(MenuItem menuItem) {
    if (menuItem.getURL() == null) { // Composite
      List<MenuItem> children = menuItem.getChildren();
      for (MenuItem child : children) {
        if (this.isActive(child)) {
          return true;
        }
      }
      return false;
    }
    
    return this.uri.equals(this.webappRoot + menuItem.getURL());
  }
  
  public String getActiveClass(String name) {
    if (this.uri.equals(this.webappRoot + name)) {
      return "class=\"blueactive\"";
    }
    else {
      return "";
    }
  }
  
  public void writeLiA(String url, String title) throws IOException {
    String href = "";
    
    if (url.equals("#")) {
      href = "#";
    }
    else {
      href = webappRoot + url;
    }
    
    String html = "<li><a " + this.getActiveClass(url) + " href=\"" + href + "\">";
    
    html = html + title;
    
    html = html + "</a></li>";
    
    out.print(html);
  }
}
