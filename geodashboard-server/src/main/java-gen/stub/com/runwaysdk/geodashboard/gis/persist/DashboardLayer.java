package com.runwaysdk.geodashboard.gis.persist;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.RunwayException;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.QueryUtil;
import com.runwaysdk.geodashboard.SessionParameterFacade;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverBatch;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.model.FeatureStrategy;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.util.IDGenerator;

public abstract class DashboardLayer extends DashboardLayerBase implements
    com.runwaysdk.generation.loader.Reloadable, Layer
{
  private static final long        serialVersionUID = 1992575686;

  public static final String       DB_VIEW_PREFIX   = "lv_";

  public static final Log          log              = LogFactory.getLog(DashboardLayer.class);

  public boolean                   viewHasData      = true;

  private List<DashboardCondition> conditions       = null;

  public abstract ValueQuery getViewQuery();

  public abstract JSONObject toJSON();

  public void setConditions(List<DashboardCondition> conditions)
  {
    this.conditions = conditions;
  }

  public List<DashboardCondition> getConditions()
  {
    return this.conditions;
  }

  @Override
  public FeatureStrategy getFeatureStrategy()
  {
    AllLayerType type = this.getLayerType().get(0);
    return FeatureStrategy.valueOf(type.name());
  }

  public boolean viewHasData()
  {
    return viewHasData;
  }

  public String generateViewName()
  {
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      String sessionId = session.getId();

      // The max length for a postgres table name is 63 characters, and as a
      // result our metadata is set at max length 63
      // as well.

      String vn = DB_VIEW_PREFIX + sessionId + "_" + IDGenerator.nextID().substring(0, 10);

      return vn;
    }

    return DB_VIEW_PREFIX + IDGenerator.nextID().substring(0, 10);
  }

  @Override
  public String applyWithStyle(DashboardStyle style, String mapId, DashboardCondition[] conditions)
  {
    this.applyWithStyleInTransaction(style, mapId, conditions);

    // We have to make sure that the transaction has ended before we can publish
    // to geoserver,
    // otherwise our database view won't exist yet.
    //
    // The false flag is set in publish(createDBView) to allow for running the
    // createDatabaseView
    // method inside the applyWithStyleInTransaction method so that incorrect
    // SQL for view
    // creation is caught before database object are created. Originally noticed
    // on text attribute
    // layer creation

    GeoserverBatch batch = new GeoserverBatch();

    this.publish(batch, true);

    GeoserverFacade.pushUpdates(batch);

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
  protected void applyWithStyleInTransaction(DashboardStyle style, String mapId,
      DashboardCondition[] conditions)
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
        throw new ProgrammingErrorException("Class [" + mdClass.definesType()
            + "] does not reference a [" + GeoEntity.CLASS + "].");
      }
    }

    // We have to generate a new viewName for us on every apply because
    // otherwise there's browser-side caching that
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

  /**
   * Validate the layer data
   */
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
    catch (RunwayException e)
    {
      throw e;
    }
    catch (SmartException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      // If this happens it means the SQL generated wrong and coding will be
      // required to fix.
      throw new ProgrammingErrorException(e);
    }
  }

  public void createDatabaseView(boolean force)
  {
    String sql = this.getViewQuery().getSQL();

    Database.dropView(this.getViewName(), sql, false);

    Database.createView(this.getViewName(), sql);
  }

  /**
   * Publishes the layer and all its styles to GeoServer, creating a new
   * database view that GeoServer will read, if it does not exist yet.
   */
  public void publish(GeoserverBatch batch, boolean createDBView)
  {
    batch.addLayerToDrop(this);

    if (createDBView)
    {
      createDatabaseView(true);
    }

    if (viewHasData)
    {
      batch.addLayerToPublish(this);
    }
  }

  /**
   * For easy reference, the name of the SLD is the same as the db view name.
   * The .sld extension is automatically added
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

  public boolean isPublished()
  {
    return GeoserverFacade.layerExists(this.getViewName());
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

  @Override
  protected String buildKey()
  {
    if (this.getDashboardMapId() != null && this.getDashboardMapId().length() > 0)
    {
      return this.getDashboardMapId() + "-" + this.getName();
    }

    /*
     * The apply will fail because dashboard map is a required field. However,
     * in order to give the user a better error message we still need to
     * populate the key with value.
     */
    return this.getId();
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
      throw new ProgrammingErrorException("The attribute [" + DashboardLayer.GEOENTITY
          + "] can only reference an MdAttributeReference to [" + GeoEntity.CLASS + "]");
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
}