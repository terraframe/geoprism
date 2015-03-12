package com.runwaysdk.geodashboard.gis.persist;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
// import org.hsqldb.lib.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.geodashboard.QueryUtil;
import com.runwaysdk.geodashboard.SessionParameterFacade;
import com.runwaysdk.geodashboard.gis.EmptyLayerInformation;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.model.FeatureStrategy;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.LocationCondition;
import com.runwaysdk.geodashboard.util.CollectionUtil;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.F;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableDecimal;
import com.runwaysdk.query.SelectableDouble;
import com.runwaysdk.query.SelectableFloat;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.util.IDGenerator;

public class DashboardLayer extends DashboardLayerBase implements com.runwaysdk.generation.loader.Reloadable, Layer
{
  private static final long        serialVersionUID = 1992575686;

  public static final String       DB_VIEW_PREFIX   = "lv_";

  private static final Log         log              = LogFactory.getLog(DashboardLayer.class);

  private List<DashboardCondition> conditions       = null;

  private boolean                  viewHasData      = true;

  @Override
  public FeatureStrategy getFeatureStrategy()
  {
    AllLayerType type = this.getLayerType().get(0);
    return FeatureStrategy.valueOf(type.name());
  }

  @Override
  public void apply()
  {
    super.apply();
  }

  public boolean viewHasData()
  {
    return viewHasData;
  }

  public void setConditions(List<DashboardCondition> conditions)
  {
    this.conditions = conditions;
  }

  public List<DashboardCondition> getConditions()
  {
    return this.conditions;
  }

  @Override
  public String applyWithStyle(DashboardStyle style, String mapId, DashboardCondition[] conditions)
  {
    this.applyWithStyleInTransaction(style, mapId, conditions);

    // We have to make sure that the transaction has ended before we can publish to geoserver,
    // otherwise our database view won't exist yet.
    //
    // The false flag is set in publish(createDBView) to allow for running the createDatabaseView
    // method inside the applyWithStyleInTransaction method so that incorrect SQL for view
    // creation is caught before database object are created. Originally noticed on text attribute
    // layer creation
    this.publish(true);
    GeoserverFacade.pushUpdates();

    try
    {
      JSONObject json = this.toJSON();

      JSONArray jsonArray = new JSONArray();
      List<? extends DashboardStyle> styles = this.getStyles();
      for (int i = 0; i < styles.size(); ++i)
      {
        DashboardStyle stile = styles.get(i);
        jsonArray.put(stile.toJSON());
      }
      json.put("styles", jsonArray);

      return json.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Transaction
  public void applyWithStyleInTransaction(DashboardStyle style, String mapId, DashboardCondition[] conditions)
  {
    boolean isNew = this.isNew();

    // Find (and set) the GeoEntity reference attribute
    // FIXME UI needs to allow for picking of the geo entity attribute
    if (isNew && style instanceof DashboardThematicStyle)
    {
      DashboardThematicStyle tStyle = (DashboardThematicStyle) style;
      MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(tStyle.getMdAttributeId());
      MdClassDAOIF mdClass = mdAttribute.definedByClass();
      MdAttributeDAOIF attr = QueryUtil.getGeoEntityAttribute(mdClass);

      if (attr != null)
      {
        this.setValue(DashboardLayer.GEOENTITY, attr.getId());
      }
      else
      {
        throw new ProgrammingErrorException("Class [" + mdClass.definesType() + "] does not reference a [" + GeoEntity.CLASS + "].");
      }
    }

    // We have to generate a new viewName for us on every apply because otherwise there's browser-side caching that
    // won't show the new style update.
    String vn = generateViewName();
    this.setViewName(vn);
    this.setDashboardMap(DashboardMap.get(mapId));
    this.setVirtual(true);

    style.setName(this.getViewName());

    if (conditions != null)
    {
      this.conditions = Arrays.asList(conditions);
    }

    style.apply();

    this.apply();

    // Create hasLayer and hasStyle relationships
    if (isNew)
    {
      QueryFactory f = new QueryFactory();
      DashboardLayerQuery q = new DashboardLayerQuery(f);
      DashboardMapQuery mQ = new DashboardMapQuery(f);

      mQ.WHERE(mQ.getId().EQ(mapId));
      q.WHERE(q.containingMap(mQ));

      int count = (int) q.getCount();
      count++;

      DashboardMap map = DashboardMap.get(mapId);
      HasLayer hasLayer = map.addHasLayer(this);
      hasLayer.setLayerIndex(count);
      hasLayer.apply();

      HasStyle hasStyle = this.addHasStyle(style);
      hasStyle.apply();
    }

    this.validate();
  }

  public String generateViewName()
  {
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      String sessionId = session.getId();

      // The max length for a postgres table name is 63 characters, and as a result our metadata is set at max length 63
      // as well.

      String vn = DB_VIEW_PREFIX + sessionId + "_" + IDGenerator.nextID().substring(0, 10);

      return vn;
    }

    return DB_VIEW_PREFIX + IDGenerator.nextID().substring(0, 10);
  }

  /**
   * For easy reference, the name of the SLD is the same as the db view name. The .sld extension is automatically added
   * 
   * @return
   */
  public String getSLDName()
  {
    return this.getViewName();
  }

  /**
   * Returns the File object associated with the SLD for this view.
   * 
   * @return
   */
  public File getSLDFile()
  {
    String path = GeoserverProperties.getGeoserverSLDDir();
    String sld = path + this.getSLDName() + GeoserverProperties.SLD_EXTENSION;
    return new File(sld);
  }

  public void validate()
  {
    try
    {
      // Ensure the generated query is valid and executes
      Database.query(this.getViewQuery().getSQL());

      // Create the database view
      createDatabaseView(true);

      // Ensure there is a valid bounding box
      GeoserverFacade.getBBOX(this.getViewName());
    }
    catch (Exception e)
    {
      // If this happens it means the SQL generated wrong and coding will be required to fix.

      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Removes the layer, and all its styles, from GeoServer.
   */
  public void drop()
  {
    if (this.isPublished())
    {
      GeoserverFacade.dropLayerOnUpdate(this);
    }
  }

  public boolean isPublished()
  {
    return GeoserverFacade.layerExists(this.getViewName());
  }

  /**
   * Publishes the layer and all its styles to GeoServer, creating a new database view that GeoServer will read, if it
   * does not exist yet.
   */
  public void publish(boolean createDBView)
  {
    this.drop();

    if (createDBView)
    {
      createDatabaseView(true);
    }

    if (viewHasData)
    {
      GeoserverFacade.publishLayerOnUpdate(this);
    }

    this.appLock();
    this.setLastPublishDate(new Date());
    this.apply();
    this.unlock();
  }

  public void createDatabaseView(boolean force)
  {
    String sql = this.getViewQuery().getSQL();

    Database.dropView(this.getViewName(), sql, false);

    Database.createView(this.getViewName(), sql);
  }

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

      // String minAttr = SLDConstants.getMinProperty(attribute);
      // String maxAttr = SLDConstants.getMaxProperty(attribute);

      // String minAttr = "min_numberofunits";
      // String maxAttr = "max_numberofunits";

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
  public String getViewName()
  {
    if (!SessionParameterFacade.containsKey(this.getId()))
    {
      SessionParameterFacade.put(this.getId(), this.generateViewName());
    }

    return SessionParameterFacade.get(this.getId());
  }

  public void setViewName(String value)
  {
    SessionParameterFacade.put(this.getId(), value);
  }

  /**
   * @prerequisite conditions is populated with any DashboardConditions necessary for restricting the view dataset.
   * 
   * @return A ValueQuery for use in creating/dropping the database view which will be used with GeoServer.
   */
  public ValueQuery getViewQuery()
  {
    QueryFactory factory = new QueryFactory();
    ValueQuery innerQuery1 = new ValueQuery(factory);
    ValueQuery innerQuery2 = new ValueQuery(factory);

    ValueQuery outerQuery = new ValueQuery(factory);

    OIterator<? extends DashboardStyle> iter = this.getAllHasStyle();
    try
    {
      while (iter.hasNext())
      {
        DashboardStyle style = iter.next();

        // IMPORTANT - Everything is going to be a 'thematic layer' in IDE,
        // but we need to define a non-thematic's behavior or even finalize
        // on the semantics of a layer without a thematic attribute...which might
        // not even exist!
        if (style instanceof DashboardThematicStyle)
        {
          DashboardThematicStyle tStyle = (DashboardThematicStyle) style;
          String attribute = tStyle.getAttribute();
          MdAttributeDAOIF mdAttributeDAO = tStyle.getMdAttributeDAO();

          MdClassDAOIF mdClass = mdAttributeDAO.definedByClass();

          GeneratedComponentQuery query = QueryUtil.getQuery(mdClass, factory);

          // thematic attribute
          String attributeName = mdAttributeDAO.definesAttribute();
          String displayLabel = mdAttributeDAO.getDisplayLabel(Session.getCurrentLocale());

          Attribute thematicAttr = QueryUtil.get(query, attributeName);

          // use the basic Selectable if no aggregate is selected
          Selectable thematicSel = thematicAttr;
          List<AllAggregationType> allAgg = tStyle.getAggregationType();
          boolean isAggregate = false;
          if (allAgg.size() == 1)
          {
            AllAggregationType agg = allAgg.get(0);
            // String func = null;
            if (agg == AllAggregationType.SUM)
            {
              // func = "SUM";
              thematicSel = F.SUM(thematicAttr);
            }
            else if (agg == AllAggregationType.MIN)
            {
              // func = "MIN";
              thematicSel = F.MIN(thematicAttr);
            }
            else if (agg == AllAggregationType.MAX)
            {
              // func = "MAX";
              thematicSel = F.MAX(thematicAttr);
            }
            else if (agg == AllAggregationType.AVG)
            {
              // func = "AVG";
              thematicSel = F.AVG(thematicAttr);
            }
            // else if (agg == AllAggregationType.MAJORITY){
            // // func = "MAJORITY";
            // thematicSel = F.SUM(thematicAttr);
            // }
            // else if (agg == AllAggregationType.MINORITY){
            // // func = "MINORITY";
            // thematicSel = F.COUNT(thematicAttr);
            // }

            isAggregate = true;
          }

          // If we doing a bubble/gradient map with a min/max add window aggregations
          // to provide the min and max of the attribute.
          // AllLayerType layerType = this.getLayerType().get(0);
          // if(layerType == AllLayerType.BUBBLE)
          // {
          // String minCol = SLDConstants.getMinProperty(attribute);
          // String maxCol = SLDConstants.getMaxProperty(attribute);
          //
          // Selectable min = v.aSQLAggregateDouble(minCol, "MIN("+thematicSel.getDbQualifiedName()+") OVER()", minCol);
          // min.setColumnAlias(minCol);
          //
          // Selectable max = v.aSQLAggregateDouble(maxCol, "MAX("+thematicSel.getDbQualifiedName()+") OVER()", maxCol);
          // max.setColumnAlias(maxCol);
          //
          // v.SELECT(min, max);
          //
          // // Because we're using the window functions we must group by the thematic variable, or rather an alias to
          // it
          // SelectableSingle groupBy = v.aSQLDouble(thematicSel.getResultAttributeName()+"_GROUP_BY",
          // thematicSel.getDbQualifiedName());
          // groupBy.setColumnAlias(thematicSel.getDbQualifiedName());
          // v.GROUP_BY(groupBy);
          //
          // // Don't include null values in bubble/gradient maps as it throws errors in geoserver (maybe there's an SLD
          // trick for this)
          // v.WHERE(v.aSQLCharacter("null_check", thematicAttr.getDbQualifiedName()+" IS NOT NULL").EQ("true"));
          // }

          if (thematicSel instanceof SelectableDouble || thematicSel instanceof SelectableDecimal || thematicSel instanceof SelectableFloat)
          {
            Integer length = GeoserverProperties.getDecimalLength();
            Integer precision = GeoserverProperties.getDecimalPrecision();

            String sql = thematicSel.getSQL() + "::decimal(" + length + "," + precision + ")";

            if (isAggregate)
            {
              thematicSel = innerQuery1.aSQLAggregateDouble(thematicSel.getResultAttributeName(), sql, attributeName, displayLabel);
            }
            else
            {
              thematicSel = innerQuery1.aSQLDouble(thematicSel.getResultAttributeName(), sql, attributeName, displayLabel);
            }
          }

          thematicSel.setColumnAlias(attribute);

          innerQuery1.SELECT(thematicSel);

          // geoentity label
          GeoEntityQuery geQ1 = new GeoEntityQuery(innerQuery1);
          Selectable label = geQ1.getDisplayLabel().localize();
          label.setColumnAlias(GeoEntity.DISPLAYLABEL);
          innerQuery1.SELECT(label);

          // geo id (for uniqueness)
          Selectable geoId1 = geQ1.getGeoId(GeoEntity.GEOID);
          geoId1.setColumnAlias(GeoEntity.GEOID);
          innerQuery1.SELECT(geoId1);

          // Join the entity's GeoEntity reference with the all paths table
          MdAttributeReferenceDAOIF geoRef = MdAttributeReferenceDAO.get(this.getGeoEntityId());
          Attribute geoAttr = QueryUtil.get(query, geoRef.definesAttribute());

          // the entity's GeoEntity should match the all path's child
          GeoEntityAllPathsTableQuery geAllPathsQ = new GeoEntityAllPathsTableQuery(innerQuery1);
          innerQuery1.WHERE(geoAttr.LEFT_JOIN_EQ(geAllPathsQ.getChildTerm()));

          // the displayed GeoEntity should match the all path's parent
          innerQuery1.AND(geAllPathsQ.getParentTerm().EQ(geQ1));

          // make sure the parent GeoEntity is of the proper Universal
          Universal universal = this.getUniversal();
          innerQuery1.AND(geQ1.getUniversal().EQ(universal));

          // Attribute condition filtering (i.e. sales unit is greater than 50)
          if (conditions != null)
          {
            for (DashboardCondition condition : conditions)
            {
              if (condition instanceof DashboardAttributeCondition)
              {
                String mdAttributeId = ( (DashboardAttributeCondition) condition ).getDefiningMdAttributeId();

                MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId);
                MdClassDAOIF definedByClass = mdAttribute.definedByClass();

                if (definedByClass.getId().equals(mdClass.getId()))
                {
                  Attribute attr = QueryUtil.get(query, mdAttribute.definesAttribute());
                  condition.restrictQuery(factory, innerQuery1, attr);
                }
              }
              else if (condition instanceof LocationCondition)
              {
                condition.restrictQuery(factory, innerQuery1, geoAttr);
              }
            }
          }

        } // if (style instanceof DashboardThematicStyle)
      } // while (iter.hasNext())
    }
    finally
    {
      iter.close();
    }

    if (log.isDebugEnabled())
    {
      // print the SQL if the generated
      log.debug("SLD for Layer [%s], this:\n" + innerQuery1.getSQL());
    }

    viewHasData = true;
    if (innerQuery1.getCount() == 0)
    {
      EmptyLayerInformation info = new EmptyLayerInformation();
      info.setLayerName(this.getName());
      info.apply();

      info.throwIt();

      viewHasData = false;
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

  @Override
  protected String buildKey()
  {
    if (this.getDashboardMapId() != null && this.getDashboardMapId().length() > 0)
    {
      return this.getDashboardMapId() + "-" + this.getName();
    }

    /*
     * The apply will fail because dashboard map is a required field. However, in order to give the user a better error
     * message we still need to populate the key with value.
     */
    return this.getId();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public void setGeoEntity(MdAttributeReference value)
  {
    if (value.getMdBusiness().definesType().equals(GeoEntity.CLASS))
    {
      super.setGeoEntity(value);
    }
    else
    {
      throw new ProgrammingErrorException("The attribute [" + DashboardLayer.GEOENTITY + "] can only reference an MdAttributeReference to [" + GeoEntity.CLASS + "]");
    }
  }

  @Override
  public void lock()
  {
    for (DashboardStyle style : this.getAllHasStyle())
    {
      style.lock();
    }

    super.lock();
  }

  @Override
  public void unlock()
  {
    for (DashboardStyle style : this.getAllHasStyle())
    {
      style.unlock();
    }

    super.unlock();
  }

  @Override
  @Transaction
  public void delete()
  {
    for (DashboardStyle style : this.getAllHasStyle())
    {
      style.delete();
    }

    super.delete();
  }

  @Override
  public List<? extends DashboardStyle> getStyles()
  {
    return this.getAllHasStyle().getAll();
  }

  public static UniversalQuery getSortedUniversals()
  {
    QueryFactory f = new QueryFactory();
    UniversalQuery q = new UniversalQuery(f);

    Universal root = Universal.getRoot();
    q.WHERE(q.getId().NE(root.getId()));

    q.ORDER_BY_ASC(q.getDisplayLabel().localize());

    return q;
  }

  @Override
  public FeatureType getFeatureType()
  {
    // LayerType is required so it's safe to assume access to the object
    AllLayerType type = this.getLayerType().get(0);
    if (type == AllLayerType.BUBBLE)
    {
      return FeatureType.POINT;
    }
    else
    {
      return FeatureType.POLYGON;
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

      // Getting the aggregation method (i.e. avg, sum, min, max) and aggregation attribute
      // (i.e. numberofunits) for the style representation
      OIterator<? extends DashboardStyle> iter = this.getAllHasStyle();
      try
      {
        while (iter.hasNext())
        {
          DashboardStyle style = iter.next();
          DashboardThematicStyle tStyle = (DashboardThematicStyle) style;
          String aggregationAttribute = tStyle.getAttribute();
          List<AllAggregationType> allAgg = tStyle.getAggregationType();
          AllAggregationType aggregationMethod = allAgg.get(0);

          json.put("aggregationMethod", aggregationMethod);
          json.put("aggregationAttribute", aggregationAttribute);
        }
      }
      finally
      {
        iter.close();
      }

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

  @Override
  @Transaction
  public void updateLegend(Integer legendXPosition, Integer legendYPosition, Boolean groupedInLegend)
  {
    this.appLock();
    this.getDashboardLegend().setLegendXPosition(legendXPosition);
    this.getDashboardLegend().setLegendYPosition(legendYPosition);
    this.getDashboardLegend().setGroupedInLegend(groupedInLegend);
    this.apply();
  }

  public static String getSessionId(String viewName)
  {
    String[] split = viewName.split("_");

    if (split != null && split.length == 3)
    {
      String sessionId = split[1];

      return sessionId;
    }

    return null;
  }
}
