package com.runwaysdk.geodashboard.gis;

@com.runwaysdk.business.ClassSignature(hash = 543952122)
public abstract class EmptyLayerInformationDTOBase extends com.runwaysdk.business.InformationDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.EmptyLayerInformation";
  private static final long serialVersionUID = 543952122;
  
  public EmptyLayerInformationDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
}
