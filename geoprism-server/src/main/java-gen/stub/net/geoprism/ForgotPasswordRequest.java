/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

import net.geoprism.localization.LocalizationFacade;

public class ForgotPasswordRequest extends ForgotPasswordRequestBase 
{
  private static final long serialVersionUID = 961492736;
  
  private static final int expireTime = GeoprismProperties.getForgotPasswordExpireTime(); // in hours
  
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
  @Authenticate
  public static void complete(String token, String newPassword)
  {
    completeInTransaction(token, newPassword);
  }
  @Transaction
  public static void completeInTransaction(String token, String newPassword)
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
    
    // TODO : Info level is somewhat useless in a production environment. I'm not sure what else to log it as though.
    logger.info("Password for user [" + user.getUsername() + "] has been changed via ForgotPasswordRequest!");
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
  @Authenticate
  public static void initiate(String username, String serverExternalUrl)
  {
    initiateInRequest(username, serverExternalUrl);
  }
  @Transaction
  public static void initiateInRequest(String username, String serverExternalUrl)
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
    req.setToken(req.generateEncryptedToken(user));
    req.apply();
    
    req.sendEmail(serverExternalUrl);
  }
  
  private void sendEmail(String serverExternalUrl)
  {
    String address = this.getUserRef().getEmail();
    String link = serverExternalUrl + "/prism/home#/forgotpassword-complete/" + this.getToken();
    
    String subject = LocalizationFacade.getFromBundles("forgotpassword.emailSubject");
    String body = LocalizationFacade.getFromBundles("forgotpassword.emailBody");
    body = body.replace("${link}", link);
    body = body.replace("${expireTime}", String.valueOf(expireTime));
    
    EmailSetting.sendEmail(subject, body, new String[]{address});
  }
  
  private String generateEncryptedToken(GeoprismUser user)
  {
    String hashedTime = UUID.nameUUIDFromBytes(String.valueOf(System.currentTimeMillis()).getBytes()).toString();
    
    String hashedUser = UUID.nameUUIDFromBytes(user.getOid().getBytes()).toString();
    
    return hashedTime + hashedUser;
  }
}
