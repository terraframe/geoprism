package com.runwaysdk.geodashboard;

@com.runwaysdk.business.ClassSignature(hash = 2145410212)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to Dashboard.java
 *
 * @author Autogenerated by RunwaySDK
 */
public class DashboardQueryDTO extends com.runwaysdk.business.BusinessQueryDTO
 implements com.runwaysdk.generation.loader.Reloadable
{
private static final long serialVersionUID = 2145410212;

  protected DashboardQueryDTO(String type)
  {
    super(type);
  }

@SuppressWarnings("unchecked")
public java.util.List<? extends com.runwaysdk.geodashboard.DashboardDTO> getResultSet()
{
  return (java.util.List<? extends com.runwaysdk.geodashboard.DashboardDTO>)super.getResultSet();
}
}
