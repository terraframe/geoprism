package net.geoprism.account;

@com.runwaysdk.business.ClassSignature(hash = -1989510893)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to InactiveUserException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class InactiveUserExceptionBase extends com.runwaysdk.business.SmartException
{
  public final static String CLASS = "net.geoprism.account.InactiveUserException";
  public final static java.lang.String OID = "oid";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1989510893;
  
  public InactiveUserExceptionBase()
  {
    super();
  }
  
  public InactiveUserExceptionBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public InactiveUserExceptionBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public InactiveUserExceptionBase(java.lang.Throwable cause)
  {
    super(cause);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.account.InactiveUserException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{oid}", this.getOid());
    return message;
  }
  
}