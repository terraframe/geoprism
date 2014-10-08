package com.runwaysdk.geodashboard.report;

@com.runwaysdk.business.ClassSignature(hash = -465618707)
public abstract class ReportQueryViewDTOBase extends com.runwaysdk.business.ViewDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.report.ReportQueryView";
  private static final long serialVersionUID = -465618707;
  
  protected ReportQueryViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String MAXDEPTH = "maxDepth";
  public static java.lang.String QUERYID = "queryId";
  public static java.lang.String QUERYLABEL = "queryLabel";
  public Integer getMaxDepth()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(MAXDEPTH));
  }
  
  public void setMaxDepth(Integer value)
  {
    if(value == null)
    {
      setValue(MAXDEPTH, "");
    }
    else
    {
      setValue(MAXDEPTH, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isMaxDepthWritable()
  {
    return isWritable(MAXDEPTH);
  }
  
  public boolean isMaxDepthReadable()
  {
    return isReadable(MAXDEPTH);
  }
  
  public boolean isMaxDepthModified()
  {
    return isModified(MAXDEPTH);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getMaxDepthMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(MAXDEPTH).getAttributeMdDTO();
  }
  
  public String getQueryId()
  {
    return getValue(QUERYID);
  }
  
  public void setQueryId(String value)
  {
    if(value == null)
    {
      setValue(QUERYID, "");
    }
    else
    {
      setValue(QUERYID, value);
    }
  }
  
  public boolean isQueryIdWritable()
  {
    return isWritable(QUERYID);
  }
  
  public boolean isQueryIdReadable()
  {
    return isReadable(QUERYID);
  }
  
  public boolean isQueryIdModified()
  {
    return isModified(QUERYID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getQueryIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(QUERYID).getAttributeMdDTO();
  }
  
  public String getQueryLabel()
  {
    return getValue(QUERYLABEL);
  }
  
  public void setQueryLabel(String value)
  {
    if(value == null)
    {
      setValue(QUERYLABEL, "");
    }
    else
    {
      setValue(QUERYLABEL, value);
    }
  }
  
  public boolean isQueryLabelWritable()
  {
    return isWritable(QUERYLABEL);
  }
  
  public boolean isQueryLabelReadable()
  {
    return isReadable(QUERYLABEL);
  }
  
  public boolean isQueryLabelModified()
  {
    return isModified(QUERYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getQueryLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(QUERYLABEL).getAttributeMdDTO();
  }
  
  public static ReportQueryViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(id);
    
    return (ReportQueryViewDTO) dto;
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
