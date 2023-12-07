package net.geoprism.registry.service;

public interface LoginGuardServiceIF
{
  public void guardLogin();
  
  public void loginFailed(final String key);
}
