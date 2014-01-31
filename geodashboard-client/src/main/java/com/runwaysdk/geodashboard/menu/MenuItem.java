package com.runwaysdk.geodashboard.menu;

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
