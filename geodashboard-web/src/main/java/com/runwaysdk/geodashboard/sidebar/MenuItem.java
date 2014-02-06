package com.runwaysdk.geodashboard.sidebar;

import java.util.ArrayList;
import java.util.List;

import com.runwaysdk.controller.XMLServletRequestMapper;
import com.runwaysdk.controller.XMLServletRequestMapper.RedirectMapping;

public class MenuItem
{
  private String name;
  private String url;
  private String mappedUrl;
  private ArrayList<MenuItem> children = new ArrayList<MenuItem>();
  
  private static XMLServletRequestMapper mapper = new XMLServletRequestMapper();
  
  public MenuItem(String name, String URL) {
    this.name = name;
    this.url = URL;
    
    if (URL != null) {
      RedirectMapping map = mapper.getRedirectMapping(URL);
      if (map != null) {
        this.mappedUrl = map.getUriEnd();
      }
    }
  }
  
  public void addChild(MenuItem child) {
    this.children.add(child);
  }
  
  public void addChildren(ArrayList<MenuItem> children) {
    this.children.addAll(children);
  }
  
  public boolean handlesUri(String uri, String context) {
    // If we have children loop over them.
    if (this.getURL() == null) {
      List<MenuItem> children = this.getChildren();
      for (MenuItem child : children) {
        if (child.handlesUri(uri, context)) {
          return true;
        }
      }
      return false;
    }
    
    if (this.mappedUrl != null) {
      return uri.equals(context + this.mappedUrl);
    }
    else {
      return uri.equals(context + this.getURL());
    }
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getURL() {
    return this.url;
  }
  
  public boolean hasChildren() {
    return !this.children.isEmpty();
  }
  
  public List<MenuItem> getChildren() {
    return this.children;
  }
}
