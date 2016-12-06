package net.geoprism.account;

import java.util.List;
import java.util.Locale;

import net.geoprism.ConfigurationService;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONObject;

import com.runwaysdk.facade.Facade;

public class OauthHandler implements OauthHandlerIF
{

  @Override
  public String createSession(String serverId, String code, Locale[] locales) throws Exception
  {
    List<OauthServerIF> servers = ConfigurationService.getOauthServers();

    for (OauthServerIF server : servers)
    {
      if (server.getServerId().equals(serverId))
      {
        /*
         * Get the access token
         */
        TokenRequestBuilder tokenBuilder = OAuthClientRequest.tokenLocation(server.getTokenLocation());
        tokenBuilder.setGrantType(GrantType.AUTHORIZATION_CODE);
        tokenBuilder.setClientId(server.getClientId());
        tokenBuilder.setClientSecret(server.getClientSecret());
        tokenBuilder.setRedirectURI("https://localhost:8443/geoprism/session/ologin");
        tokenBuilder.setCode(code);

        OAuthClientRequest tokenRequest = tokenBuilder.buildQueryMessage();
        tokenRequest.setHeader("Accept", "application/json");

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthJSONAccessTokenResponse accessToken = oAuthClient.accessToken(tokenRequest);

        /*
         * Request the user information
         */
        OAuthBearerClientRequest requestBuilder = new OAuthBearerClientRequest(server.getProfileLocation());
        requestBuilder.setAccessToken(accessToken.getAccessToken());

        OAuthClientRequest bearerRequest = requestBuilder.buildQueryMessage();
        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

        String body = resourceResponse.getBody();

        JSONObject object = new JSONObject(body);
        String id = object.getString(server.getIdProperty());

        return Facade.login("admin", "_nm8P4gfdWxGqNRQ#8", locales);
      }
    }

    return null;
  }

}
