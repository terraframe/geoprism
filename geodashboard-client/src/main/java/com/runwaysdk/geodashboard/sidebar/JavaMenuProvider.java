package com.runwaysdk.geodashboard.sidebar;

import java.util.ArrayList;
import java.util.List;

public class JavaMenuProvider
{

  public List<MenuItem> getMenu()
  {
    ArrayList<MenuItem> items = new ArrayList<MenuItem>();
    
    String jspDir = "com/runwaysdk/geodashboard/jsp/";
    
    MenuItem accManage = new MenuItem("Account Management", null);
    accManage.addChild(new MenuItem("User Accounts", jspDir + "useraccounts.jsp"));
    accManage.addChild(new MenuItem("Roles", jspDir + "roles.jsp"));
    items.add(accManage);
    
    MenuItem dataType = new MenuItem("DataType Management", null);
    dataType.addChild(new MenuItem("Term Ontology Administration", jspDir + "termAdmin.jsp"));
    dataType.addChild(new MenuItem("Data Browser", jspDir + "dataBrowser.jsp"));
    items.add(dataType);
    
    MenuItem importer = new MenuItem("Sales Force Import Manager", jspDir + "importer.jsp");
    items.add(importer);
    
    return items;
  }
  
}
