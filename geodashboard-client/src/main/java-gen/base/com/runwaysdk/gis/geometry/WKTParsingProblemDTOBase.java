package com.runwaysdk.gis.geometry;

@com.runwaysdk.business.ClassSignature(hash = -726743692)
public abstract class WKTParsingProblemDTOBase extends com.runwaysdk.business.ProblemDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.gis.geometry.WKTParsingProblem";
  private static final long serialVersionUID = -726743692;
  
  public WKTParsingProblemDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  public WKTParsingProblemDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF, java.util.Locale locale)
  {
    super(clientRequestIF, locale);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static java.lang.String REASON = "reason";
  public String getReason()
  {
    return getValue(REASON);
  }
  
  public void setReason(String value)
  {
    if(value == null)
    {
      setValue(REASON, "");
    }
    else
    {
      setValue(REASON, value);
    }
  }
  
  public boolean isReasonWritable()
  {
    return isWritable(REASON);
  }
  
  public boolean isReasonReadable()
  {
    return isReadable(REASON);
  }
  
  public boolean isReasonModified()
  {
    return isModified(REASON);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getReasonMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(REASON).getAttributeMdDTO();
  }
  
  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    java.lang.String template = super.getMessage();
    
    template = template.replace("{id}", this.getId().toString());
    template = template.replace("{reason}", this.getReason().toString());
    
    return template;
  }
  
}
