package net.geoprism.ontology;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.wdtinc.mapbox_vector_tile.VectorTile.Tile.Feature.Builder;
import com.wdtinc.mapbox_vector_tile.adapt.jts.IUserDataConverter;
import com.wdtinc.mapbox_vector_tile.build.MvtLayerProps;

public class UserDataConverter implements IUserDataConverter
{
  @Override
  @SuppressWarnings("unchecked")
  public void addTags(Object userData, MvtLayerProps layerProps, Builder featureBuilder)
  {
    if (userData != null)
    {
      Map<String, String> data = (Map<String, String>) userData;
      Set<Entry<String, String>> entries = data.entrySet();

      for (Entry<String, String> entry : entries)
      {
        int kIndex = layerProps.addKey(entry.getKey());
        int vIndex = layerProps.addValue(entry.getValue());

        featureBuilder.addTags(kIndex);
        featureBuilder.addTags(vIndex);
      }
    }
  }
}
