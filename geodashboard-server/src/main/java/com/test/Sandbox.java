package com.test;

import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.Map;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.geodashboard.gis.model.condition.And;
import com.runwaysdk.geodashboard.gis.model.condition.Equal;
import com.runwaysdk.geodashboard.gis.model.condition.Or;
import com.runwaysdk.geodashboard.gis.model.impl.AndImpl;
import com.runwaysdk.geodashboard.gis.model.impl.EqualImpl;
import com.runwaysdk.geodashboard.gis.model.impl.LayerImpl;
import com.runwaysdk.geodashboard.gis.model.impl.MapImpl;
import com.runwaysdk.geodashboard.gis.model.impl.OrImpl;
import com.runwaysdk.geodashboard.gis.model.impl.StyleImpl;
import com.runwaysdk.geodashboard.gis.model.impl.ThematicStyleImpl;
import com.runwaysdk.geodashboard.gis.sld.SLDMapVisitor;
import com.runwaysdk.geodashboard.gis.sld.WellKnownName;

public class Sandbox
{
  public static void main(String[] args)
  {
    
    Map map = new MapImpl("My Map");
    
    Layer layer1 = new LayerImpl("Layer 1");
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
    
    System.out.println(visitor.getSLD());
//    
//    Map map2 = new MapBuilder("My Map")
//    
//      .layer("Layer 1").composite(true).featureType(FeatureType.POINT)
//        .style("Default Style").pointSize(3).pointStrokeWidth(5).pointRotation(5)
//        .add()
//      
//      .layer("Layer 2").featureType(FeatureType.POLYGON)
//        .tStyle("Thematic Style").attribute("foo")
//        .add()
//      
//    .build();
//
//    SLDMapVisitor visitor2 = new SLDMapVisitor();
//    map2.accepts(visitor2);
//    System.out.println(visitor2.getSLD());
  }
}
