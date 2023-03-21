package net.geoprism.account;

import java.util.Set;

import net.geoprism.GeoprismUser;

public interface AccountBusinessServiceIF
{
  /**
   * Applys the user, and then (if roleIds is not null) assigns all provided roles to the user.
   * All roles that the user has which are not part of the roleIds set will be removed.
   * 
   * @param user
   * @param roleIds
   */
  public void applyUserWithRoles(GeoprismUser user, Set<String> roleIds);
}
