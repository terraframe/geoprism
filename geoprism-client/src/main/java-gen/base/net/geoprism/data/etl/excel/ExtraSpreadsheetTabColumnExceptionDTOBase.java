package net.geoprism.data.etl.excel;

@com.runwaysdk.business.ClassSignature(hash = 680051183)
public abstract class ExtraSpreadsheetTabColumnExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.data.etl.excel.ExtraSpreadsheetTabColumnException";
  private static final long serialVersionUID = 680051183;
  
  public ExtraSpreadsheetTabColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected ExtraSpreadsheetTabColumnExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public ExtraSpreadsheetTabColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public ExtraSpreadsheetTabColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public ExtraSpreadsheetTabColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public ExtraSpreadsheetTabColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public ExtraSpreadsheetTabColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public ExtraSpreadsheetTabColumnExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String COLUMNLABEL = "columnLabel";
  public static java.lang.String ID = "id";
  public static java.lang.String TABLABEL = "tabLabel";
  public String getColumnLabel()
  {
    return getValue(COLUMNLABEL);
  }
  
  public void setColumnLabel(String value)
  {
    if(value == null)
    {
      setValue(COLUMNLABEL, "");
    }
    else
    {
      setValue(COLUMNLABEL, value);
    }
  }
  
  public boolean isColumnLabelWritable()
  {
    return isWritable(COLUMNLABEL);
  }
  
  public boolean isColumnLabelReadable()
  {
    return isReadable(COLUMNLABEL);
  }
  
  public boolean isColumnLabelModified()
  {
    return isModified(COLUMNLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getColumnLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(COLUMNLABEL).getAttributeMdDTO();
  }
  
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
    
    template = template.replace("{columnLabel}", this.getColumnLabel().toString());
    template = template.replace("{id}", this.getId().toString());
    template = template.replace("{tabLabel}", this.getTabLabel().toString());
    
    return template;
  }
  
}
