package net.geoprism.account;

import org.springframework.context.ApplicationContext;

import com.runwaysdk.business.rbac.Authenticate;

import net.geoprism.userinvite.business.UserInviteBusinessServiceIF;

public class UserInviteAuthenticator extends UserInviteAuthenticatorBase
{
  private static final long serialVersionUID = 1197013198;
  
  private ApplicationContext applicationContext;
  
  public UserInviteAuthenticator(ApplicationContext applicationContext)
  {
    super();
    this.applicationContext = applicationContext;
  }
  
  public UserInviteAuthenticator()
  {
    super();
  }
  
  @Authenticate
  public void complete(String token, String jsonUser)
  {
    this.applicationContext.getBean(UserInviteBusinessServiceIF.class).complete(token, jsonUser);
  }
  
  @Authenticate
  public void initiate(String invite, String roleIds)
  {
    this.applicationContext.getBean(UserInviteBusinessServiceIF.class).initiate(invite, roleIds);
  }
}
