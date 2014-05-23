package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = -1266323454)
public abstract class LayerExcludedExceptionDTOBase extends com.runwaysdk.business.InformationDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.LayerExcludedException";
  private static final long serialVersionUID = -1266323454;
  
  public LayerExcludedExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String LAYERNAME = "layerName";
  public String getLayerName()
  {
    return getValue(LAYERNAME);
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
  
  public boolean isLayerNameWritable()
  {
    return isWritable(LAYERNAME);
  }
  
  public boolean isLayerNameReadable()
  {
    return isReadable(LAYERNAME);
  }
  
  public boolean isLayerNameModified()
  {
    return isModified(LAYERNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getLayerNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(LAYERNAME).getAttributeMdDTO();
  }
  
}
