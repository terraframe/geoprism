package net.geoprism.forgotpassword.business;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.localization.LocalizationFacade;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

import net.geoprism.ForgotPasswordRequest;
import net.geoprism.ForgotPasswordRequestQuery;
import net.geoprism.GeoprismUser;
import net.geoprism.GeoprismUserQuery;
import net.geoprism.InvalidForgotPasswordToken;
import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.email.business.EmailBusinessServiceIF;
import net.geoprism.session.SessionServiceIF;

@Component
public class ForgotPasswordBusinessService implements ForgotPasswordBusinessServiceIF
{
  private static Logger logger = LoggerFactory.getLogger(ForgotPasswordBusinessService.class);
  
  protected static final int expireTime = GeoprismProperties.getForgotPasswordExpireTime(); // in hours
  
  @Autowired
  protected SessionServiceIF sessionService;
  
  @Autowired
  protected EmailBusinessServiceIF emailService;
  
  /**
   * Initiates a forgot password request. If the user already has one in progress, it will be invalidated and a new one will be issued. If the
   * server's email settings have not been properly set up, or the user does not exist, an error will be thrown.
   * 
   * @param username
   */
  @Transaction
  public void initiate(String username)
  {
    GeoprismUserQuery q = new GeoprismUserQuery(new QueryFactory());
    q.WHERE(q.getUsername().EQ(username));
    OIterator<? extends GeoprismUser> it = q.getIterator();
    
    if (!it.hasNext())
    {
      throw new DataNotFoundException("User [" + username + "] does not exist.", MdBusinessDAO.getMdBusinessDAO(GeoprismUser.CLASS));
    }
    
    GeoprismUser user = it.next();
    
    validateInitiate(user);
    
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
    req.setToken(this.generateEncryptedToken(user));
    req.apply();
    
    this.sendEmail(req.getUserRef(), req.getToken());
  }
  
  protected void validateInitiate(GeoprismUser user)
  {
    // Can't forgot password on an OAuth user...
  }
  
  /**
   * Completes the forgot password request by verifying the token is valid, changing the user's password to the one provided, and then invalidating the request. 
   * 
   * @param token
   * @param newPassword
   */
  @Transaction
  public void complete(String token, String newPassword)
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
    
    logger.warn("Password for user [" + user.getUsername() + "] has been changed via ForgotPasswordRequest.");
  }
  
  protected void sendEmail(GeoprismUser user, String token)
  {
    final String serverExternalUrl = GeoprismProperties.getRemoteServerUrl();
    
    String address = user.getEmail();
    String link = serverExternalUrl + "#/forgotpassword-complete/" + token;
    
    String subject = LocalizationFacade.localize("forgotpassword.emailSubject");
    String body = LocalizationFacade.localize("forgotpassword.emailBody");
    body = body.replaceAll("\\\\n", "\n");
    body = body.replace("${link}", link);
    body = body.replace("${expireTime}", String.valueOf(expireTime));
    
    this.emailService.sendEmail(subject, body, new String[]{address});
  }
  
  protected String generateEncryptedToken(GeoprismUser user)
  {
    String hashedTime = UUID.nameUUIDFromBytes(String.valueOf(System.currentTimeMillis()).getBytes()).toString();
    
    String hashedUser = UUID.nameUUIDFromBytes(user.getOid().getBytes()).toString();
    
    return hashedTime + hashedUser;
  }
}
