package net.geoprism.account;

import org.springframework.context.ApplicationContext;

import com.runwaysdk.business.rbac.Authenticate;

import net.geoprism.externalprofile.business.ExternalProfileBusinessServiceIF;

public class ExternalProfileAuthenticator extends ExternalProfileAuthenticatorBase
{
  private static final long serialVersionUID = 1328523372;
  
  protected ApplicationContext applicationContext;
  
  public ExternalProfileAuthenticator(ApplicationContext applicationContext)
  {
    super();
    this.applicationContext = applicationContext;
  }
  
  public ExternalProfileAuthenticator()
  {
    super();
  }
  
  @Authenticate
  public String ologin(java.lang.String json)
  {
    return this.applicationContext.getBean(ExternalProfileBusinessServiceIF.class).ologin(json);
  }
}
