package com.runwaysdk.geodashboard.gis.persist;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.model.AttributeType;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicLayer;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.util.CollectionUtil;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.metadata.MdAttributeMultiPolygon;
import com.runwaysdk.system.gis.metadata.MdAttributePoint;
import com.runwaysdk.system.metadata.MdAttribute;

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

  public String applyWithStyleAndStrategy(DashboardStyle style, String mapId, AggregationStrategy strategy, DashboardCondition[] conditions)
  {
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
    if (layerType == AllLayerType.BUBBLE || layerType == AllLayerType.GRADIENT)
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

  public JSONObject toJSON()
  {
    try
    {
      JSONObject json = new JSONObject();
      json.put("viewName", getViewName());
      json.put("sldName", getSLDName());
      json.put("layerName", getName());
      json.put("layerId", getId());
      json.put("inLegend", this.getDisplayInLegend());
      json.put("legendXPosition", this.getDashboardLegend().getLegendXPosition());
      json.put("legendYPosition", this.getDashboardLegend().getLegendYPosition());
      json.put("groupedInLegend", this.getDashboardLegend().getGroupedInLegend());
      json.put("featureStrategy", getFeatureStrategy());
      json.put("mdAttributeId", this.getMdAttributeId());
      json.put("attributeType", this.getAttributeType());
      json.put("aggregationMethod", this.getAggregationMethod());
      json.put("aggregationAttribute", this.getAttribute());
      json.put("layerType", layerType);
      json.put("attributeLabel", this.getAttributeDisplayLabel());
      json.put("geoNodeId", this.getGeoNodeId());
      json.put("aggregationStrategy", this.getAggregationStrategy().getJSON());

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
    MdAttributeDAOIF mdAttribute = this.getMdAttributeDAO().getMdAttributeConcrete();

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
  
  
  public static String getGeoNodeGeometryTypesJSON(String geoNodeId)
  {
    JSONArray geomTypesJSONArr = new JSONArray();
    JSONObject geomTypesJSON = new JSONObject();
    
    GeoNode geoNode = GeoNode.get(geoNodeId);
    MdAttribute geomAttr = geoNode.getGeometryAttribute();
    if(geomAttr != null){
      geomTypesJSONArr.put(geomAttr.getType());
    }
    MdAttributePoint pointAttr = geoNode.getPointAttribute();
    if(pointAttr != null){
      geomTypesJSONArr.put(pointAttr.getType());
    }
    MdAttributeMultiPolygon polyAttr = geoNode.getMultiPolygonAttribute();
    if(polyAttr != null){
      geomTypesJSONArr.put(polyAttr.getType());
    }
    
    return geomTypesJSONArr.toString();
  }

  public String getCategoryLabel(String categoryId)
  {
    AggregationStrategy strategy = this.getAggregationStrategy();
    GeoNode geoNode = this.getGeoNode();

    return strategy.getCategoryLabel(geoNode, categoryId);
  }

}
