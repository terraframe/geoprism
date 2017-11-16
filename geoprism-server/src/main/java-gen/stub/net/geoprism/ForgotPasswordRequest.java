package net.geoprism;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
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
   * 
   * Verifies that the user owns the email address and allows them to log in.
   * 
   * @param token
   */
  @Authenticate
  public static void verify(String token)
  {
    
    
    // Don't forget to invalidate the token afterwards
  }
  
  /**
   * MdMethod
   * 
   * Initiates a forgot password request. If the user already has one in progress, it will be invalidated and a new one will be issued. If the
   * server's email settings have not been properly set up, or the user does not exist, an error will be thrown.
   * 
   * @param username
   */
  @Authenticate
  public static void initiate(String username, String serverExternalUrl)
  {
    GeoprismUserQuery q = new GeoprismUserQuery(new QueryFactory());
    q.WHERE(q.getUsername().EQ(username));
    OIterator<? extends GeoprismUser> it = q.getIterator();
    
    if (!it.hasNext())
    {
      throw new DataNotFoundException("User [" + username + "] does not exist.", MdBusinessDAO.getMdBusinessDAO(GeoprismUser.CLASS));
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
    
    String subject = LocalizationFacade.getFromBundles("forgotpassword.emailSubject");
    String body = LocalizationFacade.getFromBundles("forgotpassword.emailBody");
    body = body.replace("${link}", link);
    body = body.replace("${expireTime}", String.valueOf(expireTime));
    
    EmailSetting.sendEmail(subject, body, new String[]{address});
  }
  
  public String generateEncryptedToken()
  {
    String hashedTime = ServerIDGenerator.hashedId(String.valueOf(System.currentTimeMillis()));
    String hashedUser = ServerIDGenerator.hashedId(this.getUserRefId());
    
    return hashedTime + hashedUser;
  }
}
