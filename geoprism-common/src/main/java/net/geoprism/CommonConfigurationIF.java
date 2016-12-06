package net.geoprism;

import java.util.List;

import com.runwaysdk.generation.loader.Reloadable;

import net.geoprism.account.OauthServerIF;

public interface CommonConfigurationIF extends Reloadable
{
  public List<OauthServerIF> getOauthServers();
}
