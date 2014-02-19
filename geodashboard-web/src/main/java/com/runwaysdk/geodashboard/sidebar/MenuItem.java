package com.runwaysdk.geodashboard.sidebar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.runwaysdk.controller.URLConfigurationManager;
import com.runwaysdk.controller.URLConfigurationManager.UriForwardMapping;
import com.runwaysdk.controller.URLConfigurationManager.UriMapping;

public class MenuItem
{
  private String name;
  private String url;
  private String mappedUrl;
  private ArrayList<MenuItem> children = new ArrayList<MenuItem>();
  
  private static URLConfigurationManager mapper = new URLConfigurationManager();
  
  public MenuItem(String name, String URL) {
    this.name = name;
    this.url = URL;
    
    if (URL != null) {
      UriMapping mapping = mapper.getMapping(URL);
      if (mapping != null && mapping instanceof UriForwardMapping) {
        this.mappedUrl = ( (UriForwardMapping) mapping ).getUriEnd();
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
  
  @Override
  public String toString() {
    String out = "MenuItem: " + this.getName();
    if (this.getURL() != null) {
     out = out + this.getURL();
    }
    
    Iterator<MenuItem> it = this.getChildren().iterator();
    while (it.hasNext()) {
      out = out + it.next().toString() + "\n";
    }
    
    return out;
  }
}
