package net.geoprism;

@com.runwaysdk.business.ClassSignature(hash = 435191861)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to NonUniqueDatasetException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class NonUniqueDatasetExceptionBase extends com.runwaysdk.business.SmartException implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.NonUniqueDatasetException";
  public static java.lang.String ID = "id";
  public static java.lang.String LABEL = "label";
  private static final long serialVersionUID = 435191861;
  
  public NonUniqueDatasetExceptionBase()
  {
    super();
  }
  
  public NonUniqueDatasetExceptionBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public NonUniqueDatasetExceptionBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public NonUniqueDatasetExceptionBase(java.lang.Throwable cause)
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
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.NonUniqueDatasetException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(ID);
  }
  
  public String getLabel()
  {
    return getValue(LABEL);
  }
  
  public void validateLabel()
  {
    this.validateAttribute(LABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.NonUniqueDatasetException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(LABEL);
  }
  
  public void setLabel(String value)
  {
    if(value == null)
    {
      setValue(LABEL, "");
    }
    else
    {
      setValue(LABEL, value);
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
    message = replace(message, "{label}", this.getLabel());
    return message;
  }
  
}