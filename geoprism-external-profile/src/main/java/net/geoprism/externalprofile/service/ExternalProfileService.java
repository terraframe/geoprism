package net.geoprism.externalprofile.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.account.ExternalProfileAuthenticator;

@Component
public class ExternalProfileService implements ExternalProfileServiceIF
{
  private Logger logger = LoggerFactory.getLogger(ExternalProfileService.class);
  
  @Autowired
  protected ApplicationContext applicationContext;

  @Request(RequestType.SESSION)
  public String ologin(String sessionId, String json)
  {
    return new ExternalProfileAuthenticator(applicationContext).ologin(json);
  }
}
