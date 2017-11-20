package net.geoprism;

import java.util.Date;

import net.geoprism.localization.LocalizationFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

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
   * Completes the forgot password request by verifying the token is valid, changing the user's password to the one provided, and then invalidating the request. 
   * 
   * @param token
   */
  @Authenticate // TODO : Is there any problem with having both these annotations on the same method?
  @Transaction
  public static void complete(String token, String newPassword)
  {
    if (newPassword == null || newPassword.equals("")) { return; }
    
    ForgotPasswordRequestQuery reqQ = new ForgotPasswordRequestQuery(new QueryFactory());
    reqQ.WHERE(reqQ.getToken().EQ(token));
    OIterator<? extends ForgotPasswordRequest> reqIt = reqQ.getIterator();
    
    ForgotPasswordRequest req;
    if (reqIt.hasNext())
    {
      req = reqIt.next();
      req.appLock();
    }
    else
    {
      throw new InvalidForgotPasswordToken();
    }
    
    if ((System.currentTimeMillis() - req.getStartTime().getTime()) > (expireTime * 3600000))
    {
      throw new InvalidForgotPasswordToken();
    }
    
//    req.changeUserPassword(newPassword);
    GeoprismUser user = req.getUserRef();
    user.appLock();
    user.setPassword(newPassword);
    user.apply();
    
    req.delete();
  }
  
  /**
   * Changes the users password to the one provided.
   * 
   * @param newPassword
   */
//  private void changeUserPassword(String newPassword)
//  {
//    GeoprismUser user = this.getUserRef();
//    user.appLock();
//    user.setPassword(newPassword);
//    user.apply();
//  }
  
  /**
   * MdMethod
   * 
   * Initiates a forgot password request. If the user already has one in progress, it will be invalidated and a new one will be issued. If the
   * server's email settings have not been properly set up, or the user does not exist, an error will be thrown.
   * 
   * @param username
   */
  @Authenticate // TODO : Is there any problem with having both these annotations on the same method?
  @Transaction
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
  
  private void sendEmail(String serverExternalUrl)
  {
    String address = this.getUserRef().getEmail();
    String link = serverExternalUrl + "/prism/admin#/forgotpassword-complete/" + this.getToken();
    
    String subject = LocalizationFacade.getFromBundles("forgotpassword.emailSubject");
    String body = LocalizationFacade.getFromBundles("forgotpassword.emailBody");
    body = body.replace("${link}", link);
    body = body.replace("${expireTime}", String.valueOf(expireTime));
    
    EmailSetting.sendEmail(subject, body, new String[]{address});
  }
  
  private String generateEncryptedToken()
  {
    String hashedTime = ServerIDGenerator.hashedId(String.valueOf(System.currentTimeMillis()));
    
    return hashedTime;
  }
}
