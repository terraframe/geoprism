package com.runwaysdk.geodashboard.menu;

import java.util.ArrayList;
import java.util.List;

public class JSONFileMenuProvider
{

  public List<MenuItem> getMenuItems()
  {
    ArrayList<MenuItem> items = new ArrayList<MenuItem>();
    
    MenuItem accManage = new MenuItem("Account Management", null);
    accManage.addChild(new MenuItem("User Accounts", "com/runwaysdk/geodashboard/jsp/account.jsp"));
    accManage.addChild(new MenuItem("Roles", "#"));
    items.add(accManage);
    
    MenuItem dataType = new MenuItem("DataType Management", null);
    dataType.addChild(new MenuItem("Term Ontology Administration", "#"));
    dataType.addChild(new MenuItem("Data Browser", "#"));
    items.add(dataType);
    
    MenuItem importer = new MenuItem("Sales Force Import Manager", "#");
    items.add(importer);
    
    return items;
  }
  
}
