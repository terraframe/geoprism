package net.geoprism.account;

import net.geoprism.DefaultConfiguration;
import net.geoprism.GeoprismUserIF;

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
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.InvalidLoginException;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.system.AssignmentsQuery;
import com.runwaysdk.system.Roles;

public class ExternalProfile extends ExternalProfileBase implements Reloadable, GeoprismUserIF
{
  private static final long serialVersionUID = -377482924;

  public ExternalProfile()
  {
    super();
  }

  @Override
  public Boolean isAssigned(Roles role)
  {
    if (role != null)
    {
      QueryFactory factory = new QueryFactory();

      AssignmentsQuery query = new AssignmentsQuery(factory);
      query.WHERE(query.getParent().EQ(this));
      query.AND(query.getChild().EQ(role));

      return ( query.getCount() > 0 );
    }

    return false;
  }

  @Authenticate
  public static String login(String serverId, String code, String locales)
  {
    try
    {
      String redirect = DeployProperties.getApplicationURL() + "/session/ologin";

      OauthServer server = OauthServer.get(serverId);
      /*
       * Get the access token
       */
      TokenRequestBuilder tokenBuilder = OAuthClientRequest.tokenLocation(server.getTokenLocation());
      tokenBuilder.setGrantType(GrantType.AUTHORIZATION_CODE);
      tokenBuilder.setClientId(server.getClientId());
      tokenBuilder.setClientSecret(server.getSecretKey());
      tokenBuilder.setRedirectURI(redirect);
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

      SingleActorDAOIF profile = ExternalProfile.getOrCreate(server, object);

      return SessionFacade.logIn(profile, LocaleSerializer.deserialize(locales));
    }
    catch (JSONException | OAuthSystemException | OAuthProblemException e)
    {
      throw new InvalidLoginException(e);
    }
  }

  @Transaction
  private static synchronized SingleActorDAOIF getOrCreate(OauthServer server, JSONObject object) throws JSONException
  {
    String serverType = server.getServerType();

    String remoteId = OauthServer.getRemoteId(serverType, object);

    ExternalProfileQuery query = new ExternalProfileQuery(new QueryFactory());
    query.WHERE(query.getRemoteId().EQ(remoteId));
    OIterator<? extends ExternalProfile> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        ExternalProfile profile = it.next();
        profile.lock();
        OauthServer.populate(serverType, profile, object);
        profile.apply();

        return (SingleActorDAOIF) BusinessFacade.getEntityDAO(profile);
      }
      else
      {
        ExternalProfile profile = new ExternalProfile();
        profile.setRemoteId(remoteId);
        profile.setServer(server);
        OauthServer.populate(serverType, profile, object);
        profile.apply();

        SingleActorDAOIF actor = (SingleActorDAOIF) BusinessFacade.getEntityDAO(profile);

        RoleDAO role = RoleDAO.findRole(DefaultConfiguration.ADMIN).getBusinessDAO();
        role.assignMember(actor);

        return actor;
      }
    }
    finally
    {
      it.close();
    }
  }

}
