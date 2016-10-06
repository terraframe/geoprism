package net.geoprism;

import java.util.LinkedList;
import java.util.List;

import net.geoprism.localization.LocalizationFacadeDTO;

import com.runwaysdk.constants.ClientRequestIF;

public class DefaultClientConfiguration implements ClientConfigurationIF
{

  @Override
  public List<GeoprismApplication> getApplications(ClientRequestIF request)
  {
    /*
     * Default applications which geoprism defines
     */
    GeoprismApplication kaleidoscope = new GeoprismApplication();
    kaleidoscope.setId("kaleidoscope");
    kaleidoscope.setLabel(LocalizationFacadeDTO.getFromBundles(request, "geoprismLanding.dashboards"));
    kaleidoscope.setSrc("net/geoprism/images/dashboard_icon.png");
    kaleidoscope.setUrl("kaleidoscopes");

    GeoprismApplication management = new GeoprismApplication();
    management.setId("management");
    management.setLabel(LocalizationFacadeDTO.getFromBundles(request, "geoprismLanding.dataManagement"));
    management.setSrc("net/geoprism/images/admin_icon.png");
    management.setUrl("management");
    management.addRole(RoleConstants.ADIM_ROLE);
    management.addRole(RoleConstants.BUILDER_ROLE);

    List<GeoprismApplication> applications = new LinkedList<GeoprismApplication>();
    applications.add(kaleidoscope);
    applications.add(management);

    return applications;
  }

}
