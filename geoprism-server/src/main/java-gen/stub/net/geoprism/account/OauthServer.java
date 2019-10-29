/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.account;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class OauthServer extends OauthServerBase 
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
        return object.getString("oid");
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
