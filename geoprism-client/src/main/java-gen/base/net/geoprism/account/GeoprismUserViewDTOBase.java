package net.geoprism.account;

@com.runwaysdk.business.ClassSignature(hash = 1740460611)
public abstract class GeoprismUserViewDTOBase extends com.runwaysdk.business.ViewDTO
{
  public final static String CLASS = "net.geoprism.account.GeoprismUserView";
  private static final long serialVersionUID = 1740460611;
  
  protected GeoprismUserViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String EMAIL = "email";
  public static java.lang.String FIRSTNAME = "firstName";
  public static java.lang.String INACTIVE = "inactive";
  public static java.lang.String LASTNAME = "lastName";
  public static java.lang.String NEWINSTANCE = "newInstance";
  public static java.lang.String OID = "oid";
  public static java.lang.String PHONENUMBER = "phoneNumber";
  public static java.lang.String ROLES = "roles";
  public static java.lang.String USER = "user";
  public static java.lang.String USERNAME = "username";
  public String getEmail()
  {
    return getValue(EMAIL);
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
  
  public boolean isEmailWritable()
  {
    return isWritable(EMAIL);
  }
  
  public boolean isEmailReadable()
  {
    return isReadable(EMAIL);
  }
  
  public boolean isEmailModified()
  {
    return isModified(EMAIL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getEmailMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(EMAIL).getAttributeMdDTO();
  }
  
  public String getFirstName()
  {
    return getValue(FIRSTNAME);
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
  
  public boolean isFirstNameWritable()
  {
    return isWritable(FIRSTNAME);
  }
  
  public boolean isFirstNameReadable()
  {
    return isReadable(FIRSTNAME);
  }
  
  public boolean isFirstNameModified()
  {
    return isModified(FIRSTNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getFirstNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(FIRSTNAME).getAttributeMdDTO();
  }
  
  public Boolean getInactive()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(INACTIVE));
  }
  
  public void setInactive(Boolean value)
  {
    if(value == null)
    {
      setValue(INACTIVE, "");
    }
    else
    {
      setValue(INACTIVE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isInactiveWritable()
  {
    return isWritable(INACTIVE);
  }
  
  public boolean isInactiveReadable()
  {
    return isReadable(INACTIVE);
  }
  
  public boolean isInactiveModified()
  {
    return isModified(INACTIVE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getInactiveMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(INACTIVE).getAttributeMdDTO();
  }
  
  public String getLastName()
  {
    return getValue(LASTNAME);
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
  
  public boolean isLastNameWritable()
  {
    return isWritable(LASTNAME);
  }
  
  public boolean isLastNameReadable()
  {
    return isReadable(LASTNAME);
  }
  
  public boolean isLastNameModified()
  {
    return isModified(LASTNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getLastNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(LASTNAME).getAttributeMdDTO();
  }
  
  public Boolean getNewInstance()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(NEWINSTANCE));
  }
  
  public void setNewInstance(Boolean value)
  {
    if(value == null)
    {
      setValue(NEWINSTANCE, "");
    }
    else
    {
      setValue(NEWINSTANCE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isNewInstanceWritable()
  {
    return isWritable(NEWINSTANCE);
  }
  
  public boolean isNewInstanceReadable()
  {
    return isReadable(NEWINSTANCE);
  }
  
  public boolean isNewInstanceModified()
  {
    return isModified(NEWINSTANCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getNewInstanceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(NEWINSTANCE).getAttributeMdDTO();
  }
  
  public String getPhoneNumber()
  {
    return getValue(PHONENUMBER);
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
  
  public boolean isPhoneNumberWritable()
  {
    return isWritable(PHONENUMBER);
  }
  
  public boolean isPhoneNumberReadable()
  {
    return isReadable(PHONENUMBER);
  }
  
  public boolean isPhoneNumberModified()
  {
    return isModified(PHONENUMBER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getPhoneNumberMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PHONENUMBER).getAttributeMdDTO();
  }
  
  public String getRoles()
  {
    return getValue(ROLES);
  }
  
  public void setRoles(String value)
  {
    if(value == null)
    {
      setValue(ROLES, "");
    }
    else
    {
      setValue(ROLES, value);
    }
  }
  
  public boolean isRolesWritable()
  {
    return isWritable(ROLES);
  }
  
  public boolean isRolesReadable()
  {
    return isReadable(ROLES);
  }
  
  public boolean isRolesModified()
  {
    return isModified(ROLES);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getRolesMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(ROLES).getAttributeMdDTO();
  }
  
  public net.geoprism.GeoprismUserDTO getUser()
  {
    if(getValue(USER) == null || getValue(USER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return net.geoprism.GeoprismUserDTO.get(getRequest(), getValue(USER));
    }
  }
  
  public String getUserOid()
  {
    return getValue(USER);
  }
  
  public void setUser(net.geoprism.GeoprismUserDTO value)
  {
    if(value == null)
    {
      setValue(USER, "");
    }
    else
    {
      setValue(USER, value.getOid());
    }
  }
  
  public boolean isUserWritable()
  {
    return isWritable(USER);
  }
  
  public boolean isUserReadable()
  {
    return isReadable(USER);
  }
  
  public boolean isUserModified()
  {
    return isModified(USER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getUserMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(USER).getAttributeMdDTO();
  }
  
  public String getUsername()
  {
    return getValue(USERNAME);
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
  
  public boolean isUsernameWritable()
  {
    return isWritable(USERNAME);
  }
  
  public boolean isUsernameReadable()
  {
    return isReadable(USERNAME);
  }
  
  public boolean isUsernameModified()
  {
    return isModified(USERNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getUsernameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(USERNAME).getAttributeMdDTO();
  }
  
  public static final java.lang.String page(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Integer pageNumber)
  {
    String[] _declaredTypes = new String[]{"java.lang.Integer"};
    Object[] _parameters = new Object[]{pageNumber};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.account.GeoprismUserViewDTO.CLASS, "page", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static GeoprismUserViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(oid);
    
    return (GeoprismUserViewDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createSessionComponent(this);
    }
    else
    {
      getRequest().update(this);
    }
  }
  public void delete()
  {
    getRequest().delete(this.getOid());
  }
  
}
