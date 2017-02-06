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
package net.geoprism.ontology;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONWriter;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableChar;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedInQuery;
import com.runwaysdk.system.gis.geo.Universal;

import net.geoprism.data.DatabaseUtil;
import net.geoprism.gis.geoserver.GeoserverFacade;
import net.geoprism.gis.geoserver.GeoserverLayer;
import net.geoprism.gis.geoserver.GeoserverLayerIF;
import net.geoprism.gis.geoserver.SessionPredicate;

public class LocationTargetPublisher extends LayerPublisher
{
  private String id;

  private String universalId;

  public LocationTargetPublisher(String id, String universalId, String layers)
  {
    super(layers);

    this.id = id;
    this.universalId = universalId;
  }

  @Override
  protected List<GeoserverLayerIF> buildLayers()
  {

    GeoEntity entity = GeoEntity.get(this.id);
    Universal universal = entity.getUniversal();
    List<Term> descendants = universal.getAllDescendants(AllowedIn.CLASS).getAll();

    List<GeoserverLayerIF> layers = new LinkedList<GeoserverLayerIF>();
    layers.add(this.publishLayer(entity, descendants));

    return layers;
  }

  private GeoserverLayerIF publishLayer(GeoEntity entity, List<Term> descendants)
  {
    LayerType layerType = this.getLayerType(descendants);
    String sql = this.getQuery(entity, layerType).getSQL();
    String viewName = SessionPredicate.generateId();

    DatabaseUtil.createView(viewName, sql);

    GeoserverLayer layer = new GeoserverLayer();
    layer.setLayerName(viewName);
    layer.setStyleName(this.getStyle(layerType));
    layer.setLayerType(layerType);

    return layer;
  }

  private ValueQuery getQuery(GeoEntity entity, LayerType type)
  {
    ValueQuery vQuery = new ValueQuery(new QueryFactory());
    LocatedInQuery liQuery = new LocatedInQuery(vQuery);
    GeoEntityQuery query = new GeoEntityQuery(vQuery);

    // Id column
    SelectableChar id = query.getId(GeoEntity.ID);
    id.setColumnAlias(GeoEntity.ID);

    SelectableSingle label = query.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);

    Selectable geoId = query.getGeoId(GeoEntity.GEOID);
    geoId.setColumnAlias(GeoEntity.GEOID);

    Selectable geom = ( type.equals(LayerType.POINT) ? query.get(GeoEntity.GEOPOINT) : query.get(GeoEntity.GEOMULTIPOLYGON) );
    geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);

    vQuery.SELECT(id, label, geoId, geom);
    vQuery.WHERE(liQuery.parentId().EQ(entity.getId()));
    vQuery.AND(query.locatedIn(liQuery));

    if (this.universalId != null && this.universalId.length() > 0)
    {
      vQuery.AND(query.getUniversal().EQ(this.universalId));
    }

    return vQuery;
  }

  private LayerType getLayerType(List<Term> descendants)
  {
    return LayerType.POLYGON;
  }

  public void writeGeojson(JSONWriter writer)
  {
    GeoEntity entity = GeoEntity.get(this.id);
    Universal universal = entity.getUniversal();
    List<Term> descendants = universal.getAllDescendants(AllowedIn.CLASS).getAll();

    try
    {
      LayerType childLayerType = this.getLayerType(descendants);
      ValueQuery childQuery = this.getQuery(entity, childLayerType);

      this.writeGeojson(writer, childQuery);
    }
    catch (JSONException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private void writeGeojson(JSONWriter writer, ValueQuery query) throws IOException
  {
    writer.object();

    writer.key("type");
    writer.value("FeatureCollection");
    writer.key("features");
    writer.array();

    long count = this.writeFeatures(writer, query);

    writer.endArray();

    writer.key("totalFeatures");
    writer.value(count);

    this.writeCRS(writer);

    writer.endObject();
  }

  public byte[] writeVectorTiles(String layerName, int x, int y, int zoom)
  {
    GeoEntity entity = GeoEntity.get(this.id);
    Universal universal = entity.getUniversal();
    List<Term> descendants = universal.getAllDescendants(AllowedIn.CLASS).getAll();

    try
    {
      LayerType entityLayerType = this.getLayerType(descendants);
      ValueQuery entityQuery = this.getQuery(entity, entityLayerType);

      return this.writeVectorTiles(layerName, x, y, zoom, entityQuery);
    }
    catch (JSONException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
}
