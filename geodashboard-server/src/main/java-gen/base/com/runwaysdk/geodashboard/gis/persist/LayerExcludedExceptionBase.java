package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = -332136062)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to LayerExcludedException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class LayerExcludedExceptionBase extends com.runwaysdk.business.Information implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.LayerExcludedException";
  public static java.lang.String ID = "id";
  public static java.lang.String LAYERNAME = "layerName";
  private static final long serialVersionUID = -332136062;
  
  public LayerExcludedExceptionBase()
  {
    super();
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.LayerExcludedException.CLASS);
    return mdClassIF.definesAttribute(ID);
  }
  
  public String getLayerName()
  {
    return getValue(LAYERNAME);
  }
  
  public void validateLayerName()
  {
    this.validateAttribute(LAYERNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getLayerNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.LayerExcludedException.CLASS);
    return mdClassIF.definesAttribute(LAYERNAME);
  }
  
  public void setLayerName(String value)
  {
    if(value == null)
    {
      setValue(LAYERNAME, "");
    }
    else
    {
      setValue(LAYERNAME, value);
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
    message = replace(message, "{layerName}", this.getLayerName());
    return message;
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
