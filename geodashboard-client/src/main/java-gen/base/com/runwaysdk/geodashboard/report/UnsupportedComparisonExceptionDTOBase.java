package com.runwaysdk.geodashboard.report;

@com.runwaysdk.business.ClassSignature(hash = -1129100353)
public abstract class UnsupportedComparisonExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.report.UnsupportedComparisonException";
  private static final long serialVersionUID = -1129100353;
  
  public UnsupportedComparisonExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected UnsupportedComparisonExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public UnsupportedComparisonExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public UnsupportedComparisonExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public UnsupportedComparisonExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public UnsupportedComparisonExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public UnsupportedComparisonExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public UnsupportedComparisonExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String COMPARISON = "comparison";
  public static java.lang.String ID = "id";
  public String getComparison()
  {
    return getValue(COMPARISON);
  }
  
  public void setComparison(String value)
  {
    if(value == null)
    {
      setValue(COMPARISON, "");
    }
    else
    {
      setValue(COMPARISON, value);
    }
  }
  
  public boolean isComparisonWritable()
  {
    return isWritable(COMPARISON);
  }
  
  public boolean isComparisonReadable()
  {
    return isReadable(COMPARISON);
  }
  
  public boolean isComparisonModified()
  {
    return isModified(COMPARISON);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getComparisonMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(COMPARISON).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{comparison}", this.getComparison().toString());
    template = template.replace("{id}", this.getId().toString());
    
    return template;
  }
  
}
