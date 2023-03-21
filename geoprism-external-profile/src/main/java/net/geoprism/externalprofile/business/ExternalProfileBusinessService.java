package net.geoprism.externalprofile.business;

import java.util.Base64;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.InvalidLoginException;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.system.Users;
import com.runwaysdk.system.UsersQuery;

import net.geoprism.GeoprismUser;
import net.geoprism.account.LocaleSerializer;
import net.geoprism.account.OauthServer;
import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.session.UserNotFoundException;
import net.geoprism.session.UserNotOuathEnabledException;

@Component
public class ExternalProfileBusinessService implements ExternalProfileBusinessServiceIF
{
  private Logger logger = LoggerFactory.getLogger(ExternalProfileBusinessService.class);

  @Override
  public String ologin(String json)
  {
    try
    {
      JsonObject joJson = JsonParser.parseString(json).getAsJsonObject();
      final String serverId = joJson.get("serverId").getAsString();
      final String code = joJson.get("code").getAsString();
      final String locales = joJson.get("locales").getAsString();
      
      final String redirect = buildRedirectURI();

      OauthServer server = OauthServer.get(serverId);
      /*
       * Get the access token
       */
      TokenRequestBuilder tokenBuilder = OAuthClientRequest.tokenLocation(server.getTokenLocation());
      tokenBuilder.setGrantType(GrantType.AUTHORIZATION_CODE);
      tokenBuilder.setRedirectURI(redirect);
      tokenBuilder.setCode(code);

      String auth = server.getClientId() + ":" + server.getSecretKey();

      OAuthClientRequest tokenRequest = tokenBuilder.buildBodyMessage();
      tokenRequest.setHeader("Accept", "application/json");
      tokenRequest.setHeader("Authorization", "Basic " + new String(Base64.getEncoder().encode(auth.getBytes())));

      URLConnectionClient connClient = new URLConnectionClient();
      OAuthClient oAuthClient = new OAuthClient(connClient);
      OAuthJSONAccessTokenResponse accessToken = oAuthClient.accessToken(tokenRequest, OAuth.HttpMethod.POST, OAuthJSONAccessTokenResponse.class);

      /*
       * Request the user information
       */
      OAuthBearerClientRequest requestBuilder = new OAuthBearerClientRequest(server.getProfileLocation());
      requestBuilder.setAccessToken(accessToken.getAccessToken());

      OAuthClientRequest bearerRequest = requestBuilder.buildQueryMessage();
      OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

      String body = resourceResponse.getBody();

      JsonObject object = JsonParser.parseString(body).getAsJsonObject();
      
      final String username = object.get("userCredentials").getAsJsonObject().get("username").getAsString();

      SingleActorDAOIF profile = getActor(server, username);

      String sessionId = SessionFacade.logIn(profile, LocaleSerializer.deserialize(locales));
      
      JsonObject jsonOut = new JsonObject();
      jsonOut.addProperty("sessionId", sessionId);
      jsonOut.addProperty("username", username);
      return jsonOut.toString();
    }
    catch (OAuthSystemException | OAuthProblemException e)
    {
      throw new InvalidLoginException(e);
    }
  }
  
  protected String buildRedirectURI()
  {
    return GeoprismProperties.getRemoteServerUrl() + "session/ologin";
  }
  
  @Transaction
  protected synchronized SingleActorDAOIF getActor(OauthServer server, String username)
  {
    UsersQuery query = new UsersQuery(new QueryFactory());
    query.WHERE(query.getUsername().EQ(username));
    OIterator<? extends Users> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        UserDAO user = (UserDAO) BusinessFacade.getEntityDAO(it.next());
        
        try
        {
          GeoprismUser geoprismUser = GeoprismUser.getByUsername(user.getUsername());
          
          validateUser(username, user, geoprismUser, server);
          
          return user;
        }
        catch (Throwable t)
        {
          logger.error("Encountered an unexpected error while logging user in.", t);
        }
        
        UserNotOuathEnabledException ex = new UserNotOuathEnabledException();
        ex.setUsername(user.getUsername());
        ex.setOauthServer(server.getDisplayLabel().getValue());
        throw ex;
      }
      else
      {
        UserNotFoundException ex = new UserNotFoundException();
        ex.setUsername(username);
        throw ex;
      }
    }
    finally
    {
      it.close();
    }
  }
  
  protected void validateUser(String username, UserDAO user, GeoprismUser geoprismUser, OauthServer server)
  {
 // TODO : Inject for CGR
//    UserInfo userInfo = UserInfo.getByUser(geoprismUser);
//    
//    ExternalSystem system = ExternalSystem.get(userInfo.getExternalSystemOid());
//    
//    if (system instanceof DHIS2ExternalSystem)
//    {
//      DHIS2ExternalSystem dhis2System = (DHIS2ExternalSystem) system;
//      
//      if (dhis2System.getOauthServerOid().equals(server.getOid()))
//      {
//        return user;
//      }
//    }
    
    // If the user doesn't pass validation you can throw:
//    UserNotOuathEnabledException ex = new UserNotOuathEnabledException();
//    ex.setUsername(geoprismUser.getUsername());
//    ex.setOauthServer(server.getDisplayLabel().getValue());
//    throw ex;
  }
  
}
