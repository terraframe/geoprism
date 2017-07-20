package net.geoprism;

@com.runwaysdk.business.ClassSignature(hash = -1264570153)
public abstract class PluginUtilDTOBase extends com.runwaysdk.business.UtilDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.PluginUtil";
  private static final long serialVersionUID = -1264570153;
  
  protected PluginUtilDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static final java.lang.Boolean isDHIS2Enabled(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.PluginUtilDTO.CLASS, "isDHIS2Enabled", _declaredTypes);
    return (java.lang.Boolean) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static PluginUtilDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.UtilDTO dto = (com.runwaysdk.business.UtilDTO)clientRequest.get(id);
    
    return (PluginUtilDTO) dto;
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
    getRequest().delete(this.getId());
  }
  
}
