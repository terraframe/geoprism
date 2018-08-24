package net.geoprism.data.etl.excel;

@com.runwaysdk.business.ClassSignature(hash = -2026153371)
public abstract class MissingSpreadsheetTabExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.data.etl.excel.MissingSpreadsheetTabException";
  private static final long serialVersionUID = -2026153371;
  
  public MissingSpreadsheetTabExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected MissingSpreadsheetTabExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public MissingSpreadsheetTabExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public MissingSpreadsheetTabExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public MissingSpreadsheetTabExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public MissingSpreadsheetTabExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public MissingSpreadsheetTabExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public MissingSpreadsheetTabExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String TABLABEL = "tabLabel";
  public String getTabLabel()
  {
    return getValue(TABLABEL);
  }
  
  public void setTabLabel(String value)
  {
    if(value == null)
    {
      setValue(TABLABEL, "");
    }
    else
    {
      setValue(TABLABEL, value);
    }
  }
  
  public boolean isTabLabelWritable()
  {
    return isWritable(TABLABEL);
  }
  
  public boolean isTabLabelReadable()
  {
    return isReadable(TABLABEL);
  }
  
  public boolean isTabLabelModified()
  {
    return isModified(TABLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getTabLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(TABLABEL).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{id}", this.getId().toString());
    template = template.replace("{tabLabel}", this.getTabLabel().toString());
    
    return template;
  }
  
}
