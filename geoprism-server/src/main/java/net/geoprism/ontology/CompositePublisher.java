package net.geoprism.ontology;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.wdtinc.mapbox_vector_tile.VectorTile;
import com.wdtinc.mapbox_vector_tile.VectorTile.Tile;
import com.wdtinc.mapbox_vector_tile.VectorTile.Tile.Layer;

public class CompositePublisher
{
  private List<VectorLayerPublisherIF> publishers;

  public CompositePublisher(VectorLayerPublisherIF... publishers)
  {
    this.publishers = new LinkedList<VectorLayerPublisherIF>(Arrays.asList(publishers));
  }

  public void add(VectorLayerPublisherIF publisher)
  {
    this.publishers.add(publisher);
  }

  public byte[] writeVectorTiles(Envelope envelope)
  {
    // Add built layer to MVT
    final VectorTile.Tile.Builder builder = VectorTile.Tile.newBuilder();

    for (VectorLayerPublisherIF publisher : this.publishers)
    {
      List<Layer> layers = publisher.writeVectorLayers(envelope);

      for (Layer layer : layers)
      {
        builder.addLayers(layer);
      }
    }

    /// Build MVT
    Tile mvt = builder.build();

    return mvt.toByteArray();
  }
}
