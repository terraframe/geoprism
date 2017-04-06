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
