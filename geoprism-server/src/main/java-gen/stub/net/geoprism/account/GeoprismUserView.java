package net.geoprism.account;

import java.util.Set;

import net.geoprism.GeoprismUser;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

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
