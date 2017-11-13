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

@Controller(url = "account")
public class AccountController
{
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF page(ClientRequestIF request, @RequestParamter(name = "number") Integer number) throws JSONException
  {
    GeoprismUserQueryDTO users = GeoprismUserDTO.getAllInstances(request, GeoprismUserDTO.USERNAME, true, 10, number);

    return new RestBodyResponse(users);
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF edit(ClientRequestIF request, @RequestParamter(name = "id") String id) throws JSONException
  {
    GeoprismUserDTO user = GeoprismUserDTO.lock(request, id);
    RoleViewDTO[] roles = RoleViewDTO.getRoles(request, user);

    JSONArray groups = this.createRoleMap(roles);

    RestResponse response = new RestResponse();
    response.set("user", user);
    response.set("groups", groups);

    return response;
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF newInstance(ClientRequestIF request) throws JSONException
  {
    GeoprismUserDTO user = new GeoprismUserDTO(request);
    RoleViewDTO[] roles = RoleViewDTO.getRoles(request, user);

    JSONArray groups = this.createRoleMap(roles);

    RestResponse response = new RestResponse();
    response.set("user", user);
    response.set("groups", groups);

    return response;
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF remove(ClientRequestIF request, @RequestParamter(name = "id") String id) throws JSONException
  {
    GeoprismUserDTO user = GeoprismUserDTO.lock(request, id);
    user.delete();

    return new RestResponse();
  }

  private JSONArray createRoleMap(RoleViewDTO[] roles) throws JSONException
  {
    Map<String, JSONArray> map = new HashMap<String, JSONArray>();

    for (RoleViewDTO role : roles)
    {
      if (!map.containsKey(role.getGroupName()))
      {
        map.put(role.getGroupName(), new JSONArray());
      }

      JSONObject object = new JSONObject();
      object.put(RoleViewDTO.ASSIGNED, role.getAssigned());
      object.put(RoleViewDTO.DISPLAYLABEL, role.getDisplayLabel());
      object.put(RoleViewDTO.ROLEID, role.getRoleId());

      map.get(role.getGroupName()).put(object);
    }

    JSONArray groups = new JSONArray();

    Set<Entry<String, JSONArray>> entries = map.entrySet();

    for (Entry<String, JSONArray> entry : entries)
    {
      JSONObject group = new JSONObject();
      group.put("name", entry.getKey());
      group.put("roles", entry.getValue());

      groups.put(group);
    }
    return groups;
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF unlock(ClientRequestIF request, @RequestParamter(name = "id") String id) throws JSONException
  {
    GeoprismUserDTO.unlock(request, id);

    return new RestBodyResponse("");
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF apply(ClientRequestIF request, @RequestParamter(name = "account", parser = ParseType.BASIC_JSON) GeoprismUserDTO account, @RequestParamter(name = "roleIds") String roleIds) throws JSONException
  {
    JSONArray array = new JSONArray(roleIds);
    List<String> list = new LinkedList<String>();

    for (int i = 0; i < array.length(); i++)
    {
      list.add(array.getString(i));
    }

    account.applyWithRoles(list.toArray(new String[list.size()]));

    return new RestBodyResponse(account);
  }
}
