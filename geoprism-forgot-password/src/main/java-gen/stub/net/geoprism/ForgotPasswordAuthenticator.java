package net.geoprism;

import org.springframework.context.ApplicationContext;

import com.runwaysdk.business.rbac.Authenticate;

import net.geoprism.forgotpassword.business.ForgotPasswordBusinessServiceIF;

public class ForgotPasswordAuthenticator extends ForgotPasswordAuthenticatorBase
{
  private static final long serialVersionUID = 1585492533;
  
  protected ApplicationContext applicationContext;
  
  public ForgotPasswordAuthenticator(ApplicationContext applicationContext)
  {
    super();
    this.applicationContext = applicationContext;
  }
  
  public ForgotPasswordAuthenticator()
  {
    super();
  }
  
  @Authenticate
  public void complete(java.lang.String token, java.lang.String newPassword)
  {
    this.applicationContext.getBean(ForgotPasswordBusinessServiceIF.class).complete(token, newPassword);
  }
  
  @Authenticate
  public void initiate(java.lang.String username)
  {
    this.applicationContext.getBean(ForgotPasswordBusinessServiceIF.class).initiate(username);
  }
}
