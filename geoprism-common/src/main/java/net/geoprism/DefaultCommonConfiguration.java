package net.geoprism;

import java.util.LinkedList;
import java.util.List;

import net.geoprism.account.OauthServer;
import net.geoprism.account.OauthServerIF;

public class DefaultCommonConfiguration implements CommonConfigurationIF
{

  @Override
  public List<OauthServerIF> getOauthServers()
  {
    OauthServer server = new OauthServer();
    server.setServerId("DHIS2");
    server.setAuthorizationLocation("http://127.0.0.1:8085/uaa/oauth/authorize");
    server.setTokenLocation("http://127.0.0.1:8085/uaa/oauth/token");
    server.setProfileLocation("http://127.0.0.1:8085/api/me");
    server.setIdProperty("id");
    server.setClientId("geoprism");
    server.setClientSecret("3b66bf6f0-8b24-2fc1-24c0-0709f3a6b09");

    LinkedList<OauthServerIF> servers = new LinkedList<OauthServerIF>();
    servers.add(server);

    return servers;
  }

}
