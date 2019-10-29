package net.geoprism.account;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.LocalizationFacade;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

import net.geoprism.EmailSetting;
import net.geoprism.GeoprismProperties;
import net.geoprism.GeoprismUser;

public class UserInvite extends UserInviteBase
{
  private static final long serialVersionUID = 121923297;
  
  private static final int  expireTime       = GeoprismProperties.getInviteUserTokenExpireTime(); // in
                                                                                             // hours

  private static Logger     logger           = LoggerFactory.getLogger(UserInvite.class);

  public UserInvite()
  {
    super();
  }

  /**
   * This is done for permissions reasons. Runway is throwing an exception when
   * we try to create a UserDTO on the client and so we're bypassing it this
   * way.
   */
  public static net.geoprism.GeoprismUser newUserInst()
  {
    return new GeoprismUser();
  }

  /**
   * MdMethod
   * 
   * Initiates a user invite request. If the user already has one in progress,
   * it will be invalidated and a new one will be issued. If the server's email
   * settings have not been properly set up, or the user does not exist, an
   * error will be thrown.
   * 
   * @param username
   */
  @Authenticate
  public static void initiate(String invite, String roleIds, String serverExternalUrl)
  {
    initiateInTrans(invite, roleIds, serverExternalUrl);
  }

  @Transaction
  public static void initiateInTrans(String sInvite, String roleIds, String serverExternalUrl)
  {
    JSONObject joInvite = new JSONObject(sInvite);

    String email = joInvite.getString("email");

    UserInvite invite = new UserInvite();
    invite.setEmail(email);

    UserInviteQuery query = new UserInviteQuery(new QueryFactory());
    query.WHERE(query.getEmail().EQi(invite.getEmail()));
    OIterator<? extends UserInvite> it = query.getIterator();

    while (it.hasNext())
    {
      it.next().delete();
    }

    invite.setStartTime(new Date());
    invite.setToken(UserInvite.generateEncryptedToken(invite.getEmail()));
    invite.setRoleIds(roleIds);

    invite.apply();

    invite.sendEmail(serverExternalUrl);
  }

  /**
   * MdMethod
   * 
   * Completes the user invite request by verifying the token is valid, creating
   * the requested user, and then invalidating the request.
   * 
   * @param token
   */
  @Authenticate
  public static void complete(java.lang.String token, net.geoprism.GeoprismUser user)
  {
    completeInTrans(token, user);
  }

  @Transaction
  private static void completeInTrans(java.lang.String token, net.geoprism.GeoprismUser user)
  {
    UserInviteQuery query = new UserInviteQuery(new QueryFactory());
    query.WHERE(query.getToken().EQ(token));
    OIterator<? extends UserInvite> reqIt = query.getIterator();

    UserInvite invite;
    if (reqIt.hasNext())
    {
      invite = reqIt.next();
      invite.appLock();
    }
    else
    {
      throw new InvalidUserInviteToken();
    }

    if ( ( System.currentTimeMillis() - invite.getStartTime().getTime() ) > ( expireTime * 3600000 ))
    {
      throw new InvalidUserInviteToken();
    }

    if (invite.getRoleIds().length() > 0)
    {
      JSONArray array = new JSONArray(invite.getRoleIds());
      List<String> list = new LinkedList<String>();

      for (int i = 0; i < array.length(); i++)
      {
        list.add(array.getString(i));
      }

      user.applyWithRoles(list.toArray(new String[list.size()]));
    }
    else
    {
      user.apply();
    }

    invite.delete();

    logger.info("User [" + user.getUsername() + "] has been created via a user invite.");
  }

  private void sendEmail(String serverExternalUrl)
  {
    String address = this.getEmail();
    String link = serverExternalUrl + "/cgr/manage#/admin/invite-complete/" + this.getToken();

    String subject = LocalizationFacade.localize("user.invite.email.subject");
    String body = LocalizationFacade.localize("user.invite.email.body");
    body = body.replaceAll("\\\\n", "\n");
    body = body.replace("${link}", link);
    body = body.replace("${expireTime}", String.valueOf(expireTime));

    EmailSetting.sendEmail(subject, body, new String[] { address });
  }

  private static String generateEncryptedToken(String email)
  {
    String hashedTime = UUID.nameUUIDFromBytes(String.valueOf(System.currentTimeMillis()).getBytes()).toString();

    String hashedEmail = UUID.nameUUIDFromBytes(email.getBytes()).toString();

    return hashedTime + hashedEmail;
  }
  
}
