package com.runwaysdk.geodashboard.gis.persist;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.generation.NameConventionUtil;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.geodashboard.gis.EmptyLayerInformation;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.model.FeatureStrategy;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.gis.sld.SLDConstants;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.F;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableDecimal;
import com.runwaysdk.query.SelectableDouble;
import com.runwaysdk.query.SelectableFloat;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.util.IDGenerator;
import com.runwaysdk.util.IdParser;

public class DashboardLayer extends DashboardLayerBase implements
    com.runwaysdk.generation.loader.Reloadable, Layer
{
  private static final long  serialVersionUID = 1992575686;

  public static final String DB_VIEW_PREFIX   = "layer$";

  private static final Log   log              = LogFactory.getLog(DashboardLayer.class);
  
  private boolean viewHasData = true;
  
  public DashboardLayer()
  {
    super();
  }
  
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
  
  public boolean viewHasData() {
    return viewHasData;
  }
  
  @Override
  public String applyWithStyle(DashboardStyle style, String mapId, DashboardCondition condition)
  {
    this.applyWithStyleInTransaction(style, mapId, condition);
    
    // We have to make sure that the transaction has ended before we can publish to geoserver, otherwise our database view won't exist yet.
    this.publish();
    GeoserverFacade.pushUpdates();
    
    try {
      
      JSONObject json = this.toJSON();     
      
      JSONArray jsonArray = new JSONArray();
      List<? extends DashboardStyle> styles = this.getStyles();
      for (int i = 0; i < styles.size(); ++i) {
        DashboardStyle stile = styles.get(i);
        jsonArray.put(stile.toJSON());
      }
      json.put("styles", jsonArray);

      return json.toString();
    }
    catch (JSONException e) {
      throw new ProgrammingErrorException(e);
    }
    
//    DashboardLayerView view = new DashboardLayerView();
//    view.setLayerId(this.getId());
//    view.setLayerName(this.getName());
//    view.setViewName(this.getViewName());
//    view.setSldName(this.getSLDName());
//    return view;
  }
  @Transaction
  public void applyWithStyleInTransaction(DashboardStyle style, String mapId, DashboardCondition condition) {
    boolean isNew = this.isNew();
    if (isNew && style instanceof DashboardThematicStyle)
    {
      // FIXME UI needs to allow for picking of the geo entity attribute
      DashboardThematicStyle tStyle = (DashboardThematicStyle) style;
      MdClass mdClass = tStyle.getMdAttribute().getAllDefiningClass().getAll().get(0);
      MdClassDAO md = (MdClassDAO) MdClassDAO.get(mdClass.getId());
      MdAttributeDAOIF attr = null;
      
      for(MdAttributeDAOIF mdAttr : md.definesAttributes())
      {
        if(mdAttr instanceof MdAttributeReferenceDAOIF)
        {
          MdAttributeReferenceDAOIF mdRef = (MdAttributeReferenceDAOIF) mdAttr;
          if(mdRef.getReferenceMdBusinessDAO().definesType().equals(GeoEntity.CLASS))
          {
            attr = mdRef;
            break;
          }
        }
      }
      
      if(attr != null)
      {
        this.setValue(DashboardLayer.GEOENTITY, attr.getId());
      }
      else
      {
        throw new ProgrammingErrorException("Class ["+mdClass.definesType()+"] does not referenced ["+GeoEntity.CLASS+"].");
      }
    }
    
    // We have to generate a new viewName for us on every apply because otherwise there's browser-side caching that won't show the new style update.
    String vn = DB_VIEW_PREFIX + IDGenerator.nextID();
    this.setViewName(vn);
    this.setVirtual(true);
    
//    boolean isLocked = false;
//    if (!style.isNew()) {
//      style.appLock();
//      isLocked = true;
//    }
    style.generateName(this.getViewName());
    
    if (condition != null) {
      condition.apply();
      
      if (style instanceof DashboardThematicStyle) {
        ((DashboardThematicStyle)style).setStyleCondition(condition);
      }
    }
    
    style.apply();
//    if (isLocked) {
//      style.unlock();
//    }
    
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
  
  public void validate() {
//    String geoIdColumnName = GeoEntity.getIdMd().getColumnName();
//    
//    // make sure there are no duplicate geo entities
//    String countSQL = "SELECT COUNT(*) " + Database.formatColumnAlias("ct") + " FROM " + ((MdEntity)this.getMdClass()).getTableName();
//    countSQL += " GROUP BY " + geoIdColumnName + " HAVING COUNT(*) > 1";
//
//    ResultSet resultSet = Database.query(countSQL);
//
//    try
//    {
//      if (resultSet.next())
//      {
//        // We have duplicate data! Throw an exception if this is the base
//        // layer,
//        // but only omit the layer with info if non-base.
//        if (i == 0)
//        {
//          DuplicateMapDataException ex = new DuplicateMapDataException();
//          throw ex;
//        }
//        else
//        {
//          LayerOmittedDuplicateDataInformation info = new LayerOmittedDuplicateDataInformation();
//          info.setLayerName(layerName);
//          info.throwIt();
//         
//          continue;
//        }
//      }
//    }
//    catch (SQLException sqlEx1)
//    {
//      Database.throwDatabaseException(sqlEx1);
//    }
//    finally
//    {
//      try
//      {
//        java.sql.Statement statement = resultSet.getStatement();
//        resultSet.close();
//        statement.close();
//      }
//      catch (SQLException sqlEx2)
//      {
//        Database.throwDatabaseException(sqlEx2);
//      }
//    }
  }
  
  /**
   * Removes the layer, and all its styles, from GeoServer.
   */
  public void drop() {
    if (this.isPublished()) {
      GeoserverFacade.dropLayerOnUpdate(this);
    }
  }
  
  public boolean isPublished() {
    return GeoserverFacade.layerExists(this.getViewName());
  }
  
  /**
   * Publishes the layer and all its styles to GeoServer, creating a new database view that GeoServer will read, if it does not exist yet.
   */
  public void publish() {
    if (needsRepublish()) {
      this.drop();
      
      createDatabaseView(true);
      
      if (viewHasData) {
        GeoserverFacade.publishLayerOnUpdate(this);
      }
      
      this.appLock();
      this.setLastPublishDate(new Date());
      this.apply();
      this.unlock();
    }
  }
  
  public boolean needsRepublish() {
    boolean isPublished = isPublished();
    if (!isPublished) { return true; }
    
    Date lastUpdate = this.getLastUpdateDate();
    Date lastPublish = this.getLastPublishDate();
    if (lastPublish == null || lastUpdate.after(lastPublish)) {
      return true;
    }
    
    return false;
  }
  
  public void createDatabaseView(boolean force) {
    Boolean viewExists = GeoserverFacade.viewExists(this.getViewName());
    
    if (force || !viewExists) {
      String sql = this.asValueQuery().getSQL();
      
      if (viewExists) {
        Database.dropView(this.getViewName(), sql, false);
      }
      Database.createView(this.getViewName(), sql);
    }
  }
  
  public ValueQuery asValueQuery()
  {
    QueryFactory f = new QueryFactory();
    ValueQuery v = new ValueQuery(f);

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
          
          MdAttributeConcrete mdAttr = (MdAttributeConcrete) tStyle.getMdAttribute();
          MdAttributeConcrete mdC = (MdAttributeConcrete) mdAttr;
          MdClass mdClass = mdC.getDefiningMdClass();
          EntityQuery entityQ = f.businessQuery(mdClass.definesType());
          
          // thematic attribute
          Attribute thematicAttr = entityQ.get(mdC.getAttributeName());
          
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

            isAggregate = true;
          }
          
          // If we doing a bubble/gradient map with a min/max add window aggregations
          // to provide the min and max of the attribute.
          AllLayerType layerType = this.getLayerType().get(0);
          if(layerType == AllLayerType.BUBBLE || layerType == AllLayerType.GRADIENT)
          {
            String minCol = SLDConstants.getMinProperty(attribute);
            String maxCol = SLDConstants.getMaxProperty(attribute);
            
            Selectable min = v.aSQLAggregateDouble(minCol, "MIN("+thematicSel.getDbQualifiedName()+") OVER()", minCol);
            min.setColumnAlias(minCol);
            
            Selectable max = v.aSQLAggregateDouble(maxCol, "MAX("+thematicSel.getDbQualifiedName()+") OVER()", maxCol);
            max.setColumnAlias(maxCol);
            
            v.SELECT(min, max);
            
            // Because we're using the window functions we must group by the thematic variable, or rather an alias to it
            SelectableSingle groupBy = v.aSQLDouble(thematicSel._getAttributeName()+"_GROUP_BY", thematicSel.getDbQualifiedName());
            groupBy.setColumnAlias(thematicSel.getDbQualifiedName());
            v.GROUP_BY(groupBy);
            
            // Don't include null values in bubble/gradient maps as it throws errors in geoserver (maybe there's an SLD trick for this)
            v.WHERE(v.aSQLCharacter("null_check", thematicAttr.getDbQualifiedName()+" IS NOT NULL").EQ("true"));
          }

          if (thematicSel instanceof SelectableDouble || thematicSel instanceof SelectableDecimal
              || thematicSel instanceof SelectableFloat)
          {
            Integer length = GeoserverProperties.getDecimalLength();
            Integer precision = GeoserverProperties.getDecimalPrecision();
            
            String sql = thematicSel.getSQL()
                + "::decimal(" + length + "," + precision + ")";
            
            if(isAggregate)
            {
              thematicSel = v.aSQLAggregateDouble(thematicSel._getAttributeName(), sql,
                  mdC.getAttributeName(), mdC.getDisplayLabel().getDefaultValue());
            }
            else
            {
              thematicSel = v.aSQLDouble(thematicSel._getAttributeName(), sql,
                  mdC.getAttributeName(), mdC.getDisplayLabel().getDefaultValue());
            }
          }
          
          thematicSel.setColumnAlias(attribute);
          
          v.SELECT(thematicSel);
          
          // geoentity label
          GeoEntityQuery geQ = new GeoEntityQuery(v);
          Selectable label = geQ.getDisplayLabel().localize();
          label.setColumnAlias(GeoEntity.DISPLAYLABEL);
          v.SELECT(label);
          
          // geo id (for uniqueness)
          Selectable geoId = geQ.getGeoId(GeoEntity.GEOID);
          geoId.setColumnAlias(GeoEntity.GEOID);
          v.SELECT(geoId);
          
          // geometry
          Selectable geom;
          if (this.getFeatureType().equals(FeatureType.POINT))
          {
            geom = geQ.get(GeoEntity.GEOPOINT);
          }
          else
          {
            geom = geQ.get(GeoEntity.GEOMULTIPOLYGON);
          }
          
          geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
          geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);
          
          v.SELECT(geom);
          
          // Join the entity's GeoEntity reference with the all paths table
          MdAttributeReference geoRef = this.getGeoEntity();
          Attribute geoAttr = entityQ.get(geoRef.getAttributeName());
          
          // the entity's GeoEntity should match the all path's child
          GeoEntityAllPathsTableQuery geAllPathsQ = new GeoEntityAllPathsTableQuery(v);
          v.WHERE(geoAttr.LEFT_JOIN_EQ(geAllPathsQ.getChildTerm()));
          
          // the displayed GeoEntity should match the all path's parent
          v.AND(geAllPathsQ.getParentTerm().EQ(geQ));
          
          // make sure the parent GeoEntity is of the proper Universal
          Universal universal = this.getUniversal();
          v.AND(geQ.getUniversal().EQ(universal));
          
          // Attribute condition filtering (i.e. sales unit is greater than 50)
          DashboardCondition condition = tStyle.getStyleCondition();
          if (condition != null && thematicAttr instanceof AttributeNumber) {
            v.AND(condition.asRunwayQuery(thematicAttr));
          }
        }
      }
    }
    finally
    {
      iter.close();
    }
    
    if (log.isDebugEnabled())
    {
      // print the SQL if the generated
      log.debug("SLD for Layer [%s], this:\n" + v.getSQL());
    }
    
    viewHasData = true;
    if (v.getCount() == 0) {
      EmptyLayerInformation info = new EmptyLayerInformation();
      info.apply();
      
      info.throwIt();
      
      viewHasData = false;
    }
    
    return v;
  }

  @Override
  protected String buildKey()
  {
    String name = this.getName();
    String idRoot = IdParser.parseRootFromId(this.getId());
    String keyName = NameConventionUtil.buildAttribute(name, idRoot + "_");
    return keyName;
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

  public JSONObject toJSON() {
    try {
      JSONObject json = new JSONObject();
      json.put("viewName", getViewName());
      json.put("sldName", getSLDName());
      json.put("layerName", getName());
      json.put("layerId", getId());
      json.put("inLegend", this.getDisplayInLegend());
      json.put("legendXPosition", this.getDashboardLegend().getLegendXPosition());
      json.put("legendYPosition", this.getDashboardLegend().getLegendYPosition());
      
      JSONArray jsonStyles = new JSONArray();
      List<? extends DashboardStyle> styles = this.getStyles();
      for (int i = 0; i < styles.size(); ++i) {
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
  public void updateLegend(Integer legendXPosition, Integer legendYPosition)
  {
    this.appLock();
    this.getDashboardLegend().setLegendXPosition(legendXPosition);
    this.getDashboardLegend().setLegendYPosition(legendYPosition);
    this.apply();
  }
}
