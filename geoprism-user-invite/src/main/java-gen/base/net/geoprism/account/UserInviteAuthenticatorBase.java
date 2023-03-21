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
package net.geoprism.account;

@com.runwaysdk.business.ClassSignature(hash = -772336184)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to UserInviteAuthenticator.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class UserInviteAuthenticatorBase extends com.runwaysdk.business.Util
{
  public final static String CLASS = "net.geoprism.account.UserInviteAuthenticator";
  public final static java.lang.String OID = "oid";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -772336184;
  
  public UserInviteAuthenticatorBase()
  {
    super();
  }
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateOid()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.account.UserInviteAuthenticator.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static UserInviteAuthenticator get(String oid)
  {
    return (UserInviteAuthenticator) com.runwaysdk.business.Util.get(oid);
  }
  
  public void complete(java.lang.String token, java.lang.String user)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.account.UserInviteAuthenticator.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static final void complete(java.lang.String oid, java.lang.String token, java.lang.String user)
  {
    UserInviteAuthenticator _instance = UserInviteAuthenticator.get(oid);
    _instance.complete(token, user);
  }
  
  public void initiate(java.lang.String invite, java.lang.String roleIds)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.account.UserInviteAuthenticator.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static final void initiate(java.lang.String oid, java.lang.String invite, java.lang.String roleIds)
  {
    UserInviteAuthenticator _instance = UserInviteAuthenticator.get(oid);
    _instance.initiate(invite, roleIds);
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}
