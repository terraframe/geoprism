package net.geoprism.forgotpassword.business;

public interface ForgotPasswordBusinessServiceIF
{
  public void initiate(String username);
  
  public void complete(String token, String newPassword);
}
