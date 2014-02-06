package com.runwaysdk.geodashboard.sidebar;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.configuration.ConfigurationManager;

public class JSONMenuProvider
{
  public ArrayList<MenuItem> getMenu() {
    try
    {
      String json = FileUtils.readFileToString(new File(ConfigurationManager.getResource(ConfigurationManager.ConfigGroup.ROOT, "geodashboard/sidebar.txt").getPath()));
      return this.toMenu(new JSONArray(json));
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public ArrayList<MenuItem> toMenu(JSONArray array) throws JSONException {
    ArrayList<MenuItem> menu = new ArrayList<MenuItem>();
    
    for (int i = 0; i < array.length(); ++i) {
      JSONObject item = array.getJSONObject(i);
      
      String uri = null;
      if (item.has("uri")) {
        uri = item.getString("uri");
      }
      
      MenuItem parent = new MenuItem(item.getString("name"), uri);
      menu.add(parent);
      
      if (item.has("children")) {
        JSONArray children = item.getJSONArray("children");
        parent.addChildren(this.toMenu(children));
      }
    }
    
    return menu;
  }
}
