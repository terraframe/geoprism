package com.runwaysdk.geodashboard.gis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.geodashboard.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.impl.LayerImpl;
import com.runwaysdk.geodashboard.gis.impl.MapImpl;
import com.runwaysdk.geodashboard.gis.impl.StyleImpl;
import com.runwaysdk.geodashboard.gis.impl.ThematicStyleImpl;
import com.runwaysdk.geodashboard.gis.impl.condition.AndImpl;
import com.runwaysdk.geodashboard.gis.impl.condition.EqualImpl;
import com.runwaysdk.geodashboard.gis.impl.condition.OrImpl;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.geodashboard.gis.model.condition.And;
import com.runwaysdk.geodashboard.gis.model.condition.Equal;
import com.runwaysdk.geodashboard.gis.model.condition.Or;
import com.runwaysdk.geodashboard.gis.sld.SLDMapVisitor;
import com.runwaysdk.geodashboard.gis.sld.WellKnownName;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.Universal;

public class GeoserverTest
{
  private static Universal city;
  
  @BeforeClass
  @Request
  public static void classSetup()
  {
    city = new Universal();
    city.getDisplayLabel().setDefaultValue("State");
    city.getDescription().setDefaultValue("State");
    city.setUniversalId("state");
    city.apply();
    
    
  }
  
  @AfterClass
  @Request
  public static void classTeardown()
  {
    city.delete();
  }
  
  @Test
  @Request
  public void testCreateStyle()
  {
     MapImpl map = new MapImpl();
     map.setName("Map 1");
     
     LayerImpl layer1 = new LayerImpl();
     layer1.setName("Layer 1");
     layer1.setVirtual(true);
     layer1.setFeatureType(FeatureType.POINT);
     map.addLayer(layer1);
    
     Style style1 = new StyleImpl();
     style1.setName("Style 1.1");
    
     // point
     style1.setPointSize(3);
     style1.setPointStroke("#000000");
     style1.setPointFill("#fffeee");
     style1.setPointStrokeWidth(1);
     style1.setPointOpacity(0.4);
     style1.setPointRotation(6);
     style1.setPointStrokeWidth(8);
     style1.setPointWellKnownName(WellKnownName.STANDARD.CIRCLE.getSymbol());
     // polygon
     style1.setPolygonFill("#eeeeee");
     style1.setPolygonStroke("#000000");
     style1.setPolygonStrokeWidth(4);
    
     layer1.addStyle(style1);
    
     ThematicStyleImpl style2 = new ThematicStyleImpl();
     style2.setAttribute("testAttribute");
     // point
     style2.setName("Style 1.2");
     style2.setPointFill("#999eee");
     style2.setPointSize(1);
     style2.setPointStroke("#008800");
     style2.setPointStrokeWidth(2);
     style2.setPointOpacity(1.0);
     style2.setPointRotation(3);
     style2.setPointStrokeWidth(2);
     // polygon
     style2.setPolygonFill("#efefef");
     style2.setPolygonStroke("#ff0000");
     style2.setPolygonStrokeWidth(3);
     style2.setPointWellKnownName(WellKnownName.STANDARD.SQUARE.getSymbol());
    
     layer1.addStyle(style2);
    
     Equal a = new EqualImpl();
     a.setValue("1");
    
     Equal b = new EqualImpl();
     b.setValue("2");
    
     Or or1 = new OrImpl();
     or1.setThematicStyle(style2);
     or1.setLeftCondition(a);
     or1.setRightCondition(b);
    
     Equal c = new EqualImpl();
     c.setValue("8");
    
     Equal d = new EqualImpl();
     d.setValue("9");
    
     Or or2 = new OrImpl();
     or2.setThematicStyle(style2);
     or2.setLeftCondition(c);
     or2.setRightCondition(d);
    
     And and = new AndImpl();
     and.setThematicStyle(style2);
     and.setLeftCondition(or1);
     and.setRightCondition(or2);
    
     style2.setCondition(and);
    
     SLDMapVisitor visitor = new SLDMapVisitor();
     map.accepts(visitor);
    
     String layer1SLD = visitor.getSLD(layer1);
     
     try
     {
       GeoserverFacade.publishStyle("testStyle", layer1SLD);
     }
     finally
     {
       GeoserverFacade.removeStyle("testStyle");
     }
     
  }
}
