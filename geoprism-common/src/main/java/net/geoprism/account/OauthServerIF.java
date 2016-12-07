package net.geoprism.account;

public interface OauthServerIF
{
  public static final String SERVER_ID = "serverId";

  /**
   * @return Id of the Oauth server (e.g. The dhis2 instance)
   */
  public String getId();

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
   * @return GeoPrism client id assigned by the Oauth server
   */
  public String getClientId();

  /**
   * @return GeoPrism client secret key assigned by the Oauth server
   */
  public String getSecretKey();

  public String getUrl();
}