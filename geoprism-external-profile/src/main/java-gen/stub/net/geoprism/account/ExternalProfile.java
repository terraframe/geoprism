/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.AssignmentsQuery;
import com.runwaysdk.system.Roles;

import net.geoprism.rbac.RoleConstants;

public class ExternalProfile extends ExternalProfileBase implements GeoprismActorIF
{
  private static final long serialVersionUID = -377482924;

  public ExternalProfile()
  {
    super();
  }

  @Override
  public Boolean isAssigned(Roles role)
  {
    if (role != null)
    {
      QueryFactory factory = new QueryFactory();

      AssignmentsQuery query = new AssignmentsQuery(factory);
      query.WHERE(query.getParent().EQ(this));
      query.AND(query.getChild().EQ(role));

      return ( query.getCount() > 0 );
    }

    return false;
  }

  @Transaction
  private static synchronized SingleActorDAOIF getOrCreate(OauthServer server, JSONObject object) throws JSONException
  {
    String serverType = server.getServerType();

    String remoteId = OauthServer.getRemoteId(serverType, object);

    ExternalProfileQuery query = new ExternalProfileQuery(new QueryFactory());
    query.WHERE(query.getRemoteId().EQ(remoteId));
    OIterator<? extends ExternalProfile> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        ExternalProfile profile = it.next();
        profile.lock();
        OauthServer.populate(serverType, profile, object);
        profile.apply();

        return (SingleActorDAOIF) BusinessFacade.getEntityDAO(profile);
      }
      else
      {
        ExternalProfile profile = new ExternalProfile();
        profile.setRemoteId(remoteId);
        profile.setServer(server);
        OauthServer.populate(serverType, profile, object);
        profile.apply();

        SingleActorDAOIF actor = (SingleActorDAOIF) BusinessFacade.getEntityDAO(profile);

        RoleDAO role = RoleDAO.findRole(RoleConstants.ADMIN).getBusinessDAO();
        role.assignMember(actor);

        return actor;
      }
    }
    finally
    {
      it.close();
    }
  }

}
