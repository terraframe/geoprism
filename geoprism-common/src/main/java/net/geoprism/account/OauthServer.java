package net.geoprism.account;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

public class OauthServer implements OauthServerIF
{
  private String serverId;

  private String profileLocation;

  private String idProperty;

  private String authorizationLocation;

  private String tokenLocation;
  
  private String clientId;

  private String clientSecret;

  /*
   * (non-Javadoc)
   * 
   * @see net.geoprism.account.OauthServerIF#getAuthorizationLocation()
   */
  @Override
  public String getAuthorizationLocation()
  {
    return authorizationLocation;
  }

  public void setAuthorizationLocation(String authorizationLocation)
  {
    this.authorizationLocation = authorizationLocation;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.geoprism.account.OauthServerIF#getTokenLocation()
   */
  @Override
  public String getTokenLocation()
  {
    return tokenLocation;
  }

  public void setTokenLocation(String tokenLocation)
  {
    this.tokenLocation = tokenLocation;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.geoprism.account.OauthServerIF#getClientId()
   */
  @Override
  public String getClientId()
  {
    return clientId;
  }

  public void setClientId(String clientId)
  {
    this.clientId = clientId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.geoprism.account.OauthServerIF#getClientSecret()
   */
  @Override
  public String getClientSecret()
  {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret)
  {
    this.clientSecret = clientSecret;
  }

  public String getServerId()
  {
    return serverId;
  }

  public void setServerId(String serverId)
  {
    this.serverId = serverId;
  }

  public String getProfileLocation()
  {
    return profileLocation;
  }

  public void setProfileLocation(String profileLocation)
  {
    this.profileLocation = profileLocation;
  }

  public String getIdProperty()
  {
    return idProperty;
  }

  public void setIdProperty(String idProperty)
  {
    this.idProperty = idProperty;
  }
  
  public String getUrl()
  {
    String redirect = "https://localhost:8443/geoprism/session/ologin";
    
    OAuthClientRequest request;
    try
    {
      request = OAuthClientRequest
          .authorizationLocation(this.authorizationLocation)
          .setClientId(this.clientId)
          .setRedirectURI(redirect)
          .setResponseType("code")
          .buildQueryMessage();
      
      return request.getLocationUri();
    }
    catch (OAuthSystemException e)
    {
    }    
    
    return null;
  }
}
