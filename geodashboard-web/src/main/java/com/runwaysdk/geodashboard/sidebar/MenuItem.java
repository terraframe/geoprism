package com.runwaysdk.geodashboard.sidebar;

import java.util.ArrayList;
import java.util.List;

public class MenuItem
{
  private String name;
  private String url;
  private ArrayList<MenuItem> children = new ArrayList<MenuItem>();
  
  public MenuItem(String name, String URL) {
    this.name = name;
    this.url = URL;
  }
  
  public void addChild(MenuItem child) {
    this.children.add(child);
  }
  
  public void addChildren(ArrayList<MenuItem> children) {
    this.children.addAll(children);
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
