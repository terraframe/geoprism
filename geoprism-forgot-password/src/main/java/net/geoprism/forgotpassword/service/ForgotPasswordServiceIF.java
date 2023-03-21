package net.geoprism.forgotpassword.service;

public interface ForgotPasswordServiceIF
{
  public void initiate(String sessionId, String username);
  
  public void complete(String sessionId, String token, String username);
}
