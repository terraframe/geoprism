package net.geoprism;

import net.geoprism.configuration.GeoprismConfigGroup;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.generation.loader.Reloadable;

public class GeoprismProperties
{
  private ConfigurationReaderIF            props;
  
  private static class Singleton implements Reloadable
  {
    private static GeoprismProperties INSTANCE = new GeoprismProperties();
  }
  
  public GeoprismProperties()
  {
    this.props = ConfigurationManager.getReader(GeoprismConfigGroup.COMMON, "geoprism.properties");
  }
  
  public static String getEmailFrom()
  {
    return Singleton.INSTANCE.props.getString("email.from");
  }
  
  public static String getEmailTo()
  {
    return Singleton.INSTANCE.props.getString("email.to");
  }
  
  public static String getEmailUsername()
  {
    return Singleton.INSTANCE.props.getString("email.username");
  }
  
  public static String getEmailPassword()
  {
    return Singleton.INSTANCE.props.getString("email.password");
  }
  
  public static String getEmailServer()
  {
    return Singleton.INSTANCE.props.getString("email.server");
  }
  
  public static Integer getEmailPort()
  {
    return Singleton.INSTANCE.props.getInteger("email.port");
  }
  
  public static Integer getForgotPasswordExpireTime()
  {
    return Singleton.INSTANCE.props.getInteger("forgotPassword.expireTime");
  }
  
  public static Boolean getRequireStartTLS()
  {
    return Singleton.INSTANCE.props.getBoolean("email.requireStartTLS");
  }
}
