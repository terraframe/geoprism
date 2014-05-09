package com.runwaysdk.geodashboard.gis;

@com.runwaysdk.business.ClassSignature(hash = 1711280602)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to SessionMapLimitException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class SessionMapLimitExceptionBase extends com.runwaysdk.business.SmartException implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.SessionMapLimitException";
  public static java.lang.String ID = "id";
  public static java.lang.String MAPLIMIT = "mapLimit";
  private static final long serialVersionUID = 1711280602;
  
  public SessionMapLimitExceptionBase()
  {
    super();
  }
  
  public SessionMapLimitExceptionBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public SessionMapLimitExceptionBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public SessionMapLimitExceptionBase(java.lang.Throwable cause)
  {
    super(cause);
  }
  
  public String getId()
  {
    return getValue(ID);
  }
  
  public void validateId()
  {
    this.validateAttribute(ID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.SessionMapLimitException.CLASS);
    return mdClassIF.definesAttribute(ID);
  }
  
  public Integer getMapLimit()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(MAPLIMIT));
  }
  
  public void validateMapLimit()
  {
    this.validateAttribute(MAPLIMIT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getMapLimitMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.SessionMapLimitException.CLASS);
    return mdClassIF.definesAttribute(MAPLIMIT);
  }
  
  public void setMapLimit(Integer value)
  {
    if(value == null)
    {
      setValue(MAPLIMIT, "");
    }
    else
    {
      setValue(MAPLIMIT, java.lang.Integer.toString(value));
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{id}", this.getId());
    message = replace(message, "{mapLimit}", this.getMapLimit());
    return message;
  }
  
}
