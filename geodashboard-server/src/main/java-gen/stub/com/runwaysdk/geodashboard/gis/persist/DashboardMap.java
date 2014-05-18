package com.runwaysdk.geodashboard.gis.persist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.Map;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.query.OIterator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class DashboardMap extends DashboardMapBase implements
    com.runwaysdk.generation.loader.Reloadable, Map
{
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
  public List<? extends Layer> getLayers()
  {
    return this.getAllHasLayer().getAll();
  }

  public String getMapJSON()
  {
    try
    {
      JSONObject mapJSON = new JSONObject();
      mapJSON.put("mapName", this.getName());

      JSONArray layers = new JSONArray();
      mapJSON.put("layers", layers);

      List<DashboardLayer> layerList = new LinkedList<DashboardLayer>();
      OIterator<? extends DashboardLayer> iter = this.getAllHasLayer();
      try
      {
        while (iter.hasNext())
        {
          DashboardLayer layer = iter.next();
          layerList.add(layer);

          JSONObject layerObj = new JSONObject();
          layerObj.put("viewName", layer.getViewName());
          layerObj.put("sldName", layer.getSLDName());
          layerObj.put("layerName", layer.getName());
          layers.put(layerObj);
        }
        return mapJSON.toString();
      }
      finally
      {
        iter.close();
      }
      
      // calculate the BBOX of all layers involved
      
    }
    catch (JSONException ex)
    {
      throw new ProgrammingErrorException(ex);
    }
  }

  @Transaction
  public void delete()
  {
    for(DashboardLayer layer : this.getAllHasLayer())
    {
      layer.delete();
    }
    
    super.delete();
  }
}
