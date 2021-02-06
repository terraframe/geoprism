package net.geoprism.account;

@com.runwaysdk.business.ClassSignature(hash = -874531214)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ExternalProfile.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class ExternalProfileBase extends com.runwaysdk.system.SingleActor
{
  public final static String CLASS = "net.geoprism.account.ExternalProfile";
  public static java.lang.String DISPLAYNAME = "displayName";
  public static java.lang.String EMAIL = "email";
  public static java.lang.String FIRSTNAME = "firstName";
  public static java.lang.String LASTNAME = "lastName";
  public static java.lang.String PHONENUMBER = "phoneNumber";
  public static java.lang.String REMOTEID = "remoteId";
  public static java.lang.String SERVER = "server";
  public static java.lang.String USERNAME = "username";
  private static final long serialVersionUID = -874531214;
  
  public ExternalProfileBase()
  {
    super();
  }
  
  public String getDisplayName()
  {
    return getValue(DISPLAYNAME);
  }
  
  public void validateDisplayName()
  {
    this.validateAttribute(DISPLAYNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getDisplayNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.account.ExternalProfile.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(DISPLAYNAME);
  }
  
  public void setDisplayName(String value)
  {
    if(value == null)
    {
      setValue(DISPLAYNAME, "");
    }
    else
    {
      setValue(DISPLAYNAME, value);
    }
  }
  
  public String getEmail()
  {
    return getValue(EMAIL);
  }
  
  public void validateEmail()
  {
    this.validateAttribute(EMAIL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getEmailMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.account.ExternalProfile.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(EMAIL);
  }
  
  public void setEmail(String value)
  {
    if(value == null)
    {
      setValue(EMAIL, "");
    }
    else
    {
      setValue(EMAIL, value);
    }
  }
  
  public String getFirstName()
  {
    return getValue(FIRSTNAME);
  }
  
  public void validateFirstName()
  {
    this.validateAttribute(FIRSTNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getFirstNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.account.ExternalProfile.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(FIRSTNAME);
  }
  
  public void setFirstName(String value)
  {
    if(value == null)
    {
      setValue(FIRSTNAME, "");
    }
    else
    {
      setValue(FIRSTNAME, value);
    }
  }
  
  public String getLastName()
  {
    return getValue(LASTNAME);
  }
  
  public void validateLastName()
  {
    this.validateAttribute(LASTNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getLastNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.account.ExternalProfile.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(LASTNAME);
  }
  
  public void setLastName(String value)
  {
    if(value == null)
    {
      setValue(LASTNAME, "");
    }
    else
    {
      setValue(LASTNAME, value);
    }
  }
  
  public String getPhoneNumber()
  {
    return getValue(PHONENUMBER);
  }
  
  public void validatePhoneNumber()
  {
    this.validateAttribute(PHONENUMBER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getPhoneNumberMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.account.ExternalProfile.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(PHONENUMBER);
  }
  
  public void setPhoneNumber(String value)
  {
    if(value == null)
    {
      setValue(PHONENUMBER, "");
    }
    else
    {
      setValue(PHONENUMBER, value);
    }
  }
  
  public String getRemoteId()
  {
    return getValue(REMOTEID);
  }
  
  public void validateRemoteId()
  {
    this.validateAttribute(REMOTEID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getRemoteIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.account.ExternalProfile.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(REMOTEID);
  }
  
  public void setRemoteId(String value)
  {
    if(value == null)
    {
      setValue(REMOTEID, "");
    }
    else
    {
      setValue(REMOTEID, value);
    }
  }
  
  public net.geoprism.account.OauthServer getServer()
  {
    if (getValue(SERVER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return net.geoprism.account.OauthServer.get(getValue(SERVER));
    }
  }
  
  public String getServerOid()
  {
    return getValue(SERVER);
  }
  
  public void validateServer()
  {
    this.validateAttribute(SERVER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getServerMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.account.ExternalProfile.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(SERVER);
  }
  
  public void setServer(net.geoprism.account.OauthServer value)
  {
    if(value == null)
    {
      setValue(SERVER, "");
    }
    else
    {
      setValue(SERVER, value.getOid());
    }
  }
  
  public void setServerId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(SERVER, "");
    }
    else
    {
      setValue(SERVER, oid);
    }
  }
  
  public String getUsername()
  {
    return getValue(USERNAME);
  }
  
  public void validateUsername()
  {
    this.validateAttribute(USERNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getUsernameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.account.ExternalProfile.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(USERNAME);
  }
  
  public void setUsername(String value)
  {
    if(value == null)
    {
      setValue(USERNAME, "");
    }
    else
    {
      setValue(USERNAME, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static ExternalProfileQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    ExternalProfileQuery query = new ExternalProfileQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static ExternalProfile get(String oid)
  {
    return (ExternalProfile) com.runwaysdk.business.Business.get(oid);
  }
  
  public static ExternalProfile getByKey(String key)
  {
    return (ExternalProfile) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static java.lang.String login(java.lang.String serverId, java.lang.String code, java.lang.String locales, java.lang.String redirectBase)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.account.ExternalProfile.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static ExternalProfile lock(java.lang.String oid)
  {
    ExternalProfile _instance = ExternalProfile.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static ExternalProfile unlock(java.lang.String oid)
  {
    ExternalProfile _instance = ExternalProfile.get(oid);
    _instance.unlock();
    
    return _instance;
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
