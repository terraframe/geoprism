/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.localization.LocalizationFacade;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

import net.geoprism.GeoprismUser;
import net.geoprism.account.InvalidUserInviteToken;
import net.geoprism.account.UserInvite;
import net.geoprism.account.UserInviteQuery;
import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.service.request.UserInviteService;

@Service
public class UserInviteBusinessService implements UserInviteBusinessServiceIF
{

  private static final Logger logger = LoggerFactory.getLogger(UserInviteService.class);
  
  @Autowired
  protected EmailBusinessServiceIF emailService;
  
  @Autowired
  protected AccountBusinessServiceIF accountService;
  
  /**
    * Completes the user invite request by verifying the token is valid, creating
    * the requested user, and then invalidating the request.
    * 
    * @param token
    * @param jsonUser A serialized JSON object representing a user.
    */
  @Override
  @Transaction
  public void complete(String token, String jsonUser)
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

    if ( ( System.currentTimeMillis() - invite.getStartTime().getTime() ) > ( GeoprismProperties.getInviteUserTokenExpireTime() * 3600000L ))
    {
      throw new InvalidUserInviteToken();
    }
    
    JsonObject joUser = JsonParser.parseString(jsonUser).getAsJsonObject();
    
    Set<String> roleIds = new HashSet<String>();

    if (invite.getRoleIds().length() > 0)
    {
      JSONArray array = new JSONArray(invite.getRoleIds());

      for (int i = 0; i < array.length(); i++)
      {
        roleIds.add(array.getString(i));
      }
    }
    
    applyUserWithRoles(joUser, roleIds);

    invite.delete();

    logger.info("User [" + joUser.get(GeoprismUser.USERNAME) + "] has been created via a user invite.");
  }
  
  protected GeoprismUser deserializeUser(JsonObject joUser)
  {
    GeoprismUser user = null;

    if (joUser.has(GeoprismUser.OID))
    {
      String userId = joUser.get(GeoprismUser.OID).getAsString();

      user = GeoprismUser.get(userId);
    }
    else
    {
      user = new GeoprismUser();
    }

    user.setUsername(joUser.get(GeoprismUser.USERNAME).getAsString());
    user.setFirstName(joUser.get(GeoprismUser.FIRSTNAME).getAsString());
    user.setLastName(joUser.get(GeoprismUser.LASTNAME).getAsString());
    user.setEmail(joUser.get(GeoprismUser.EMAIL).getAsString());

    if (joUser.has(GeoprismUser.PHONENUMBER) && joUser.get(GeoprismUser.PHONENUMBER).getAsString().length() > 0)
    {
      user.setPhoneNumber(joUser.get(GeoprismUser.PHONENUMBER).getAsString());
    }

    if (joUser.has(GeoprismUser.INACTIVE))
    {
      user.setInactive(joUser.get(GeoprismUser.INACTIVE).getAsBoolean());
    }

    if (joUser.has(GeoprismUser.PASSWORD) && joUser.get(GeoprismUser.PASSWORD).getAsString().length() > 0)
    {
      String password = joUser.get(GeoprismUser.PASSWORD).getAsString();

      if (password != null && password.length() > 0)
      {
        user.setPassword(password);
      }
    }

    return user;
  }
  
  protected void applyUserWithRoles(JsonObject joUser, Set<String> roleIds)
  {
    GeoprismUser user = deserializeUser(joUser);
    
    accountService.applyUserWithRoles(user, roleIds);
  }

  /**
    * 
    * Initiates a user invite request. If the user already has one in progress,
    * it will be invalidated and a new one will be issued. If the server's email
    * settings have not been properly set up, or the user does not exist, an
    * error will be thrown.
    * 
    * @param username
    */
  @Override
  @Transaction
  public void initiate(String sInvite, String roleIds)
  {
    if (roleIds == null || roleIds.length() == 0 || JsonParser.parseString(roleIds).getAsJsonArray().size() == 0)
    {
      // TODO : Better Error
      throw new AttributeValueException("You're attempting to invite a user with zero roles?", "");
    }
    
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
    invite.setToken(this.generateEncryptedToken(invite.getEmail()));
    invite.setRoleIds(roleIds);

    invite.apply();
    
    JsonArray roleNameArray = JsonParser.parseString(roleIds).getAsJsonArray();

    this.sendEmail(invite.getEmail(), invite.getToken(), roleNameArray);
  }
  
  protected void sendEmail(String emailAddress, String token, JsonArray roleNameArray)
  {
    final String serverExternalUrl = GeoprismProperties.getRemoteServerUrl();
    
    String link = serverExternalUrl + "#/admin/invite-complete/" + token;

    String subject = LocalizationFacade.localize("user.invite.email.subject");
    String body = LocalizationFacade.localize("user.invite.email.body");
    body = body.replaceAll("\\\\n", "\n");
    body = body.replace("${link}", link);
    body = body.replace("${expireTime}", getLocalizedExpireTime());

    this.emailService.sendEmail(subject, body, new String[] { emailAddress });
  }

  protected String generateEncryptedToken(String email)
  {
    String hashedTime = UUID.nameUUIDFromBytes(String.valueOf(System.currentTimeMillis()).getBytes()).toString();

    String hashedEmail = UUID.nameUUIDFromBytes(email.getBytes()).toString();

    return hashedTime + hashedEmail;
  }
  
  protected static String getLocalizedExpireTime()
  {
    final int expireTimeInHours = GeoprismProperties.getInviteUserTokenExpireTime();
    
    final String hours = LocalizationFacade.localize("user.invite.email.hours");
    final String days = LocalizationFacade.localize("user.invite.email.days");
    final String hoursAndDays = LocalizationFacade.localize("user.invite.email.hoursAndDays");
    
    if (expireTimeInHours < 24)
    {
      return hours.replace("${hours}", String.valueOf(expireTimeInHours));
    }
    else if (expireTimeInHours % 24 == 0)
    {
      return days.replace("${days}", String.valueOf(expireTimeInHours/24));
    }
    else
    {
      return hoursAndDays.replace("${days}", String.valueOf(expireTimeInHours/24)).replace("${hours}", String.valueOf(expireTimeInHours - (24 * (expireTimeInHours/24))));
    }
  }
  
}
