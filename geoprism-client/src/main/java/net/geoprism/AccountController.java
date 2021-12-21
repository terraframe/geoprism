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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.ParseType;
import com.runwaysdk.mvc.RequestParamter;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestBodyResponse;
import com.runwaysdk.mvc.RestResponse;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.system.SingleActorDTO;

import net.geoprism.account.ExternalProfileDTO;
import net.geoprism.account.GeoprismUserViewDTO;
import net.geoprism.account.UserInviteDTO;

@Controller(url = "account")
public class AccountController
{
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF page(ClientRequestIF request, @RequestParamter(name = "number") Integer number) throws JSONException
  {
//    GeoprismUserQueryDTO users = GeoprismUserDTO.getAllInstances(request, GeoprismUserDTO.USERNAME, true, 10, number);
//
//    return new RestBodyResponse(users);
    
    String json = GeoprismUserViewDTO.page(request, number);
    
    return new RestBodyResponse(json);
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF edit(ClientRequestIF request, @RequestParamter(name = "oid") String oid) throws JSONException
  {
    GeoprismUserDTO user = GeoprismUserDTO.lock(request, oid);
    RoleViewDTO[] roles = RoleViewDTO.getRoles(request, user);

    JSONArray groups = this.createRoleMap(roles);

    RestResponse response = new RestResponse();
    response.set("user", user);
    response.set("groups", groups);

    return response;
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF get(ClientRequestIF request) throws JSONException
  {
    SingleActorDTO user = GeoprismUserDTO.getCurrentUser(request);
    
    if (!(user instanceof ExternalProfileDTO))
    {
      user.lock();
      
      return new RestBodyResponse(user);
    }
    else
    {
      ExternalProfileDTO ep = (ExternalProfileDTO) user;
      
      JSONObject jo = new JSONObject();
      
      jo.put("username", ep.getDisplayName());
      
      jo.put("email", ep.getEmail());
      
      jo.put("firstName", ep.getFirstName());
      
      jo.put("lastName", ep.getLastName());
      
      jo.put("phoneNumber", ep.getPhoneNumber());
      
      return new RestBodyResponse(jo.toString());
    }
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF newInstance(ClientRequestIF request) throws JSONException
  {
    GeoprismUserDTO user = UserInviteDTO.newUserInst(request);
    RoleViewDTO[] roles = RoleViewDTO.getRoles(request, user);

    JSONArray groups = this.createRoleMap(roles);

    RestResponse response = new RestResponse();
    response.set("user", user);
    response.set("groups", groups);

    return response;
  }
  
  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF newUserInstance(ClientRequestIF request) throws JSONException
  {
    GeoprismUserDTO user = UserInviteDTO.newUserInst(request);
    
    return new RestBodyResponse(user);
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF remove(ClientRequestIF request, @RequestParamter(name = "oid") String oid) throws JSONException
  {
    GeoprismUserDTO user = GeoprismUserDTO.lock(request, oid);
    user.delete();

    return new RestResponse();
  }

  private JSONArray createRoleMap(RoleViewDTO[] roles) throws JSONException
  {
    Map<String, JSONArray> map = new HashMap<String, JSONArray>();
    Map<String, String> selected = new HashMap<String, String>();

    for (RoleViewDTO role : roles)
    {
      if (!map.containsKey(role.getGroupName()))
      {
        map.put(role.getGroupName(), new JSONArray());
      }

      JSONObject object = new JSONObject();
      object.put(RoleViewDTO.DISPLAYLABEL, role.getDisplayLabel());
      object.put(RoleViewDTO.ROLEID, role.getRoleId());

      map.get(role.getGroupName()).put(object);

      if (role.getAssigned())
      {
        selected.put(role.getGroupName(), role.getRoleId());
      }
    }

    JSONArray groups = new JSONArray();

    Set<Entry<String, JSONArray>> entries = map.entrySet();

    for (Entry<String, JSONArray> entry : entries)
    {
      JSONObject group = new JSONObject();
      group.put("name", entry.getKey());
      group.put("roles", entry.getValue());

      if (selected.containsKey(entry.getKey()))
      {
        group.put(RoleViewDTO.ASSIGNED, selected.get(entry.getKey()));
      }

      groups.put(group);
    }
    return groups;
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF unlock(ClientRequestIF request, @RequestParamter(name = "oid") String oid) throws JSONException
  {
    GeoprismUserDTO.unlock(request, oid);

    return new RestBodyResponse("");
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF apply(ClientRequestIF request, @RequestParamter(name = "account", parser = ParseType.BASIC_JSON) GeoprismUserDTO account, @RequestParamter(name = "roleIds") String roleIds) throws JSONException
  {
    if (roleIds != null)
    {
      JSONArray array = new JSONArray(roleIds);
      List<String> list = new LinkedList<String>();

      for (int i = 0; i < array.length(); i++)
      {
        list.add(array.getString(i));
      }

      account.applyWithRoles(list.toArray(new String[list.size()]));
    }
    else
    {
      account.apply();
    }

    return new RestBodyResponse(account);
  }
  
  public static String getBaseUrl(ServletRequestIF request)
  {
    String scheme = request.getScheme() + "://";
    String serverName = request.getServerName();
    String serverPort = ( request.getServerPort() == 80 ) ? "" : ":" + request.getServerPort();
    String contextPath = request.getContextPath();
    return scheme + serverName + serverPort + contextPath;
  }
  
  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF inviteUser(ClientRequestIF request, ServletRequestIF sr, @RequestParamter(name = "invite") String sInvite, @RequestParamter(name = "roleIds") String roleIds) throws JSONException
  {
    UserInviteDTO.initiate(request, sInvite, roleIds, getBaseUrl(sr));

    return new RestResponse();
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF newInvite(ClientRequestIF request) throws JSONException
  {
    RoleViewDTO[] roles = RoleViewDTO.getRoles(request, null);
    JSONObject user = new JSONObject();
    user.put("newInstance", true);

    JSONArray groups = this.createRoleMap(roles);

    RestResponse response = new RestResponse();
    response.set("user", user);
    response.set("groups", groups);

    return response;
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF inviteComplete(ClientRequestIF request, @RequestParamter(name = "user", parser = ParseType.BASIC_JSON) GeoprismUserDTO user, @RequestParamter(name = "token") String token) throws JSONException
  {
    UserInviteDTO.complete(request, token, user);

    return new RestResponse();
  }
}
