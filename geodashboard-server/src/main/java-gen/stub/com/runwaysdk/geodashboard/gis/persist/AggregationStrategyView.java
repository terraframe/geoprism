package com.runwaysdk.geodashboard.gis.persist;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.GeoEntityUtil;
import com.runwaysdk.geodashboard.MetadataGeoNode;
import com.runwaysdk.geodashboard.MetadataGeoNodeQuery;
import com.runwaysdk.geodashboard.MetadataWrapper;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.GeoNodeGeometry;
import com.runwaysdk.system.gis.geo.Universal;

public class AggregationStrategyView extends AggregationStrategyViewBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1241142559;

  public AggregationStrategyView()
  {
    super();
  }
  
  public static String getAggregationStrategiesJSON(java.lang.String nodeId)
  {
    GeoNode geoNode = GeoNode.get(nodeId);
    AggregationStrategyView[] strategyViews = getAggregationStrategies(geoNode);
    
    JSONArray strategyViewsJSON = new JSONArray();
    for(AggregationStrategyView view : strategyViews)
    {
      JSONObject viewObj = new JSONObject();
      String id = view.getId();
      String type = view.getAggregationType();
      String dispLabel = view.getDisplayLabel();
      try
      {
        viewObj.put("id", id);
        viewObj.put("type", type);
        viewObj.put("label", dispLabel);
      }
      catch (JSONException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      strategyViewsJSON.put(viewObj);
    }
    
    return encode(strategyViewsJSON.toString());
  }
  
  
  private static String encode(String value)
  {
    if (value != null)
    {
      try
      {
        return URLEncoder.encode(value, "UTF-8");
      }
      catch (UnsupportedEncodingException e)
      {
        throw new ProgrammingErrorException(e.getLocalizedMessage());
      }
    }

    return "";
  }

  
  public static AggregationStrategyView[] getAggregationStrategies(GeoNode node)
  {
    List<AggregationStrategyView> list = new LinkedList<AggregationStrategyView>();

    MetadataGeoNodeQuery query = new MetadataGeoNodeQuery(new QueryFactory());
    query.WHERE(query.getChild().EQ(node));

    OIterator<? extends MetadataGeoNode> iterator = query.getIterator();

    try
    {
      MetadataGeoNode relationship = iterator.next();
      MetadataWrapper wrapper = relationship.getParent();

      Universal lowest = wrapper.getUniversal();
      Universal root = Universal.getRoot();

      Collection<Term> universals = GeoEntityUtil.getOrderedAncestors(root, lowest, AllowedIn.CLASS);

      for (Term universal : universals)
      {
        AggregationStrategyView view = new AggregationStrategyView();
        view.setAggregationType(UniversalAggregationStrategy.CLASS);
        view.setValue(universal.getId());
        view.setDisplayLabel(universal.getDisplayLabel().getValue());

        list.add(view);
      }
    }
    finally
    {
      iterator.close();
    }

    if (node instanceof GeoNodeGeometry)
    {
      String label = node.getGeoEntityAttribute().getDisplayLabel().getValue();

      AggregationStrategyView view = new AggregationStrategyView();
      view.setAggregationType(GeometryAggregationStrategy.CLASS);
      view.setValue("GEOMETRY");
      view.setDisplayLabel(label);

      list.add(view);
    }

    return list.toArray(new AggregationStrategyView[list.size()]);
  }
}
