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
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.mapping.GeoserverFacade;

import net.geoprism.data.DatabaseUtil;
import net.geoprism.gis.geoserver.GeoserverLayer;
import net.geoprism.gis.geoserver.GeoserverLayerIF;
import net.geoprism.gis.geoserver.SessionPredicate;

public class LocationContextPublisher extends LayerPublisher
{
  private String id;

  public LocationContextPublisher(String id, String layers)
  {
    super(layers);

    this.id = id;
  }

  @Override
  protected List<GeoserverLayerIF> buildLayers()
  {

    GeoEntity entity = GeoEntity.get(this.id);
    Universal universal = entity.getUniversal();
    List<Term> descendants = universal.getAllDescendants(AllowedIn.CLASS).getAll();

    List<GeoserverLayerIF> layers = new LinkedList<GeoserverLayerIF>();
    layers.add(this.publishEntityLayer(entity, descendants));

    return layers;
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

  private LayerType getEntityLayerType(List<Term> descendants)
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
      LayerType entityLayerType = this.getEntityLayerType(descendants);
      ValueQuery entityQuery = this.getEntityQuery(entity, entityLayerType);

      this.writeGeojson(writer, entityQuery);
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
      LayerType entityLayerType = this.getEntityLayerType(descendants);
      ValueQuery entityQuery = this.getEntityQuery(entity, entityLayerType);

      return this.writeVectorTiles(layerName, x, y, zoom, entityQuery);
    }
    catch (JSONException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

}
