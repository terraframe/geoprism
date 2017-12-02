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
import com.runwaysdk.system.RolesDTO;
import com.runwaysdk.system.SingleActorDTO;

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
    Set<String> roleNames = new TreeSet<String>();

    SingleActorDTO currentUser = GeoprismUserDTO.getCurrentUser(request);

    List<? extends RolesDTO> userRoles = currentUser.getAllAssignedRole();
    for (RolesDTO role : userRoles)
    {
      roleNames.add(role.getRoleName());
    }

    return roleNames;
  }

}
