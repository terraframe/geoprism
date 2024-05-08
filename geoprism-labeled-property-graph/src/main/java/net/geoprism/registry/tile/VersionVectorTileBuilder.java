/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.tile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.json.JSONException;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.orientechnologies.common.io.OIOException;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.mapping.GeoserverFacade;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdVertex;
import com.wdtinc.mapbox_vector_tile.VectorTile;
import com.wdtinc.mapbox_vector_tile.VectorTile.Tile;
import com.wdtinc.mapbox_vector_tile.VectorTile.Tile.Layer;
import com.wdtinc.mapbox_vector_tile.adapt.jts.IGeometryFilter;
import com.wdtinc.mapbox_vector_tile.adapt.jts.IUserDataConverter;
import com.wdtinc.mapbox_vector_tile.adapt.jts.JtsAdapter;
import com.wdtinc.mapbox_vector_tile.adapt.jts.TileGeomResult;
import com.wdtinc.mapbox_vector_tile.build.MvtLayerBuild;
import com.wdtinc.mapbox_vector_tile.build.MvtLayerParams;
import com.wdtinc.mapbox_vector_tile.build.MvtLayerProps;

import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.service.business.GeoObjectTypeSnapshotBusinessServiceIF;
import net.geoprism.registry.service.business.LabeledPropertyGraphTypeVersionBusinessServiceIF;
import net.geoprism.spring.ApplicationContextHolder;

public class VersionVectorTileBuilder implements VectorLayerPublisherIF
{
  private LabeledPropertyGraphTypeVersion version;

  private String                          typeCode;

  public VersionVectorTileBuilder(LabeledPropertyGraphTypeVersion version, String typeCode)
  {
    this.version = version;
    this.typeCode = typeCode;
  }

  private List<Map<String, Object>> getResultSet(Envelope envelope)
  {
    GeoObjectTypeSnapshotBusinessServiceIF gTypeService = ApplicationContextHolder.getBean(GeoObjectTypeSnapshotBusinessServiceIF.class);
    LabeledPropertyGraphTypeVersionBusinessServiceIF versionService = ApplicationContextHolder.getBean(LabeledPropertyGraphTypeVersionBusinessServiceIF.class);

    HierarchyTypeSnapshot hierarchy = versionService.getHierarchies(version).get(0);
    GeoObjectTypeSnapshot snapshot = gTypeService.get(version, this.typeCode);

    if (snapshot != null)
    {
      MdVertex mdVertex = snapshot.getGraphMdVertex();
      MdEdge mdEdge = hierarchy.getGraphMdEdge();

      String tableName = mdVertex.getDbClassName();
      String edgeName = mdEdge.getDbClassName();

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT");
      statement.append("  oid AS oid,");
      statement.append("  code AS code,");
      statement.append("  displayLabel AS label,");
      statement.append("  uuid AS uuid,");
      statement.append("  first(in('" + edgeName + "').uuid) AS parent,");
      statement.append("  geometry AS geometry");
      statement.append(" FROM " + tableName);

      // There is a bug in orientdb where the ST_GeomFromText breaks if the
      // bounds are at +-180, as such when that is the case don't restrict the
      // results. The tile creation will filter out of tile results.
      if (envelope.getMaxX() != 180D && envelope.getMinX() != -180D)
      {
        statement.append(" WHERE ST_Intersects(geometry, ST_GeomFromText(:bounds)) == true");
      }

      GraphQuery<Map<String, Object>> query = new GraphQuery<Map<String, Object>>(statement.toString());

      if (envelope.getMaxX() != 180D && envelope.getMinX() != -180D)
      {
        Polygon bounds = JTS.toGeometry(envelope);
        String text = bounds.toText();

        query.setParameter("bounds", text);
      }

      return query.getResults();
    }

    return new LinkedList<>();
  }

  @Override
  public List<Layer> writeVectorLayers(Envelope envelope, Envelope bounds)
  {
    List<Map<String, Object>> resultSet = this.getResultSet(envelope);

    try
    {
      List<Layer> layers = new LinkedList<Layer>();
      layers.add(this.writeVectorLayer("context", bounds, resultSet));

      return layers;
    }
    catch (JSONException | IOException | MismatchedDimensionException | TransformException | FactoryException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public byte[] writeVectorTiles(Envelope envelope, Envelope bounds)
  {
    // Add built layer to MVT
    final VectorTile.Tile.Builder builder = VectorTile.Tile.newBuilder();

    List<Layer> layers = this.writeVectorLayers(envelope, bounds);

    for (Layer layer : layers)
    {
      builder.addLayers(layer);
    }

    /// Build MVT
    Tile mvt = builder.build();

    return mvt.toByteArray();
  }

  @SuppressWarnings("unchecked")
  public VectorTile.Tile.Layer writeVectorLayer(String layerName, Envelope bounds, List<Map<String, Object>> resultSet) throws IOException, MismatchedDimensionException, TransformException, NoSuchAuthorityCodeException, FactoryException
  {
    CoordinateReferenceSystem sourceCRS = DefaultGeographicCRS.WGS84;
    CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3857");

    MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);

    List<Geometry> geometries = new LinkedList<Geometry>();

    for (Map<String, Object> result : resultSet)
    {
      Map<String, String> data = new TreeMap<String, String>();
      data.put(GeoEntity.OID, (String) result.get("oid"));
      data.put(DefaultAttribute.CODE.getName(), (String) result.get("code"));
      data.put(DefaultAttribute.UID.getName(), (String) result.get("uuid"));
      data.put(GeoEntity.DISPLAYLABEL, ( (Map<String, String>) result.get("label") ).get("defaultLocale"));
      data.put("parent", (String) result.get("parent"));

      Geometry geom = (Geometry) result.get("geometry");

      if (geom != null)
      {
        // Project the geometry from WGS84 to 3857
        Geometry geometry = JTS.transform(geom, transform);
        geometry.setUserData(data);

        geometries.add(geometry);
      }
    }

    GeometryFactory geomFactory = new GeometryFactory();
    IGeometryFilter acceptAllGeomFilter = geometry -> true;

    MvtLayerParams layerParams = new MvtLayerParams();

    final Envelope bufferedEnvelope = this.getBufferedEnvelope(bounds, layerParams);

    final TileGeomResult tileGeom = JtsAdapter.createTileGeom(geometries, bounds, bufferedEnvelope, geomFactory, layerParams, acceptAllGeomFilter);

    // Create MVT layer
    final MvtLayerProps layerProps = new MvtLayerProps();
    final IUserDataConverter ignoreUserData = new UserDataConverter();

    // MVT tile geometry to MVT features
    final List<VectorTile.Tile.Feature> features = JtsAdapter.toFeatures(tileGeom.mvtGeoms, layerProps, ignoreUserData);

    final VectorTile.Tile.Layer.Builder layerBuilder = MvtLayerBuild.newLayerBuilder(layerName, layerParams);
    layerBuilder.addAllFeatures(features);

    MvtLayerBuild.writeProps(layerBuilder, layerProps);

    // Build MVT layer
    return layerBuilder.build();
  }

  protected Map<String, String> getUserData(ValueObject object)
  {
    AttributeIF[] attributes = object.getAttributeArrayIF();

    Map<String, String> data = new TreeMap<String, String>();

    for (AttributeIF attribute : attributes)
    {
      String name = attribute.getName();

      if (!name.equals(GeoserverFacade.GEOM_COLUMN))
      {
        data.put(name, attribute.getValue());
      }
    }

    return data;
  }

  public Envelope getBufferedEnvelope(Envelope envelope, MvtLayerParams layerParams)
  {
    final int bufferPixel = 64;

    final AffineTransformation t = new AffineTransformation();
    final double xDiff = envelope.getWidth();
    final double yDiff = envelope.getHeight();

    final double xOffset = -envelope.getMinX();
    final double yOffset = -envelope.getMinY();

    // Transform Setup: Shift to 0 as minimum value
    t.translate(xOffset, yOffset);

    // Transform Setup: Scale X and Y to tile extent values, flip Y values
    double xScale = 1d / ( xDiff / (double) layerParams.extent );
    double yScale = -1d / ( yDiff / (double) layerParams.extent );

    t.scale(xScale, yScale);

    // Transform Setup: Bump Y values to positive quadrant
    t.translate(0d, (double) layerParams.extent);

    // Calculate the buffered geometry envelop for determining geometry
    // intersection
    double deltaX = bufferPixel * Math.abs(1 / xScale);
    double deltaY = bufferPixel * Math.abs(1 / yScale);

    final Envelope bufferedEnvelope = new Envelope(envelope);
    bufferedEnvelope.expandBy(deltaX, deltaY);

    return bufferedEnvelope;
  }

}
