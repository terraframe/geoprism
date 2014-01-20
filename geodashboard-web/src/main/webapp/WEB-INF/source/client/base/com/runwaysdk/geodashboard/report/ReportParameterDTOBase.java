package com.runwaysdk.geodashboard.report;

@com.runwaysdk.business.ClassSignature(hash = 302354804)
public abstract class ReportParameterDTOBase extends com.runwaysdk.business.ViewDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.report.ReportParameter";
  private static final long serialVersionUID = 302354804;
  
  protected ReportParameterDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String PARAMETERNAME = "parameterName";
  public static java.lang.String PARAMETERVALUE = "parameterValue";
  public String getParameterName()
  {
    return getValue(PARAMETERNAME);
  }
  
  public void setParameterName(String value)
  {
    if(value == null)
    {
      setValue(PARAMETERNAME, "");
    }
    else
    {
      setValue(PARAMETERNAME, value);
    }
  }
  
  public boolean isParameterNameWritable()
  {
    return isWritable(PARAMETERNAME);
  }
  
  public boolean isParameterNameReadable()
  {
    return isReadable(PARAMETERNAME);
  }
  
  public boolean isParameterNameModified()
  {
    return isModified(PARAMETERNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getParameterNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(PARAMETERNAME).getAttributeMdDTO();
  }
  
  public String getParameterValue()
  {
    return getValue(PARAMETERVALUE);
  }
  
  public void setParameterValue(String value)
  {
    if(value == null)
    {
      setValue(PARAMETERVALUE, "");
    }
    else
    {
      setValue(PARAMETERVALUE, value);
    }
  }
  
  public boolean isParameterValueWritable()
  {
    return isWritable(PARAMETERVALUE);
  }
  
  public boolean isParameterValueReadable()
  {
    return isReadable(PARAMETERVALUE);
  }
  
  public boolean isParameterValueModified()
  {
    return isModified(PARAMETERVALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getParameterValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(PARAMETERVALUE).getAttributeMdDTO();
  }
  
  public static ReportParameterDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(id);
    
    return (ReportParameterDTO) dto;
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
