package com.runwaysdk.geodashboard.test;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.gis.persist.AllLayerType;
import com.runwaysdk.geodashboard.gis.persist.DashboardLayer;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.geodashboard.gis.persist.DashboardStyle;
import com.runwaysdk.geodashboard.gis.persist.HasLayer;
import com.runwaysdk.geodashboard.gis.persist.HasStyle;
import com.runwaysdk.session.Request;

public class Sandbox
{
  public static void main(String[] args)
  {
    testBuildMap();
  }
  
  @Request
  @Transaction
  public static void testBuildMap()
  {
    System.out.println("testBuildMap");
     
    DashboardMap map = new DashboardMap();
     map.setName("Test Map");
     map.apply();
     
     DashboardLayer layer = new DashboardLayer();
     layer.setName("Layer 1");
     layer.addLayerType(AllLayerType.BUBBLE);
     layer.setDisplayInLegend(false);
     layer.setVirtual(false);
     layer.apply();
     
     HasLayer hasLayer = map.addHasLayer(layer);
     hasLayer.setLayerIndex(0);
     hasLayer.apply();

     DashboardStyle style = new DashboardStyle();
     style.setName("point");
     style.setValueSize(3);
     style.setValueFont("Arial");
     style.setValueColor("red");
     style.setValueHalo("white");
     style.setValueHaloWidth(2);
     style.apply();
     
     HasStyle hasStyle = layer.addHasStyle(style);
     hasStyle.apply();

//     String json = DashboardMap.getMapJSON(map.getId());
//     System.out.println(json);
     
     map.delete();
     
     System.out.println("all done");
     
  }
}