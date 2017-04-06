package net.geoprism.ontology;

import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.wdtinc.mapbox_vector_tile.VectorTile;

public interface VectorLayerPublisherIF
{
  public List<VectorTile.Tile.Layer> writeVectorLayers(Envelope envelope);
}
