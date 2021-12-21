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
package net.geoprism;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestBodyResponse;

@Controller(url = "menu")
public class MenuController
{
  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF applications(ClientRequestIF request) throws JSONException
  {
    Set<String> roleNames = this.getAssignedRoleNames(request);

    List<GeoprismApplication> allApplications = ClientConfigurationService.getApplications(request);
    // List<GeoprismApplication> authorizedApplications = allApplications.stream().filter(p ->
    // p.isValid(roleNames)).collect(Collectors.toList());

    JSONArray response = new JSONArray();

    for (GeoprismApplication application : allApplications)
    {
      if (application.isValid(roleNames))
      {
        response.put(application.toJSON());
      }
    }

    return new RestBodyResponse(response);
  }

  private Set<String> getAssignedRoleNames(ClientRequestIF request)
  {
    try
    {
      Set<String> roleNames = new TreeSet<String>();
  
//      SingleActorDTO currentUser = GeoprismUserDTO.getCurrentUser(request);
  //    List<? extends RolesDTO> userRoles = currentUser.getAllAssignedRole();
      
      JSONArray jaUserRoles = new JSONArray(RoleViewDTO.getCurrentRoles(request));
      for (int i = 0; i < jaUserRoles.length(); ++i)
      {
        String roleName = jaUserRoles.getString(i);
        
        roleNames.add(roleName);
      }
  
      return roleNames;
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }
  }

}
