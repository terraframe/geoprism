package net.geoprism.session;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonArray;

public interface SessionServiceIF
{
  public JsonArray getInstalledLocales(String sessionId);
  
  public String getServerVersion();
  
  public String getHomeUrl();
  
  public String getLoginUrl();
  
  public Set<String> getPublicEndpoints();
  
  public boolean isPublic(HttpServletRequest req);
  
  public boolean pathAllowed(HttpServletRequest req);
  
  public void onLoginSuccess(String username, String sessionId);
  
  public void onLoginFailure(String username);
}
