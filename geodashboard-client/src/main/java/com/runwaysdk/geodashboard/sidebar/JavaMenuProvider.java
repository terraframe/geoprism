package com.runwaysdk.geodashboard.sidebar;

import java.util.ArrayList;
import java.util.List;

public class JavaMenuProvider
{

  public List<MenuItem> getMenu()
  {
    ArrayList<MenuItem> items = new ArrayList<MenuItem>();

    String jspDir = "com/runwaysdk/geodashboard/jsp/";

    MenuItem accManage = new MenuItem("Account Management", null, null);
    accManage.addChild(new MenuItem("User Accounts", jspDir + "useraccounts.jsp", null));
    accManage.addChild(new MenuItem("Roles", jspDir + "roles.jsp", null));
    items.add(accManage);

    MenuItem dataType = new MenuItem("DataType Management", null, null);
    dataType.addChild(new MenuItem("Term Ontology Administration", jspDir + "termAdmin.jsp", null));
    dataType.addChild(new MenuItem("Data Browser", jspDir + "dataBrowser.jsp", null));
    items.add(dataType);

    MenuItem importer = new MenuItem("Sales Force Import Manager", jspDir + "importer.jsp", null);
    items.add(importer);

    return items;
  }

}
