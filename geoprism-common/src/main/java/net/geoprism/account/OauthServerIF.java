package net.geoprism.account;

public interface OauthServerIF
{
  /**
   * @return Id of the Oauth server (e.g. The dhis2 instance)
   */
  public String getServerId();

  /**
   * @return Remote url for authorization requests
   */
  public String getAuthorizationLocation();

  /**
   * @return Remote url for token requests
   */
  public String getTokenLocation();

  /**
   * @return Remote url for profile requests
   */
  public String getProfileLocation();

  /**
   * @return Name of the property on the remote profile to use for user identification
   */
  public String getIdProperty();

  /**
   * @return GeoPrism client id assigned by the Oauth server
   */
  public String getClientId();

  /**
   * @return GeoPrism client secret key assigned by the Oauth server
   */
  public String getClientSecret();

  public String getUrl();
}