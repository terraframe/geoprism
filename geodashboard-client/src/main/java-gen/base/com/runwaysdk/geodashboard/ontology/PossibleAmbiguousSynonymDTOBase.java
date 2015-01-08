package com.runwaysdk.geodashboard.ontology;

@com.runwaysdk.business.ClassSignature(hash = -196790562)
public abstract class PossibleAmbiguousSynonymDTOBase extends com.runwaysdk.business.WarningDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.ontology.PossibleAmbiguousSynonym";
  private static final long serialVersionUID = -196790562;
  
  public PossibleAmbiguousSynonymDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequestIF)
  {
    super(clientRequestIF);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CLASSIFIERLABEL = "classifierLabel";
  public static java.lang.String ID = "id";
  public static java.lang.String SYNONYMLABEL = "synonymLabel";
  public String getClassifierLabel()
  {
    return getValue(CLASSIFIERLABEL);
  }
  
  public void setClassifierLabel(String value)
  {
    if(value == null)
    {
      setValue(CLASSIFIERLABEL, "");
    }
    else
    {
      setValue(CLASSIFIERLABEL, value);
    }
  }
  
  public boolean isClassifierLabelWritable()
  {
    return isWritable(CLASSIFIERLABEL);
  }
  
  public boolean isClassifierLabelReadable()
  {
    return isReadable(CLASSIFIERLABEL);
  }
  
  public boolean isClassifierLabelModified()
  {
    return isModified(CLASSIFIERLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getClassifierLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CLASSIFIERLABEL).getAttributeMdDTO();
  }
  
  public String getSynonymLabel()
  {
    return getValue(SYNONYMLABEL);
  }
  
  public void setSynonymLabel(String value)
  {
    if(value == null)
    {
      setValue(SYNONYMLABEL, "");
    }
    else
    {
      setValue(SYNONYMLABEL, value);
    }
  }
  
  public boolean isSynonymLabelWritable()
  {
    return isWritable(SYNONYMLABEL);
  }
  
  public boolean isSynonymLabelReadable()
  {
    return isReadable(SYNONYMLABEL);
  }
  
  public boolean isSynonymLabelModified()
  {
    return isModified(SYNONYMLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSynonymLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SYNONYMLABEL).getAttributeMdDTO();
  }
  
}
