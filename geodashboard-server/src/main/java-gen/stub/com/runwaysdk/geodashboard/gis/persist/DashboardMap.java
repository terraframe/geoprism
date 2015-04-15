package com.runwaysdk.geodashboard.gis.persist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.Dashboard;
import com.runwaysdk.geodashboard.DashboardQuery;
import com.runwaysdk.geodashboard.MetadataWrapper;
import com.runwaysdk.geodashboard.MetadataWrapperQuery;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.model.Map;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class DashboardMap extends DashboardMapBase implements com.runwaysdk.generation.loader.Reloadable, Map
{
  private static Log        log              = LogFactory.getLog(DashboardMap.class);

  private static final long serialVersionUID = 861649895;

  public DashboardMap()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public List<? extends DashboardLayer> getLayers()
  {
    return this.getAllHasLayer().getAll();
  }
  

  /**
   * MdMethod
   * 
   * Invoked when the user hits "apply" on the mapping screen. This will update BIRT and republish all layers with the
   * updated filter criteria conditions.
   */
  @Override
  public String updateConditions(DashboardCondition[] conditions)
  {
    List<? extends DashboardLayer> layers = this.getLayers();

    for (DashboardLayer layer : layers)
    {
      if(layer instanceof DashboardThematicLayer)
      {
        DashboardThematicLayer tLayer = (DashboardThematicLayer) layer;
        tLayer.setConditions(Arrays.asList(conditions));
  
        generateSessionViewName(tLayer);
  
        tLayer.publish(true);
      }
    }

    GeoserverFacade.pushUpdates();

    return getMapJSON("republish=false");
  }

  private void generateSessionViewName(DashboardLayer layer)
  {
    // Generate a new database view name for the layer. This viewName is specific to a user's session.
    String viewName = layer.generateViewName();

    // Update the stored viewName for the session
    layer.setViewName(viewName);

    // Update the corresponding style name to link with the view name
    DashboardStyle style = layer.getStyles().get(0);
    style.setName(layer.getViewName());
  }

  /**
   * MdMethod
   * 
   * Invoked after the user reorders a layer via drag+drop in the dashboard viewer.
   * 
   * @return The JSON representation of the current DashboardMap.
   */
  @Override
  public java.lang.String orderLayers(java.lang.String[] layerIds)
  {
    if (layerIds == null || layerIds.length == 0)
    {
      throw new RuntimeException("Unable to order layers, the layerIds array is either null or empty.");
    }

    HasLayerQuery q = new HasLayerQuery(new QueryFactory());
    q.WHERE(q.parentId().EQ(this.getId()));
    q.AND(q.childId().IN(layerIds));

    OIterator<? extends HasLayer> iter = q.getIterator();

    try
    {
      while (iter.hasNext())
      {
        HasLayer rel = iter.next();
        rel.appLock();
        rel.setLayerIndex(ArrayUtils.indexOf(layerIds, rel.getChildId()) + 1);
        rel.apply();
      }
    }
    finally
    {
      iter.close();
    }

    return "";
  }

  /**
   * Returns the layers this map defines in the proper order.
   * 
   * @return
   */
  public DashboardLayer[] getOrderedLayers()
  {
    QueryFactory f = new QueryFactory();

    HasLayerQuery hsQ = new HasLayerQuery(f);
    hsQ.WHERE(hsQ.parentId().EQ(this.getId()));
    hsQ.ORDER_BY_ASC(hsQ.getLayerIndex());

    OIterator<? extends HasLayer> iter = hsQ.getIterator();

    try
    {
      List<DashboardLayer> layers = new LinkedList<DashboardLayer>();
      while (iter.hasNext())
      {
        layers.add(iter.next().getChild());
      }

      return layers.toArray(new DashboardLayer[layers.size()]);
    }
    finally
    {
      iter.close();
    }
  }

  /**
   * Republishes all layers to GeoServer.
   */
  public void publishAllLayers(DashboardLayer[] orderedLayers)
  {
    for (DashboardLayer layer : orderedLayers)
    {
      if(layer instanceof DashboardThematicLayer)
      {
        DashboardThematicLayer tLayer = (DashboardThematicLayer) layer;
        this.generateSessionViewName(tLayer);

        tLayer.publish(true);
      }
    }

    GeoserverFacade.pushUpdates();
  }

  /**
   * MdMethod
   */
  @com.runwaysdk.logging.Log(level = LogLevel.DEBUG)
  public String getMapJSON(String config)
  {
    try
    {
      DashboardLayer[] orderedLayers = this.getOrderedLayers();
      
      JSONObject mapJSON = new JSONObject();
      JSONArray layers = new JSONArray();
      
      mapJSON.put("mapName", this.getName());
        
      DashboardThematicLayer[] orderedTLayers = new DashboardThematicLayer[orderedLayers.length];
      for(int i=0; i<orderedLayers.length; i++)
      {
        if(orderedLayers[i] instanceof DashboardThematicLayer)
        {
          DashboardThematicLayer tLayer = (DashboardThematicLayer) orderedLayers[i];
          orderedTLayers[i] = tLayer;
        }
      }

      if (config == null || !config.equals("republish=false"))
      {
        publishAllLayers(orderedTLayers);
      }

      for (int i = 0; i < orderedTLayers.length; i++)
      {
        layers.put(orderedTLayers[i].toJSON());
      }
      mapJSON.put("layers", layers);

      JSONArray mapBBox = getMapLayersBBox(orderedTLayers);
      mapJSON.put("bbox", mapBBox);

      if (log.isDebugEnabled())
      {
        log.debug("JSON for map [" + this + "]:\n" + mapJSON.toString(4));
      }
      
      return mapJSON.toString();
    }
    catch (JSONException ex)
    {
      log.error("Could not properly form map [" + this + "] into valid JSON to send back to the client.");
      throw new ProgrammingErrorException(ex);
    }
  }

  public JSONArray getMapLayersBBox(DashboardLayer[] layers)
  {
    JSONArray bboxArr = new JSONArray();

    Dashboard dashboard = this.getDashboard();

    if (dashboard != null)
    {
      GeoEntity country = dashboard.getCountry();
      MdBusinessDAOIF mdClass = (MdBusinessDAOIF) country.getMdClass();
      MdAttributeDAOIF mdAttributeGeom = mdClass.definesAttribute(GeoEntity.GEOMULTIPOLYGON);
      MdAttributeDAOIF mdAttributeId = mdClass.definesAttribute(GeoEntity.ID);

      String tableName = mdClass.getTableName();
      String geoColumnName = mdAttributeGeom.getColumnName();
      String idColumnName = mdAttributeId.getColumnName();

      StringBuffer sql = new StringBuffer();
      sql.append("SELECT ST_AsText(ST_Extent(" + tableName + "." + geoColumnName + ")) AS bbox");
      sql.append(" FROM " + tableName);
      sql.append(" WHERE " + tableName + "." + idColumnName + "= '" + country.getId() + "'");

      ResultSet resultSet = Database.query(sql.toString());

      try
      {
        if (resultSet.next())
        {
          String bbox = resultSet.getString("bbox");
          if (bbox != null)
          {
            Pattern p = Pattern.compile("POLYGON\\(\\((.*)\\)\\)");
            Matcher m = p.matcher(bbox);

            if (m.matches())
            {
              String coordinates = m.group(1);
              List<Coordinate> coords = new LinkedList<Coordinate>();

              for (String c : coordinates.split(","))
              {
                String[] xAndY = c.split(" ");
                double x = Double.valueOf(xAndY[0]);
                double y = Double.valueOf(xAndY[1]);

                coords.add(new Coordinate(x, y));
              }

              Envelope e = new Envelope(coords.get(0), coords.get(2));

              try
              {
                bboxArr.put(e.getMinX());
                bboxArr.put(e.getMinY());
                bboxArr.put(e.getMaxX());
                bboxArr.put(e.getMaxY());
              }
              catch (JSONException ex)
              {
                throw new ProgrammingErrorException(ex);
              }
            }
            else
            {
              String label = country.getDisplayLabel().getValue();
              String error = "The geometry [" + label + "] could not be used to create a valid bounding box";

              throw new ProgrammingErrorException(error);
            }

            if (bboxArr.length() > 0)
            {
              return bboxArr;
            }
          }
        }
      }
      catch (SQLException sqlEx1)
      {
        Database.throwDatabaseException(sqlEx1);
      }
      finally
      {
        try
        {
          java.sql.Statement statement = resultSet.getStatement();
          resultSet.close();
          statement.close();
        }
        catch (SQLException sqlEx2)
        {
          Database.throwDatabaseException(sqlEx2);
        }
      }
    }

    // There are no layers in the map (that contain data) so return the Cambodian defaults
    if (bboxArr.length() == 0)
    {
      try
      {
        bboxArr.put(99.60205078124999);
        bboxArr.put(10.28249130152419);
        bboxArr.put(111.33544921874999);
        bboxArr.put(14.764259178591587);
      }
      catch (JSONException ex)
      {
        throw new ProgrammingErrorException(ex);
      }
    }

    return bboxArr;
  }

  @Transaction
  public void delete()
  {
    for (DashboardLayer layer : this.getAllHasLayer())
    {
      layer.delete();
    }

    super.delete();
  }

  @Override
  public String toString()
  {
    return String.format("[%s] = %s", this.getClassDisplayLabel(), this.getName());
  }

  @Override
  public Universal[] getUniversalAggregations(String mdAttributeId)
  {
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId);
    MdClassDAOIF mdClass = mdAttribute.definedByClass();

    QueryFactory factory = new QueryFactory();

    MetadataWrapperQuery query = new MetadataWrapperQuery(factory);

    DashboardQuery dQuery = new DashboardQuery(factory);
    dQuery.WHERE(dQuery.getId().EQ(this.getDashboardId()));

    query.WHERE(query.getWrappedMdClass().EQ(mdClass));
    query.AND(query.dashboard(dQuery));

    OIterator<? extends MetadataWrapper> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        MetadataWrapper wrapper = iterator.next();
        Universal lowest = wrapper.getUniversal();

        Universal root = Universal.getRoot();

        OIterator<Term> ancestors = lowest.getAllAncestors(AllowedIn.CLASS);

        try
        {
          List<Term> results = ancestors.getAll();
          List<Universal> universals = new LinkedList<Universal>();

          for (Term result : results)
          {
            if (!result.getId().equals(root.getId()))
            {
              universals.add((Universal) result);
            }
          }

          universals.add(lowest);

          return universals.toArray(new Universal[universals.size()]);
        }
        finally
        {
          ancestors.close();
        }

      }
      else
      {
        String message = "An unwrapped MdAttribute exists as part of a Dashboard.  This should never happen.";

        throw new ProgrammingErrorException(message);
      }
    }
    finally
    {
      iterator.close();
    }

  }

}
