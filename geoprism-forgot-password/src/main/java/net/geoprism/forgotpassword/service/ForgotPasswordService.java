package net.geoprism.forgotpassword.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.ForgotPasswordAuthenticator;
import net.geoprism.configuration.GeoprismProperties;

@Component
public class ForgotPasswordService implements ForgotPasswordServiceIF
{
  private static Logger logger = LoggerFactory.getLogger(ForgotPasswordService.class);
  
  protected static final int expireTime = GeoprismProperties.getForgotPasswordExpireTime(); // in hours
  
  @Autowired
  protected ApplicationContext applicationContext;
  
  @Request(RequestType.SESSION)
  public void complete(String sessionId, String token, String username)
  {
    new ForgotPasswordAuthenticator(applicationContext).complete(token, username);
  }
  
  @Request(RequestType.SESSION)
  public void initiate(String sessionId, String username)
  {
    new ForgotPasswordAuthenticator(applicationContext).initiate(username);
  }
}
