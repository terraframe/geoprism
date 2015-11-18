/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.geodashboard.gis.persist;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.Dashboard;
import com.runwaysdk.geodashboard.QueryUtil;
import com.runwaysdk.geodashboard.gis.impl.condition.DashboardCondition;
import com.runwaysdk.geodashboard.gis.model.AttributeType;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicLayer;
import com.runwaysdk.geodashboard.util.CollectionUtil;
import com.runwaysdk.geodashboard.util.EscapeUtil;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.GeoNodeGeometry;
import com.runwaysdk.system.gis.metadata.MdAttributeMultiPolygon;
import com.runwaysdk.system.gis.metadata.MdAttributePoint;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeDate;
import com.runwaysdk.system.metadata.MdAttributeTerm;
import com.runwaysdk.system.metadata.MdAttributeText;
import com.runwaysdk.system.metadata.MdAttributeVirtual;

public class DashboardThematicLayer extends DashboardThematicLayerBase implements Reloadable, ThematicLayer
{
  private static final long serialVersionUID = -810007054;

  public static String      layerType        = "THEMATICLAYER";

  public DashboardThematicLayer()
  {
    super();
  }

  @Transaction
  public void applyAll(DashboardStyle style, String mapId, AggregationStrategy strategy, DashboardCondition[] conditions)
  {
    // If there is an existing aggregation strategy then delete it and use the new one
    AggregationStrategy existing = this.getAggregationStrategy();

    strategy.apply();

    this.setAggregationStrategy(strategy);

    super.applyAll(style, mapId, conditions);

    if (existing != null)
    {
      existing.delete();
    }
  }
  
  @Override
  public String applyWithStyleAndStrategy(DashboardStyle style, String mapId, AggregationStrategy strategy, String state)
  {
    DashboardCondition[] conditions = DashboardCondition.getConditionsFromState(state);
    
    this.applyAll(style, mapId, strategy, conditions);

    return this.publish();
  }

  /**
   * Gets the min and max values of a data set to be used for styling based data distributions
   */
  public HashMap<String, Double> getLayerMinMax(String _attribute)
  {

    HashMap<String, Double> minMaxMap = new HashMap<String, Double>();

    QueryFactory f = new QueryFactory();
    ValueQuery wrapper = new ValueQuery(f);
    wrapper.FROM(getViewName(), "");

    List<Selectable> selectables = new LinkedList<Selectable>();
    AllLayerType layerType = this.getLayerType().get(0);
    if (layerType == AllLayerType.BUBBLE || layerType == AllLayerType.GRADIENTPOLYGON || layerType == AllLayerType.GRADIENTPOINT)
    {

      selectables.add(wrapper.aSQLAggregateDouble("min_data", "MIN(" + _attribute + ")"));
      selectables.add(wrapper.aSQLAggregateDouble("max_data", "MAX(" + _attribute + ")"));
    }

    selectables.add(wrapper.aSQLAggregateLong("totalResults", "COUNT(*)"));

    wrapper.SELECT(selectables.toArray(new Selectable[selectables.size()]));

    OIterator<? extends ValueObject> iter = wrapper.getIterator();
    try
    {
      ValueObject row = iter.next();

      String min = row.getValue("min_data");
      String max = row.getValue("max_data");

      CollectionUtil.populateMap(minMaxMap, "min", min, new Double(0));
      CollectionUtil.populateMap(minMaxMap, "max", max, new Double(0));

      return minMaxMap;
    }
    finally
    {
      iter.close();
    }
  }
  
  @Override
  public String getJSON()
  {
    return this.toJSON().toString();
  }
  
  private static MdAttributeConcrete getMdAttributeConcrete(MdAttribute mdAttr)
  {
    if (mdAttr instanceof MdAttributeVirtual)
    {
      MdAttributeVirtual mdAttributeVirtual = (MdAttributeVirtual) mdAttr;

      return mdAttributeVirtual.getMdAttributeConcrete();
    }

    return ( (MdAttributeConcrete) mdAttr );
  }
  
  public static String getOptionsJSON(String thematicAttributeId, String dashboardId)
  {
    Dashboard dashboard = Dashboard.get(dashboardId);
    MdAttribute tAttr = MdAttribute.get(thematicAttributeId);
    String[] fonts = DashboardThematicStyle.getSortedFonts();
    OIterator<? extends AggregationType> aggregations = DashboardStyle.getSortedAggregations(thematicAttributeId).getIterator();
    String geoNodesJSON = dashboard.getGeoNodesJSON(tAttr);
    
    JSONArray aggStrategiesJSON = new JSONArray();
    GeoNode[] geoNodes = dashboard.getGeoNodes(tAttr);
    for(GeoNode geoNode : geoNodes)
    {
      JSONObject nodeObj = new JSONObject();
      String nodeId = geoNode.getId();
      String nodeType = geoNode.getType();
      String nodeLabel = geoNode.getDisplayLabelAttribute().getDisplayLabel().toString();
      
      try
      {
        nodeObj.put("nodeId", nodeId);
        nodeObj.put("nodeType", nodeType);
        nodeObj.put("nodeLabel", nodeLabel);
      }
      catch (JSONException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      JSONArray aggArr = new JSONArray();
      AggregationStrategyView[] aggStrategies = AggregationStrategyView.getAggregationStrategies(geoNode);
      for(AggregationStrategyView aggStrat : aggStrategies)
      {
        JSONObject aggObj = new JSONObject();
        String aggStratId = aggStrat.getId();
        String aggStratLabel = aggStrat.getDisplayLabel();
        String aggType = aggStrat.getAggregationType();
        String aggGeomTypes = aggStrat.getAvailableGeometryTypes();
        String aggValue = aggStrat.getValue();
        
        try
        {
          aggObj.put("aggStrategyId", aggStratId);
          aggObj.put("aggStrategyLabel", aggStratLabel);
          aggObj.put("aggStrategyType", aggType);
          aggObj.put("aggStrategyGeomTypes", aggGeomTypes);
          aggObj.put("aggStrategyValue", aggValue);
          aggArr.put(aggObj);
        }
        catch (JSONException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      
      try
      {
        nodeObj.put("aggregationStrategies", aggArr);
      }
      catch (JSONException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      aggStrategiesJSON.put(nodeObj);
    }
    
    
    // Set possible layer types based on attribute type
    Map<String, String> layerTypes = new LinkedHashMap<String, String>();
    MdAttributeConcrete mdAttributeConcrete = getMdAttributeConcrete(tAttr);
    if (mdAttributeConcrete instanceof MdAttributeDate)
    {
      layerTypes.put(AllLayerType.BASICPOINT.getEnumName(), AllLayerType.BASICPOINT.getDisplayLabel());
      layerTypes.put(AllLayerType.BASICPOLYGON.getEnumName(), AllLayerType.BASICPOLYGON.getDisplayLabel());
    }
    else if (mdAttributeConcrete instanceof MdAttributeTerm || mdAttributeConcrete instanceof MdAttributeText || mdAttributeConcrete instanceof MdAttributeCharacter)
    {
      layerTypes.put(AllLayerType.BASICPOINT.getEnumName(), AllLayerType.BASICPOINT.getDisplayLabel());
      layerTypes.put(AllLayerType.CATEGORYPOINT.getEnumName(), AllLayerType.CATEGORYPOINT.getDisplayLabel());
      layerTypes.put(AllLayerType.BASICPOLYGON.getEnumName(), AllLayerType.BASICPOLYGON.getDisplayLabel());
      layerTypes.put(AllLayerType.CATEGORYPOLYGON.getEnumName(), AllLayerType.CATEGORYPOLYGON.getDisplayLabel());
    }
    else
    {
      layerTypes.put(AllLayerType.BASICPOINT.getEnumName(), AllLayerType.BASICPOINT.getDisplayLabel());
      layerTypes.put(AllLayerType.GRADIENTPOINT.getEnumName(), AllLayerType.GRADIENTPOINT.getDisplayLabel());
      layerTypes.put(AllLayerType.CATEGORYPOINT.getEnumName(), AllLayerType.CATEGORYPOINT.getDisplayLabel());
      layerTypes.put(AllLayerType.BUBBLE.getEnumName(), AllLayerType.BUBBLE.getDisplayLabel());
      layerTypes.put(AllLayerType.BASICPOLYGON.getEnumName(), AllLayerType.BASICPOLYGON.getDisplayLabel());
      layerTypes.put(AllLayerType.GRADIENTPOLYGON.getEnumName(), AllLayerType.GRADIENTPOLYGON.getDisplayLabel());
      layerTypes.put(AllLayerType.CATEGORYPOLYGON.getEnumName(), AllLayerType.CATEGORYPOLYGON.getDisplayLabel());
    }
   
    JSONObject json = new JSONObject();
    try
    {
      json.put("aggregations", formatAggregationMethods(aggregations));
      json.put("aggegationStrategies", aggStrategiesJSON);
      json.put("fonts", new JSONArray(Arrays.asList(fonts)));
      json.put("geoNodes", new JSONArray(geoNodesJSON));
      
      json.put("layerTypeNames", new JSONArray(layerTypes.keySet().toArray()));
      json.put("layerTypeLabels", new JSONArray(layerTypes.values().toArray()));
//      json.put("layerTypeNamesJSON", new JSONArray(layerTypes.keySet()));
      
      JSONArray pointTypes = new JSONArray();
      pointTypes.put("CIRCLE");
      pointTypes.put("STAR");
      pointTypes.put("SQUARE");
      pointTypes.put("TRIANGLE");
      pointTypes.put("CROSS");
      pointTypes.put("X");
      json.put("pointTypes", pointTypes);
    }
    catch (JSONException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return json.toString();
  }
  
  private static JSONArray formatAggregationMethods(OIterator<? extends AggregationType> aggregations)
  {
    JSONArray formattedAggMethods = new JSONArray();
    for(AggregationType aggMethod : aggregations)
    {
      try
      {
        JSONObject aggMethodObj = new JSONObject();
        String formattedAggMethod = aggMethod.toString().replaceAll(".*\\.", "");
        aggMethodObj.put("method", formattedAggMethod);
        aggMethodObj.put("label", aggMethod.getDisplayLabel());
        aggMethodObj.put("id", aggMethod.getId());
        formattedAggMethods.put(aggMethodObj);
      }
      catch (JSONException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    return formattedAggMethods;
  }
  
  private static String encodeString(String val)
  {
    
    String value = null;
    try
    {
      value = URLEncoder.encode(val, "UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return value;
  }

  public JSONObject toJSON()
  {
    try
    {
      DashboardLegend legend = this.getDashboardLegend();
      
      JSONObject json = new JSONObject();
      json.put("viewName", getViewName());
      json.put("sldName", getSLDName());
      json.put("layerName", getName());
      json.put("layerId", getId());
      json.put("inLegend", this.getDisplayInLegend());
      json.put("legendXPosition", legend.getLegendXPosition());
      json.put("legendYPosition", legend.getLegendYPosition());
      json.put("groupedInLegend", legend.getGroupedInLegend());
      json.put("featureStrategy", getFeatureStrategy());
      json.put("mdAttributeId", this.getMdAttributeId());
      json.put("attributeType", this.getAttributeType());
      json.put("aggregationMethod", this.getAggregationMethod());
      json.put("aggregationAttribute", this.getAttribute());
      json.put("layerType", layerType);
      json.put("attributeLabel", this.getAttributeDisplayLabel());
      json.put("geoNodeId", this.getGeoNodeId());
      
      AggregationStrategy aggStrategy = this.getAggregationStrategy();
      JSONObject aggStratJSON = null;
      if(aggStrategy != null)
      {
        aggStratJSON = aggStrategy.getJSON();
      }
      else
      {
        aggStratJSON = new JSONObject();
      }
      json.put("aggregationStrategy", aggStratJSON);

      JSONArray jsonStyles = new JSONArray();
      List<? extends DashboardStyle> styles = this.getStyles();
      for (int i = 0; i < styles.size(); ++i)
      {
        DashboardStyle style = styles.get(i);
        jsonStyles.put(style.toJSON());
      }
      json.put("styles", jsonStyles);

      return json;
    }
    catch (JSONException ex)
    {
      log.error("Could not properly form DashboardLayer [" + this.toString() + "] into valid JSON to send back to the client.");
      throw new ProgrammingErrorException(ex);
    }
  }

  public String getAttributeDisplayLabel()
  {
    MdAttributeDAOIF mdAttribute = this.getMdAttributeDAO();

    if(mdAttribute != null)
    {
      String label = mdAttribute.getDisplayLabel(Session.getCurrentLocale());

      if (label == null || label.length() == 0)
      {
        return mdAttribute.getMdAttributeConcrete().getDisplayLabel(Session.getCurrentLocale());
      }

      return label;
    }
    
    return null;
  }

  /**
   * @prerequisite conditions is populated with any DashboardConditions necessary for restricting the view dataset.
   * 
   * @return A ValueQuery for use in creating/dropping the database view which will be used with GeoServer.
   */
  public ValueQuery getViewQuery()
  {
    AggregationStrategy strategy = this.getAggregationStrategy();

    return strategy.getViewQuery(this);
  }

  public DashboardStyle getStyle()
  {
    OIterator<? extends DashboardStyle> iter = this.getAllHasStyle();

    try
    {
      while (iter.hasNext())
      {
        return iter.next();

      }
    }
    finally
    {
      iter.close();
    }

    throw new ProgrammingErrorException("Dashboard layer exists without a style");
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  public AllAggregationType getAggregationMethod()
  {
    List<AllAggregationType> allAgg = this.getAggregationType();

    if (allAgg.size() > 0)
    {
      return allAgg.get(0);
    }

    return null;
  }

  public MdAttributeDAOIF getMdAttributeDAO()
  {
    String mdAttributeId = this.getMdAttributeId();

    if (mdAttributeId != null && mdAttributeId.length() > 0)
    {
      return MdAttributeDAO.get(mdAttributeId);
    }

    return null;
  }

  @Override
  public AttributeType getAttributeType()
  {
    MdAttributeDAOIF mdAttribute = null;
    MdAttributeDAOIF mdAttrIF = this.getMdAttributeDAO();
    if(mdAttrIF != null)
    {
      mdAttribute = mdAttrIF.getMdAttributeConcrete();
    }

    if (mdAttribute instanceof MdAttributeDateDAOIF)
    {
      return AttributeType.DATE;
    }
    else if (mdAttribute instanceof MdAttributeDateTimeDAOIF)
    {
      return AttributeType.DATETIME;
    }
    else if (mdAttribute instanceof MdAttributeTimeDAOIF)
    {
      return AttributeType.TIME;
    }
    else if (mdAttribute instanceof MdAttributeNumberDAOIF)
    {
      return AttributeType.NUMBER;
    }

    return AttributeType.BASIC;
  }

  @Override
  public String getAttribute()
  {
    String attrId = this.getMdAttributeId();
    if(attrId.length() > 0)
    {
      return MdAttributeDAO.get(attrId).definesAttribute();
    }
    else
    {
      return null;
    }
  }

  protected void populate(DashboardLayer source)
  {
    super.populate(source);

    if (source instanceof DashboardThematicLayer)
    {
      DashboardThematicLayer tSource = (DashboardThematicLayer) source;

      List<AllAggregationType> types = tSource.getAggregationType();

      for (AllAggregationType type : types)
      {
        this.addAggregationType(type);
      }

      this.setMdAttribute(tSource.getMdAttribute());
      this.setGeoNode(tSource.getGeoNode());
      this.setAggregationStrategy(tSource.getAggregationStrategy().clone());
    }
  }

  @Override
  protected DashboardLayer newInstance()
  {
    return new DashboardThematicLayer();
  }

  public String getCategoryLabel(String categoryId)
  {
    AggregationStrategy strategy = this.getAggregationStrategy();
    GeoNode geoNode = this.getGeoNode();

    return strategy.getCategoryLabel(geoNode, categoryId);
  }

//  @Override
  public String getFeatureInformation(String featureId)
  {
    GeoNode geoNode = this.getGeoNode();
    AggregationStrategy strategy = this.getAggregationStrategy();

    if (geoNode instanceof GeoNodeGeometry && strategy instanceof GeometryAggregationStrategy)
    {
      String mdAttributeId = geoNode.getValue(GeoNodeGeometry.IDENTIFIERATTRIBUTE);
      MdAttributeConcreteDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();
      MdClassDAOIF mdClass = mdAttribute.definedByClass();

      ValueQuery vQuery = new ValueQuery(new QueryFactory());
      GeneratedComponentQuery query = QueryUtil.getQuery(mdClass, vQuery);

      Selectable geoId = query.get(mdAttribute.definesAttribute());
      geoId.setColumnAlias(GeoEntity.GEOID);
      geoId.setUserDefinedAlias(GeoEntity.GEOID);
      geoId.setUserDefinedDisplayLabel(GeoEntity.getGeoIdMd().getDisplayLabel(Session.getCurrentLocale()));

      Selectable id = query.get(ComponentInfo.ID);
      id.setColumnAlias(ComponentInfo.ID);
      id.setUserDefinedAlias(ComponentInfo.ID);
      id.setUserDefinedDisplayLabel(GeoEntity.getIdMd().getDisplayLabel(Session.getCurrentLocale()));

      vQuery.SELECT(id, geoId);
      vQuery.WHERE(geoId.EQ(featureId));

      OIterator<ValueObject> iterator = null;

      try
      {
        iterator = vQuery.getIterator();

        if (iterator.hasNext())
        {
          ValueObject vObject = iterator.next();

          JSONObject json = new JSONObject();
          json.put(ComponentInfo.ID, vObject.getValue(ComponentInfo.ID));
          json.put(ComponentInfo.TYPE, mdClass.definesType());
          json.put(GeoEntity.GEOID, vObject.getValue(GeoEntity.GEOID));

          return json.toString();
        }
        else
        {
          throw new ProgrammingErrorException("Unable to find a feature with the feature id of [" + featureId + "] for layer [" + this.getId() + "]");
        }
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
      finally
      {
        if (iterator != null)
        {
          iterator.close();
        }
      }
    }
    else
    {
      throw new ProgrammingErrorException("Feature information is unsupported for layers which do not use the geometry aggregation strategy.");
    }

  }

  public static String getGeoNodeGeometryTypesJSON(String geoNodeId)
  {
    JSONArray json = new JSONArray();
    GeoNode geoNode = GeoNode.get(geoNodeId);

    MdAttribute mdAttributeGeometry = geoNode.getGeometryAttribute();
    if (mdAttributeGeometry != null)
    {
      json.put(mdAttributeGeometry.getType());
    }

    MdAttributePoint mdAttributePoint = geoNode.getPointAttribute();
    if (mdAttributePoint != null)
    {
      json.put(mdAttributePoint.getType());
    }

    MdAttributeMultiPolygon mdAttributeMultiPolygon = geoNode.getMultiPolygonAttribute();
    if (mdAttributeMultiPolygon != null)
    {
      json.put(mdAttributeMultiPolygon.getType());
    }

    return json.toString();
  }
}
