package net.geoprism.account;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class OauthServer extends OauthServerBase implements Reloadable
{
  private static final long   serialVersionUID = 654881024;

  private static final String DHIS2            = "DHIS2";

  public OauthServer()
  {
    super();
  }

  public static String getRemoteId(String serverType, JSONObject object)
  {
    try
    {
      if (serverType.equals(DHIS2))
      {
        return object.getString("id");
      }
      else
      {
        throw new RuntimeException("Unknown server type [" + serverType + "]");
      }
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }

  }

  public static void populate(String serverType, ExternalProfile profile, JSONObject object)
  {
    try
    {
      if (serverType.equals(DHIS2))
      {
        profile.setDisplayName(object.getString("displayName"));
      }
      else
      {
        throw new RuntimeException("Unknown server type [" + serverType + "]");
      }
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Authenticate
  public static OauthServer[] getAll()
  {
    OauthServerQuery query = new OauthServerQuery(new QueryFactory());
    query.ORDER_BY_ASC(query.getDisplayLabel().localize());

    OIterator<? extends OauthServer> iterator = query.getIterator();

    try
    {
      List<? extends OauthServer> results = iterator.getAll();

      return results.toArray(new OauthServer[results.size()]);
    }
    finally
    {
      iterator.close();
    }
  }

}
