package net.geoprism.account;

@com.runwaysdk.business.ClassSignature(hash = -1283098261)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ExternalProfile.java
 *
 * @author Autogenerated by RunwaySDK
 */
public class ExternalProfileQueryDTO extends com.runwaysdk.system.SingleActorQueryDTO
 implements com.runwaysdk.generation.loader.Reloadable
{
private static final long serialVersionUID = -1283098261;

  protected ExternalProfileQueryDTO(String type)
  {
    super(type);
  }

@SuppressWarnings("unchecked")
public java.util.List<? extends net.geoprism.account.ExternalProfileDTO> getResultSet()
{
  return (java.util.List<? extends net.geoprism.account.ExternalProfileDTO>)super.getResultSet();
}
}
