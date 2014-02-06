package com.runwaysdk.geodashboard.sidebar;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import com.runwaysdk.controller.XMLServletRequestMapper;

public class ActivePageWriter
{
  String uri;
  JspWriter out;
  String context;
  
  // If we've already redirected to a jsp, then we need to 
  XMLServletRequestMapper mapper;
  
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
  
  public void writeLiA(MenuItem item) throws IOException {
    String href = "";
    String url = item.getURL();
    String title = item.getName();
    
    if (url.equals("#")) {
      href = "#";
    }
    else {
      href = context + "/" + url;
    }
    
    String html = "<li><a " + this.getActiveClass(item) + " href=\"" + href + "\">";
    
    html = html + title;
    
    html = html + "</a></li>";
    
    out.print(html);
  }
  
  public void writeLiA(String title, String url) throws IOException {
    this.writeLiA(new MenuItem(title, url));
  }
}
