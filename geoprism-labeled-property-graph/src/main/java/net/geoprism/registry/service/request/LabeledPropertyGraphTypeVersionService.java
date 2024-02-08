package net.geoprism.registry.service.request;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.LPGTileCache;
import net.geoprism.registry.service.business.LabeledPropertyGraphTypeVersionBusinessServiceIF;

@Service
public class LabeledPropertyGraphTypeVersionService implements LabeledPropertyGraphTypeVersionServiceIF
{
  @Autowired
  private LabeledPropertyGraphTypeVersionBusinessServiceIF service;

  @Override
  @Request(RequestType.SESSION)
  public void createTiles(String sessionId, String oid)
  {
    LabeledPropertyGraphTypeVersion version = this.service.get(oid);

    this.service.createTiles(version);
  }

  @Override
  @Request(RequestType.SESSION)
  public InputStream getTile(String sessionId, JSONObject object)
  {
    try
    {
      byte[] bytes = LPGTileCache.getTile(object);

      if (bytes != null)
      {
        return new ByteArrayInputStream(bytes);
      }

      return new ByteArrayInputStream(new byte[] {});
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

}
