package com.runwaysdk.geodashboard.sidebar;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import com.runwaysdk.controller.URLConfigurationManager;

public class ActivePageWriter
{
  String uri;
  JspWriter out;
  String context;
  
  // If we've already redirected to a jsp, then we need to 
  URLConfigurationManager mapper;
  
  public ActivePageWriter(HttpServletRequest request, JspWriter out) {
    this.uri = request.getRequestURI();
    this.out = out;
    this.context = request.getContextPath();
  }
  
  public String getActiveClass(MenuItem item) {
    if (item.handlesUri(this.uri, this.context)) {
      return "class=\"blueactive\"";
    }
    else {
      return "";
    }
  }
  
  public void writeLiA(MenuItem item, String classes) throws IOException {
    String href = "";
    String url = item.getURL();
    String title = item.getName();
    
    if (url.equals("#")) {
      href = "#";
    }
    else {
      href = context + "/#" + url;
    }
    
    String clazz = "";
    if (classes != null) {
      clazz = "class=\"" + classes + "\"";
    }
    
    String html = "<li><a " + clazz + " " + this.getActiveClass(item) + " href=\"" + href + "\">";
    
    html = html + title;
    
    html = html + "</a></li>";
    
    out.print(html);
  }
  
  public void writeLiA(String title, String url) throws IOException {
    this.writeLiA(new MenuItem(title, url), null);
  }
  
  public void writeLiA(String title, String url, String classes) throws IOException {
    this.writeLiA(new MenuItem(title, url), classes);
  }
}
