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

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

import net.geoprism.GeoprismUser;

public class GeoprismUserView extends GeoprismUserViewBase
{
  private static final long serialVersionUID = -1976777212;
  
  public GeoprismUserView()
  {
    super();
  }
  
  public static java.lang.String page(java.lang.Integer pageNumber)
  {
    GeoprismUserViewQuery query = new GeoprismUserViewQuery(new QueryFactory());
    
    query.restrictRows(10, pageNumber);
    query.ORDER_BY_ASC(query.getUsername());
    
    return query.toJSON().toString();
  }
  
  public JSONObject toJSON()
  {
    JSONObject json = new JSONObject();
    
    json.put("oid", this.getUserOid());
    json.put(GeoprismUserView.USERNAME, this.getUsername());
    json.put(GeoprismUserView.FIRSTNAME, this.getFirstName());
    json.put(GeoprismUserView.LASTNAME, this.getLastName());
    json.put(GeoprismUserView.EMAIL, this.getEmail());
    json.put(GeoprismUserView.PHONENUMBER, this.getPhoneNumber());
    json.put(GeoprismUserView.INACTIVE, this.getInactive());
    json.put(GeoprismUserView.NEWINSTANCE, false);
    json.put(GeoprismUserView.ROLES, getSerializedRoles());
    
    return json;
  }
  
  public JSONArray getSerializedRoles()
  {
    GeoprismUser user = this.getUser();
    
    JSONArray array = new JSONArray();

    if (user.isAppliedToDB())
    {
      Set<RoleDAOIF> roles = UserDAO.get(user.getOid()).authorizedRoles();

      for (RoleDAOIF role : roles)
      {
        array.put(role.getDisplayLabel(Session.getCurrentLocale()));
      }
    }

    return array;
  }
}
