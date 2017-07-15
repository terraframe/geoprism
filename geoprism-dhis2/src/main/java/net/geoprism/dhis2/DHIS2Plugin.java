package net.geoprism.dhis2;

import net.geoprism.dhis2.util.DHIS2IdFinder;

public class DHIS2Plugin implements DHIS2PluginIF
{

  @Override
  public String findAttributes()
  {
    return DHIS2IdFinder.findAttributes();
  }

  @Override
  public String findPrograms()
  {
    return DHIS2IdFinder.findPrograms();
  }

  @Override
  public String findTrackedEntities()
  {
    return DHIS2IdFinder.findTrackedEntities();
  }

}
