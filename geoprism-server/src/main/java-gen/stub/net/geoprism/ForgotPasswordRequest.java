package net.geoprism;

import java.util.ArrayList;
import java.util.Date;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

import net.geoprism.localization.LocalizationFacade;

public class ForgotPasswordRequest extends ForgotPasswordRequestBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 961492736;
  
  private static final int expireTime = 2; // in hours // TODO : Store in a properties file
  
  private static Logger logger = LoggerFactory.getLogger(ForgotPasswordRequest.class);
  
  public ForgotPasswordRequest()
  {
    super();
  }
  
  /**
   * MdMethod
   * Initiates a forgot password request. If the user already has one in progress, it will be invalidated and a new one will be issued. If the
   * server's email settings have not been properly set up, or the user does not exist, an error will be thrown.
   * 
   * @param username
   */
  public static void initiate(String username, String serverExternalUrl)
  {
    GeoprismUserQuery q = new GeoprismUserQuery(new QueryFactory());
    q.WHERE(q.getUsername().EQ(username));
    OIterator<? extends GeoprismUser> it = q.getIterator();
    
    if (!it.hasNext())
    {
      throw new RuntimeException("User not found."); // TODO LOCALIZE
    }
    
    GeoprismUser user = it.next();
    
    ForgotPasswordRequestQuery reqQ = new ForgotPasswordRequestQuery(new QueryFactory());
    reqQ.WHERE(reqQ.getUserRef().EQ(user));
    OIterator<? extends ForgotPasswordRequest> reqIt = reqQ.getIterator();
    
    ForgotPasswordRequest req;
    if (reqIt.hasNext())
    {
      req = reqIt.next();
      req.lock();
    }
    else
    {
      req = new ForgotPasswordRequest();
    }
    
    req.setUserRef(user);
    req.setStartTime(new Date());
    req.setToken(req.generateEncryptedToken());
    req.apply();
    
    req.sendEmail(serverExternalUrl);
  }
  
  public void sendEmail(String serverExternalUrl)
  {
    String address = this.getUserRef().getEmail();
    String link = serverExternalUrl + "/forgotpassword/verify?token=" + this.getToken();
    EmailSetting settings = EmailSetting.getDefault();
    
    if (settings.getPort() == null || settings.getServer() == null || settings.getUsername() == null || settings.getPassword() == null || address == null || address.contains("@noreply"))
    {
      throw new RuntimeException("Bad email settings"); // TODO LOCALIZE
    }
    
    ArrayList<InternetAddress> iaTos = new ArrayList<InternetAddress>();
    try
    {
      iaTos.add(new InternetAddress(this.getUserRef().getEmail().trim()));
    }
    catch (AddressException e)
    {
      logger.error("Problem sending email to address [" + address + "].", e);
      throw new RuntimeException(e); // TODO : Localize
    }
    String subject = LocalizationFacade.getFromBundles("forgotpassword.emailSubject");
    String body = LocalizationFacade.getFromBundles("forgotpassword.emailBody");
    body = body.replace("${link}", link);
    body = body.replace("${expireTime}", String.valueOf(expireTime));
    try
    {
      Email email = new SimpleEmail();
      email.setHostName(settings.getServer());
      email.setSmtpPort(settings.getPort());
      email.setAuthenticator(new DefaultAuthenticator(settings.getUsername(), settings.getPassword()));
      email.setSSLOnConnect(true);
      email.setFrom(settings.getFrom());
      email.setSubject(subject);
      email.setMsg(body);
      email.setTo(iaTos);
      email.send();
    }
    catch (EmailException e)
    {
      logger.error("Error sending email with body [" + body + "] to recipients [" + address + "].", e);
      throw new RuntimeException(e); // TODO : Localize
    }
  }
  
  public String generateEncryptedToken()
  {
    String hashedTime = ServerIDGenerator.hashedId(String.valueOf(System.currentTimeMillis()));
    String hashedUser = ServerIDGenerator.hashedId(this.getUserRefId());
    
    return hashedTime + hashedUser;
  }
}
