package com.test.geodashboard;

@com.runwaysdk.business.ClassSignature(hash = -2039053717)
public abstract class StateInfoViewDTOBase extends com.runwaysdk.business.ViewDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.test.geodashboard.StateInfoView";
  private static final long serialVersionUID = -2039053717;
  
  protected StateInfoViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String VIEWRANK = "viewRank";
  public Integer getViewRank()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(VIEWRANK));
  }
  
  public void setViewRank(Integer value)
  {
    if(value == null)
    {
      setValue(VIEWRANK, "");
    }
    else
    {
      setValue(VIEWRANK, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isViewRankWritable()
  {
    return isWritable(VIEWRANK);
  }
  
  public boolean isViewRankReadable()
  {
    return isReadable(VIEWRANK);
  }
  
  public boolean isViewRankModified()
  {
    return isModified(VIEWRANK);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getViewRankMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(VIEWRANK).getAttributeMdDTO();
  }
  
  public static StateInfoViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(id);
    
    return (StateInfoViewDTO) dto;
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
