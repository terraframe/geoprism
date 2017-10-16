package net.geoprism.dashboard;

@com.runwaysdk.business.ClassSignature(hash = -941506097)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DashboardLegend.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class DashboardLegendBase extends com.runwaysdk.business.Struct implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.dashboard.DashboardLegend";
  public static java.lang.String GROUPEDINLEGEND = "groupedInLegend";
  public static java.lang.String ID = "id";
  public static java.lang.String IMAGESNAPSHOT = "imageSnapshot";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String LEGENDXPOSITION = "legendXPosition";
  public static java.lang.String LEGENDYPOSITION = "legendYPosition";
  public static java.lang.String SITEMASTER = "siteMaster";
  private static final long serialVersionUID = -941506097;
  
  public DashboardLegendBase()
  {
    super();
  }
  
  public DashboardLegendBase(com.runwaysdk.business.MutableWithStructs component, String structName)
  {
    super(component, structName);
  }
  
  public static DashboardLegend get(String id)
  {
    return (DashboardLegend) com.runwaysdk.business.Struct.get(id);
  }
  
  public static DashboardLegend getByKey(String key)
  {
    return (DashboardLegend) com.runwaysdk.business.Struct.get(CLASS, key);
  }
  
  public Boolean getGroupedInLegend()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(GROUPEDINLEGEND));
  }
  
  public void validateGroupedInLegend()
  {
    this.validateAttribute(GROUPEDINLEGEND);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getGroupedInLegendMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardLegend.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(GROUPEDINLEGEND);
  }
  
  public void setGroupedInLegend(Boolean value)
  {
    if(value == null)
    {
      setValue(GROUPEDINLEGEND, "");
    }
    else
    {
      setValue(GROUPEDINLEGEND, java.lang.Boolean.toString(value));
    }
  }
  
  public String getId()
  {
    return getValue(ID);
  }
  
  public void validateId()
  {
    this.validateAttribute(ID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardLegend.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(ID);
  }
  
  public byte[] getImageSnapshot()
  {
    return getBlob(IMAGESNAPSHOT);
  }
  
  public void validateImageSnapshot()
  {
    this.validateAttribute(IMAGESNAPSHOT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBlobDAOIF getImageSnapshotMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardLegend.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBlobDAOIF)mdClassIF.definesAttribute(IMAGESNAPSHOT);
  }
  
  public void setImageSnapshot(byte[] value)
  {
    if(value == null)
    {
      setValue(IMAGESNAPSHOT, "");
    }
    else
    {
      setBlob(IMAGESNAPSHOT, value);
    }
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
  }
  
  public void validateKeyName()
  {
    this.validateAttribute(KEYNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getKeyNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardLegend.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(KEYNAME);
  }
  
  public void setKeyName(String value)
  {
    if(value == null)
    {
      setValue(KEYNAME, "");
    }
    else
    {
      setValue(KEYNAME, value);
    }
  }
  
  public Integer getLegendXPosition()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(LEGENDXPOSITION));
  }
  
  public void validateLegendXPosition()
  {
    this.validateAttribute(LEGENDXPOSITION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getLegendXPositionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardLegend.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(LEGENDXPOSITION);
  }
  
  public void setLegendXPosition(Integer value)
  {
    if(value == null)
    {
      setValue(LEGENDXPOSITION, "");
    }
    else
    {
      setValue(LEGENDXPOSITION, java.lang.Integer.toString(value));
    }
  }
  
  public Integer getLegendYPosition()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(LEGENDYPOSITION));
  }
  
  public void validateLegendYPosition()
  {
    this.validateAttribute(LEGENDYPOSITION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getLegendYPositionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardLegend.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(LEGENDYPOSITION);
  }
  
  public void setLegendYPosition(Integer value)
  {
    if(value == null)
    {
      setValue(LEGENDYPOSITION, "");
    }
    else
    {
      setValue(LEGENDYPOSITION, java.lang.Integer.toString(value));
    }
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public void validateSiteMaster()
  {
    this.validateAttribute(SITEMASTER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getSiteMasterMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardLegend.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(SITEMASTER);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static DashboardLegendQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    DashboardLegendQuery query = new DashboardLegendQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}
