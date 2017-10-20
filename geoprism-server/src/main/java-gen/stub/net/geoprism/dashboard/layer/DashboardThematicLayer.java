/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see
 * <http://www.gnu.org/licenses/>.
 */
package net.geoprism.dashboard.layer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIndicatorInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.dataaccess.IndicatorElementDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
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

import net.geoprism.MappableAttribute;
import net.geoprism.QueryUtil;
import net.geoprism.dashboard.AggregationStrategy;
import net.geoprism.dashboard.AggregationStrategyView;
import net.geoprism.dashboard.AggregationType;
import net.geoprism.dashboard.AllAggregationType;
import net.geoprism.dashboard.Dashboard;
import net.geoprism.dashboard.DashboardLegend;
import net.geoprism.dashboard.DashboardMap;
import net.geoprism.dashboard.DashboardStyle;
import net.geoprism.dashboard.DashboardThematicStyle;
import net.geoprism.dashboard.GeometryAggregationStrategy;
import net.geoprism.dashboard.MdAttributeView;
import net.geoprism.dashboard.MissingLocationAttributeException;
import net.geoprism.dashboard.condition.DashboardCondition;
import net.geoprism.gis.wrapper.AttributeType;
import net.geoprism.gis.wrapper.MapVisitor;
import net.geoprism.gis.wrapper.ThematicLayer;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;
import net.geoprism.util.CollectionUtil;

public class DashboardThematicLayer extends DashboardThematicLayerBase implements Reloadable, ThematicLayer
{
  private static final long serialVersionUID = -810007054;

  public static long        LIMIT            = 10;

  public static String      layerType        = "THEMATICLAYER";

  public DashboardThematicLayer()
  {
    super();
  }

  // @Override
  // public String getName()
  // {
  // return this.getNameLabel().getValue();
  // }

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

    try
    {
      this.applyAll(style, mapId, strategy, conditions);
    }
    catch (com.runwaysdk.dataaccess.database.DuplicateDataDatabaseException e)
    {
      throw new DuplicateLayerException(e);
    }

    return this.publish();
  }

  /**
   * Gets the min and max values of a data set to be used for styling based data distributions
   */
  public HashMap<String, Double> getLayerMinMax(String _attribute)
  {

    HashMap<String, Double> minMaxMap = new HashMap<String, Double>();
    String thematicAttrType = this.getMdAttribute().getType();

    QueryFactory f = new QueryFactory();
    ValueQuery wrapper = new ValueQuery(f);
    wrapper.FROM(getViewName(), "");

    List<Selectable> selectables = new LinkedList<Selectable>();

    //
    // Only number types can be used
    //
    //
    // Only number types can be used
    //
    Set<String> numerics = new TreeSet<String>();
    numerics.add(MdAttributeLongInfo.CLASS);
    numerics.add(MdAttributeIntegerInfo.CLASS);
    numerics.add(MdAttributeDoubleInfo.CLASS);
    numerics.add(MdAttributeDecimalInfo.CLASS);
    numerics.add(MdAttributeFloatInfo.CLASS);
    numerics.add(MdAttributeBooleanInfo.CLASS);
    numerics.add(MdAttributeIndicatorInfo.CLASS);

    if (numerics.contains(thematicAttrType))
    {
      selectables.add(wrapper.aSQLAggregateDouble("min_data", "MIN(" + _attribute + ")"));
      selectables.add(wrapper.aSQLAggregateDouble("max_data", "MAX(" + _attribute + ")"));
    }
    else
    {
      throw new ProgrammingErrorException("Could not calculate the min/max value of a non-numeric attribute");
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

  private static String getCategoryType(MdAttributeDAOIF mdAttribute)
  {
    MdAttributeConcreteDAOIF mdAttributeConcrete = mdAttribute.getMdAttributeConcrete();

    if (mdAttributeConcrete instanceof MdAttributeDateDAOIF)
    {
      return "date";
    }
    else if (mdAttributeConcrete instanceof MdAttributeNumberDAOIF)
    {
      return "number";
    }

    return "text";
  }

  private static JSONObject getMdAttributeType(MdAttribute mdAttribute)
  {
    JSONObject attrObj = new JSONObject();
    MdAttributeConcrete mdAttributeConcrete = getMdAttributeConcrete(mdAttribute);

    // Determine if the attribute is an ontology attribute
    if (mdAttributeConcrete instanceof MdAttributeTerm)
    {
      MdAttributeTermDAOIF mdAttributeTerm = MdAttributeTermDAO.get(mdAttributeConcrete.getId());

      if (mdAttributeTerm.getReferenceMdBusinessDAO().definesType().equals(Classifier.CLASS))
      {
        try
        {
          boolean dynamic = ! ( Dashboard.getOptionCount(mdAttribute.getId()) < LIMIT );

          attrObj.put("isOntologyAttribute", true);
          attrObj.put("isTextAttribute", false);
          attrObj.put("relationshipType", ClassifierIsARelationship.CLASS);
          attrObj.put("termType", Classifier.CLASS);
          attrObj.put("dynamic", dynamic);

          if (!dynamic)
          {
            attrObj.put("nodes", Dashboard.getClassifierTreeJSON(mdAttribute.getId()));
          }
        }
        catch (JSONException e)
        {
          throw new ProgrammingErrorException(e);
        }
      }
    }
    else if (mdAttributeConcrete instanceof MdAttributeCharacter || mdAttributeConcrete instanceof MdAttributeText)
    {
      try
      {
        attrObj.put("isTextAttribute", true);
        attrObj.put("isOntologyAttribute", false);
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
    else
    {
      try
      {
        attrObj.put("isOntologyAttribute", false);
        attrObj.put("isTextAttribute", false);
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return attrObj;
  }

  public static String getOptionsJSON(String thematicAttributeId, String dashboardId)
  {
    try
    {
      Dashboard dashboard = Dashboard.get(dashboardId);
      MdAttribute tAttr = MdAttribute.get(thematicAttributeId);
      MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(thematicAttributeId);

      MappableAttribute mAttribute = MappableAttribute.getMappableAttribute(mdAttribute);
      Boolean aggregatable = ( mAttribute != null ? mAttribute.getAggregatable() : true );

      String[] fonts = DashboardThematicStyle.getSortedFonts();
      String geoNodesJSON = dashboard.getGeoNodesJSON(tAttr, aggregatable);

      JSONArray aggStrategiesJSON = new JSONArray();
      GeoNode[] geoNodes = dashboard.getGeoNodes(tAttr);

      for (GeoNode geoNode : geoNodes)
      {
        String nodeId = geoNode.getId();
        String nodeType = geoNode.getType();
        String nodeLabel = geoNode.getDisplayLabelAttribute().getDisplayLabel().getValue();

        List<AggregationStrategyView> strategies = Arrays.asList(AggregationStrategyView.getAggregationStrategies(geoNode, aggregatable));

        if (strategies.size() > 0)
        {
          Collections.reverse(strategies);

          JSONArray aggregationStrategies = new JSONArray();

          for (AggregationStrategyView strategy : strategies)
          {
            aggregationStrategies.put(strategy.toJSON());
          }

          JSONObject nodeObj = new JSONObject();
          nodeObj.put("nodeId", nodeId);
          nodeObj.put("nodeType", nodeType);
          nodeObj.put("nodeLabel", nodeLabel);
          nodeObj.put("aggregationStrategies", aggregationStrategies);

          aggStrategiesJSON.put(nodeObj);
        }
      }

      JSONArray secondaryAttributes = getSecodaryAttributesJSON(dashboard.getMapId(), thematicAttributeId);
      JSONObject attributeType = getMdAttributeType(tAttr);
      String attrDataType = getCategoryType(mdAttribute);

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
      json.put("aggregations", getAggregationMethodsAsJSON(thematicAttributeId, aggregatable));
      json.put("aggegationStrategies", aggStrategiesJSON);
      json.put("fonts", new JSONArray(Arrays.asList(fonts)));
      json.put("geoNodes", new JSONArray(geoNodesJSON));

      json.put("attributeType", attributeType);
      json.put("attributeDataType", attrDataType);

      json.put("secondaryAttributes", secondaryAttributes);

      json.put("aggregationMap", DashboardStyle.getAggregationJSON());

      json.put("layerTypeNames", new JSONArray(layerTypes.keySet().toArray()));
      json.put("layerTypeLabels", new JSONArray(layerTypes.values().toArray()));

      JSONArray pointTypes = new JSONArray();
      pointTypes.put("CIRCLE");
      pointTypes.put("STAR");
      pointTypes.put("SQUARE");
      pointTypes.put("TRIANGLE");
      pointTypes.put("CROSS");
      pointTypes.put("X");
      json.put("pointTypes", pointTypes);

      if (aggStrategiesJSON.length() == 0)
      {
        throw new MissingLocationAttributeException();
      }

      return json.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private static JSONArray getSecodaryAttributesJSON(String mapId, String mdAttributeId)
  {
    JSONArray secAttrs = new JSONArray();
    MdAttributeView[] secondaryAttributes = DashboardMap.getSecondaryAttributes(mapId, mdAttributeId);

    try
    {
      JSONObject object = new JSONObject();
      object.put("label", "None");
      object.put("id", "NONE");

      secAttrs.put(object);
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }

    for (MdAttributeView secAttr : secondaryAttributes)
    {
      JSONObject secAttrObj = new JSONObject();
      try
      {
        MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(secAttr.getMdAttributeId()).getMdAttributeConcrete();

        secAttrObj.put("id", secAttr.getId());
        secAttrObj.put("mdAttributeId", secAttr.getMdAttributeId());
        secAttrObj.put("type", secAttr.getAttributeType());
        secAttrObj.put("label", secAttr.getDisplayLabel());
        secAttrObj.put("categoryType", DashboardThematicLayer.getCategoryType(mdAttributeConcrete));

        if (mdAttributeConcrete instanceof MdAttributeTermDAOIF)
        {
          MdAttributeTermDAOIF mdAttributeTerm = (MdAttributeTermDAOIF) mdAttributeConcrete;

          if (mdAttributeTerm.getReferenceMdBusinessDAO().definesType().equals(Classifier.CLASS))
          {
            boolean dynamic = ! ( Dashboard.getOptionCount(secAttr.getMdAttributeId()) < LIMIT );

            secAttrObj.put("dynamic", dynamic);

            if (!dynamic)
            {
              secAttrObj.put("nodes", Dashboard.getClassifierTreeJSON(secAttr.getMdAttributeId()));
            }
          }
        }

        secAttrs.put(secAttrObj);
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return secAttrs;
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
      if (aggStrategy != null)
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

    String label = mdAttribute.getDisplayLabel(Session.getCurrentLocale());

    if (label == null || label.length() == 0)
    {
      return mdAttribute.getMdAttributeConcrete().getDisplayLabel(Session.getCurrentLocale());
    }

    return label;
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
    MdAttributeConcreteDAOIF mdAttribute = this.getMdAttributeDAO().getMdAttributeConcrete();

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
    else if (mdAttribute instanceof MdAttributeIndicatorDAOIF)
    {
      IndicatorElementDAOIF indicator = ( (MdAttributeIndicatorDAOIF) mdAttribute ).getIndicator();

      if (indicator.isPercentage())
      {
        return AttributeType.PERCENT;
      }
      else
      {
        return AttributeType.NUMBER;
      }
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
    return MdAttributeDAO.get(this.getMdAttributeId()).definesAttribute();
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

  @Override
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

  private static JSONArray getAggregationMethodsAsJSON(String thematicAttributeId, boolean aggregatable)
  {
    try
    {
      JSONArray methods = new JSONArray();

      if (aggregatable)
      {
        OIterator<? extends AggregationType> aggregations = DashboardStyle.getSortedAggregations(thematicAttributeId).getIterator();

        try
        {
          for (AggregationType aggMethod : aggregations)
          {
            String formattedAggMethod = aggMethod.toString().replaceAll(".*\\.", "");

            JSONObject aggMethodObj = new JSONObject();
            aggMethodObj.put("method", formattedAggMethod);
            aggMethodObj.put("label", aggMethod.getDisplayLabel());
            aggMethodObj.put("id", aggMethod.getId());

            methods.put(aggMethodObj);
          }
        }
        finally
        {
          aggregations.close();
        }
      }
      else
      {
        JSONObject method = new JSONObject();
        method.put("method", AllAggregationType.NONE.name());
        method.put("label", AllAggregationType.NONE.getDisplayLabel());
        method.put("id", AllAggregationType.NONE.getId());

        methods.put(method);
      }

      return methods;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
}
