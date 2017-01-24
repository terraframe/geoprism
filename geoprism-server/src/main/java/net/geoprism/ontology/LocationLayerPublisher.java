/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.ontology;

import java.io.IOException;
import java.io.Writer;
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

public class LocationLayerPublisher extends LayerPublisher
{
  private String id;

  private String universalId;

  public LocationLayerPublisher(String id, String universalId, String layers)
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
    layers.add(this.publishEntityLayer(entity, descendants));
    layers.add(this.publishChildLayer(entity, descendants));

    return layers;
  }

  private GeoserverLayerIF publishChildLayer(GeoEntity entity, List<Term> descendants)
  {
    LayerType layerType = this.getChildLayerType(descendants);
    String sql = this.getChildQuery(entity, layerType).getSQL();
    String viewName = SessionPredicate.generateId();

    DatabaseUtil.createView(viewName, sql);

    GeoserverLayer layer = new GeoserverLayer();
    layer.setLayerName(viewName);
    layer.setStyleName(this.getStyle(layerType));
    layer.setLayerType(layerType);

    return layer;
  }

  private GeoserverLayerIF publishEntityLayer(GeoEntity entity, List<Term> descendants)
  {
    LayerType layerType = this.getEntityLayerType(descendants);
    String sql = this.getEntityQuery(entity, layerType).getSQL();
    String viewName = SessionPredicate.generateId();

    DatabaseUtil.createView(viewName, sql);

    GeoserverLayer layer = new GeoserverLayer();
    layer.setLayerName(viewName);
    layer.setStyleName(this.getStyle(layerType));
    layer.setLayerType(layerType);

    return layer;
  }

  private ValueQuery getEntityQuery(GeoEntity entity, LayerType type)
  {
    QueryFactory factory = new QueryFactory();
    ValueQuery query = new ValueQuery(factory);

    GeoEntityQuery geQ1 = new GeoEntityQuery(query);

    // Id column
    SelectableChar id = geQ1.getId(GeoEntity.ID);
    id.setColumnAlias(GeoEntity.ID);

    // geoentity label
    SelectableSingle label = geQ1.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);

    // geo id (for uniqueness)
    Selectable geoId = geQ1.getGeoId(GeoEntity.GEOID);
    geoId.setColumnAlias(GeoEntity.GEOID);

    Selectable geom = ( type.equals(LayerType.POINT) ? geQ1.get(GeoEntity.GEOPOINT) : geQ1.get(GeoEntity.GEOMULTIPOLYGON) );
    geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);

    query.SELECT(id, label, geoId, geom);
    query.WHERE(geQ1.getId().EQ(entity.getId()));

    return query;
  }

  private ValueQuery getChildQuery(GeoEntity entity, LayerType type)
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

  private LayerType getChildLayerType(List<Term> descendants)
  {
    return descendants.size() <= 1 ? LayerType.POINT : LayerType.POLYGON;
  }

  private LayerType getEntityLayerType(List<Term> descendants)
  {
    return descendants.size() == 0 ? LayerType.POINT : LayerType.POLYGON;
  }

  public void writeGeojson(Writer writer)
  {
    GeoEntity entity = GeoEntity.get(this.id);
    Universal universal = entity.getUniversal();
    List<Term> descendants = universal.getAllDescendants(AllowedIn.CLASS).getAll();

    try
    {
      JSONWriter jw = new JSONWriter(writer);
      jw.array();

      LayerType entityLayerType = this.getEntityLayerType(descendants);
      ValueQuery entityQuery = this.getEntityQuery(entity, entityLayerType);

      this.writeGeojson(jw, entityQuery);

      LayerType childLayerType = this.getChildLayerType(descendants);
      ValueQuery childQuery = this.getChildQuery(entity, childLayerType);

      this.writeGeojson(jw, childQuery);

      jw.endArray();
    }
    catch (JSONException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private void writeGeojson(JSONWriter jw, ValueQuery query) throws IOException
  {
    jw.object();

    jw.key("type");
    jw.value("FeatureCollection");
    jw.key("features");
    jw.array();

    long count = this.writeFeatures(jw, query);

    jw.endArray();

    jw.key("totalFeatures");
    jw.value(count);

    this.writeCRS(jw);

    jw.endObject();
  }
}
