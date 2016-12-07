package net.geoprism.account;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.AuthenticationRequestBuilder;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.generation.loader.Reloadable;

public class OauthServerDTO extends OauthServerDTOBase implements Reloadable, OauthServerIF
{
  private static final long serialVersionUID = -431820160;

  public OauthServerDTO(ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }

  /**
   * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
   * 
   * @param businessDTO
   *          The BusinessDTO to duplicate
   * @param clientRequest
   *          The clientRequest this DTO should use to communicate with the server.
   */
  protected OauthServerDTO(BusinessDTO businessDTO, ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  public String getUrl()
  {
    String redirect = DeployProperties.getApplicationURL() + "/session/ologin";

    try
    {
      JSONObject state = new JSONObject();
      state.put(OauthServerIF.SERVER_ID, this.getId());
      
      AuthenticationRequestBuilder builder = OAuthClientRequest.authorizationLocation(this.getAuthorizationLocation());
      builder.setClientId(this.getClientId());
      builder.setRedirectURI(redirect);
      builder.setResponseType("code");
      builder.setState(state.toString());
      
      OAuthClientRequest request = builder.buildQueryMessage();

      return request.getLocationUri();
    }
    catch (OAuthSystemException | JSONException e)
    {
    }

    return null;
  }
}
