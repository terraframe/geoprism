package net.geoprism.email;

public class InvalidEmailSettings extends InvalidEmailSettingsBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1218552261;
  
  public InvalidEmailSettings()
  {
    super();
  }
  
  public InvalidEmailSettings(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public InvalidEmailSettings(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public InvalidEmailSettings(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
