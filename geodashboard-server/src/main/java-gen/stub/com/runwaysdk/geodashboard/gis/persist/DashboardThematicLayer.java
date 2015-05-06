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
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.QueryUtil;
import com.runwaysdk.geodashboard.gis.EmptyLayerInformation;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.model.AttributeType;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicLayer;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.util.CollectionUtil;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeChar;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeMoment;
import com.runwaysdk.system.metadata.MdAttributeTerm;
import com.runwaysdk.system.metadata.MdAttributeText;
import com.runwaysdk.system.metadata.MdAttributeVirtual;

public class DashboardThematicLayer extends DashboardThematicLayerBase implements Reloadable, ThematicLayer
{
  private static final long serialVersionUID = -810007054;

  public DashboardThematicLayer()
  {
    super();
  }

  @Transaction
  public void applyAll(DashboardStyle style, String mapId, DashboardCondition[] conditions)
  {
    boolean isNew = this.isNew();

    if (isNew)
    {
      MdAttributeDAOIF mdAttribute = this.getMdAttributeDAO();
      MdClassDAOIF mdClass = mdAttribute.definedByClass();
      MdAttributeDAOIF attr = QueryUtil.getGeoEntityAttribute(mdClass);

      if (attr == null)
      {
        throw new ProgrammingErrorException("Unable to find a Geo Entity attribute on type [" + mdClass.definesType() + "].");
      }
    }

    super.applyAll(style, mapId, conditions);
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
      json.put("layerType", "THEMATICLAYER");
      json.put("attributeLabel", this.getAttributeDisplayLabel());

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
    QueryFactory factory = new QueryFactory();
    ValueQuery innerQuery2 = new ValueQuery(factory);

    ValueQuery outerQuery = new ValueQuery(factory);

    ValueQuery innerQuery1 = this.getInnerQuery(factory);

    if (log.isDebugEnabled())
    {
      // print the SQL if the generated
      log.debug("SLD for Layer [%s], this:\n" + innerQuery1.getSQL());
    }

    this.viewHasData = true;
    if (innerQuery1.getCount() == 0)
    {
      EmptyLayerInformation info = new EmptyLayerInformation();
      info.setLayerName(this.getName());
      info.apply();

      info.throwIt();

      this.viewHasData = false;
    }

    // Set the GeoID and the Geometry attribute for the second query
    GeoEntityQuery geQ2 = new GeoEntityQuery(innerQuery2);
    Selectable geoId2 = geQ2.getGeoId(GeoEntity.GEOID);
    geoId2.setColumnAlias(GeoEntity.GEOID);
    innerQuery2.SELECT(geoId2);
    // geometry
    Selectable geom;
    if (this.getFeatureType().equals(FeatureType.POINT))
    {
      geom = geQ2.get(GeoEntity.GEOPOINT);
    }
    else
    {
      geom = geQ2.get(GeoEntity.GEOMULTIPOLYGON);
    }

    geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);
    innerQuery2.SELECT(geom);

    for (Selectable selectable : innerQuery1.getSelectableRefs())
    {
      Attribute attribute = innerQuery1.get(selectable.getResultAttributeName());
      attribute.setColumnAlias(selectable.getColumnAlias());

      outerQuery.SELECT(attribute);
    }

    Attribute geomAttribute = innerQuery2.get(GeoserverFacade.GEOM_COLUMN);
    geomAttribute.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    outerQuery.SELECT(geomAttribute);
    outerQuery.WHERE(innerQuery2.aCharacter(GeoEntity.GEOID).EQ(innerQuery1.aCharacter(GeoEntity.GEOID)));

    return outerQuery;
  }

  private ValueQuery getInnerQuery(QueryFactory factory)
  {
    MdAttributeDAOIF thematicMdAttribute = this.getMdAttributeDAO();
    AllAggregationType thematicAggregation = this.getAggregationMethod();
    Universal universal = this.getUniversal();
    List<DashboardCondition> conditions = this.getConditions();

    DashboardStyle style = this.getStyle();
    // IMPORTANT - Everything is going to be a 'thematic layer' in IDE,
    // but we need to define a non-thematic's behavior or even finalize
    // on the semantics of a layer without a thematic attribute...which might
    // not even exist!
    if (style instanceof DashboardThematicStyle)
    {
      DashboardThematicStyle tStyle = (DashboardThematicStyle) style;

      ValueQuery thematicQuery = QueryUtil.getThematicValueQuery(factory, thematicMdAttribute, thematicAggregation, universal, conditions);

      MdAttributeDAOIF secondaryMdAttribute = tStyle.getSecondaryAttributeDAO();

      if (secondaryMdAttribute != null)
      {
        AttributeCharacter thematicGeoId = thematicQuery.aCharacter(GeoEntity.GEOID);
        thematicGeoId.setColumnAlias(GeoEntity.GEOID);

        AttributeCharacter thematicLabel = thematicQuery.aCharacter(GeoEntity.DISPLAYLABEL);
        thematicLabel.setColumnAlias(GeoEntity.DISPLAYLABEL);

        Attribute thematicAttribute = thematicQuery.get(thematicMdAttribute.definesAttribute());
        thematicAttribute.setColumnAlias(thematicMdAttribute.definesAttribute());

        ValueQuery innerQuery = new ValueQuery(factory);
        innerQuery.SELECT(thematicGeoId, thematicLabel, thematicAttribute);

        if (!secondaryMdAttribute.getId().equals(thematicMdAttribute.getId()))
        {
          ValueQuery secondaryQuery = QueryUtil.getThematicValueQuery(factory, secondaryMdAttribute, tStyle.getSecondaryAttributeAggregationMethod(), universal, conditions);

          AttributeCharacter secondaryGeoId = secondaryQuery.aCharacter(GeoEntity.GEOID);
          secondaryGeoId.setColumnAlias(GeoEntity.GEOID);

          Attribute secondaryAttribute = secondaryQuery.get(secondaryMdAttribute.definesAttribute());
          secondaryAttribute.setColumnAlias(secondaryMdAttribute.definesAttribute());

          innerQuery.SELECT(secondaryAttribute);
          innerQuery.WHERE(thematicGeoId.LEFT_JOIN_EQ(secondaryGeoId));
        }

        return innerQuery;
      }
      else
      {
        return thematicQuery;
      }
    }
    else
    {
      return new ValueQuery(factory);
    }

  }

  private DashboardStyle getStyle()
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
    }
  }
  
  
  @Override
  protected DashboardLayer newInstance()
  {
    return new DashboardThematicLayer();
  }

}
