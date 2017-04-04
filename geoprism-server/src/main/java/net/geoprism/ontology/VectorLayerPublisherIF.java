package net.geoprism.ontology;

import com.vividsolutions.jts.geom.Envelope;
import com.wdtinc.mapbox_vector_tile.VectorTile;

public interface VectorLayerPublisherIF
{
  public String getLayerName();

  public VectorTile.Tile.Layer writeVectorLayer(Envelope envelope);
}
