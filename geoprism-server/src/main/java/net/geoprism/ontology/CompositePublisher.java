package net.geoprism.ontology;

import com.vividsolutions.jts.geom.Envelope;
import com.wdtinc.mapbox_vector_tile.VectorTile;
import com.wdtinc.mapbox_vector_tile.VectorTile.Tile;

public class CompositePublisher
{
  private VectorLayerPublisherIF[] publishers;

  public CompositePublisher(VectorLayerPublisherIF... publishers)
  {
    this.publishers = publishers;
  }

  public byte[] writeVectorTiles(Envelope envelope)
  {
    // Add built layer to MVT
    final VectorTile.Tile.Builder builder = VectorTile.Tile.newBuilder();

    for (VectorLayerPublisherIF publisher : this.publishers)
    {
      builder.addLayers(publisher.writeVectorLayer(envelope));
    }

    /// Build MVT
    Tile mvt = builder.build();

    return mvt.toByteArray();
  }

}
