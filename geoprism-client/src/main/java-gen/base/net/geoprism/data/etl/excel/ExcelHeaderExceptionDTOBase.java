package net.geoprism.data.etl.excel;

@com.runwaysdk.business.ClassSignature(hash = 157246384)
public abstract class ExcelHeaderExceptionDTOBase extends com.runwaysdk.business.SmartExceptionDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.data.etl.excel.ExcelHeaderException";
  private static final long serialVersionUID = 157246384;
  
  public ExcelHeaderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected ExcelHeaderExceptionDTOBase(com.runwaysdk.business.ExceptionDTO exceptionDTO)
  {
    super(exceptionDTO);
  }
  
  public ExcelHeaderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale)
  {
    super(clientRequest, locale);
  }
  
  public ExcelHeaderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage)
  {
    super(clientRequest, locale, developerMessage);
  }
  
  public ExcelHeaderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.Throwable cause)
  {
    super(clientRequest, locale, cause);
  }
  
  public ExcelHeaderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.util.Locale locale, java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(clientRequest, locale, developerMessage, cause);
  }
  
  public ExcelHeaderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Throwable cause)
  {
    super(clientRequest, cause);
  }
  
  public ExcelHeaderExceptionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String msg, java.lang.Throwable cause)
  {
    super(clientRequest, msg, cause);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String COLUMN = "column";
  public static java.lang.String ID = "id";
  public static java.lang.String MSG = "msg";
  public static java.lang.String ROW = "row";
  public String getColumn()
  {
    return getValue(COLUMN);
  }
  
  public void setColumn(String value)
  {
    if(value == null)
    {
      setValue(COLUMN, "");
    }
    else
    {
      setValue(COLUMN, value);
    }
  }
  
  public boolean isColumnWritable()
  {
    return isWritable(COLUMN);
  }
  
  public boolean isColumnReadable()
  {
    return isReadable(COLUMN);
  }
  
  public boolean isColumnModified()
  {
    return isModified(COLUMN);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getColumnMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(COLUMN).getAttributeMdDTO();
  }
  
  public String getMsg()
  {
    return getValue(MSG);
  }
  
  public void setMsg(String value)
  {
    if(value == null)
    {
      setValue(MSG, "");
    }
    else
    {
      setValue(MSG, value);
    }
  }
  
  public boolean isMsgWritable()
  {
    return isWritable(MSG);
  }
  
  public boolean isMsgReadable()
  {
    return isReadable(MSG);
  }
  
  public boolean isMsgModified()
  {
    return isModified(MSG);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getMsgMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(MSG).getAttributeMdDTO();
  }
  
  public String getRow()
  {
    return getValue(ROW);
  }
  
  public void setRow(String value)
  {
    if(value == null)
    {
      setValue(ROW, "");
    }
    else
    {
      setValue(ROW, value);
    }
  }
  
  public boolean isRowWritable()
  {
    return isWritable(ROW);
  }
  
  public boolean isRowReadable()
  {
    return isReadable(ROW);
  }
  
  public boolean isRowModified()
  {
    return isModified(ROW);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getRowMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(ROW).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{column}", this.getColumn().toString());
    template = template.replace("{id}", this.getId().toString());
    template = template.replace("{msg}", this.getMsg().toString());
    template = template.replace("{row}", this.getRow().toString());
    
    return template;
  }
  
}
