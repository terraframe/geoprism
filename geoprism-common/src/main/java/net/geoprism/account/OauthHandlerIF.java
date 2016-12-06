package net.geoprism.account;

import java.util.Locale;

public interface OauthHandlerIF
{

  public String createSession(String serverId, String code, Locale[] locales) throws Exception;

}
