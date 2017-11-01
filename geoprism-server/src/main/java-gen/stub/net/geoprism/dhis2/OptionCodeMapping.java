package net.geoprism.dhis2;

public class OptionCodeMapping extends OptionCodeMappingBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -123292063;
  
  public OptionCodeMapping()
  {
    super();
  }
  
  @Override
  public String buildKey()
  {
    return this.getRunwayId();
  }
}
