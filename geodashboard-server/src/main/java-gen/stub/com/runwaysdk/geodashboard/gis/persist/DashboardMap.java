package com.runwaysdk.geodashboard.gis.persist;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
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
import com.runwaysdk.system.gis.geo.Universal;

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
      layer.setConditions(Arrays.asList(conditions));

      generateSessionViewName(layer);

      layer.publish(true);
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
      this.generateSessionViewName(layer);

      layer.publish(true);
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
      mapJSON.put("mapName", this.getName());

      if (config == null || !config.equals("republish=false"))
      {
        publishAllLayers(orderedLayers);
      }

      JSONArray layers = new JSONArray();
      for (int i = 0; i < orderedLayers.length; i++)
      {
        layers.put(orderedLayers[i].toJSON());
      }
      mapJSON.put("layers", layers);

      JSONArray mapBBox = getMapLayersBBox(orderedLayers);
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

  @Override
  public String getName()
  {
    return super.getName();
  }

  public JSONArray getMapLayersBBox(DashboardLayer[] layers)
  {
    JSONArray bboxArr = new JSONArray();

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

    dQuery.WHERE(dQuery.getMap().EQ(this));

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
