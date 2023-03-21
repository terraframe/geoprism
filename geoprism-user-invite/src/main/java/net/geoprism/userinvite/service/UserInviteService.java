package net.geoprism.userinvite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.account.UserInviteAuthenticator;

@Component
public class UserInviteService implements UserInviteServiceIF
{

  @Autowired
  protected ApplicationContext applicationContext;
  
  @Request(RequestType.SESSION)
  public void initiate(String sessionId, String invite, String roleIds)
  {
    new UserInviteAuthenticator(applicationContext).initiate(invite, roleIds);
  }
  
  @Request(RequestType.SESSION)
  public void complete(String sessionId, String token, String user)
  {
    new UserInviteAuthenticator(applicationContext).complete(token, user);
  }
  
}
