package net.geoprism.account;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.GeoprismUser;
import net.geoprism.rbac.RoleBusinessService;
import net.geoprism.rbac.RoleBusinessServiceIF;

@Component
public class AccountBusinessService implements AccountBusinessServiceIF
{
  // TODO : Do not autowire components here. This service is directly instantiated in IDM
  
  public RoleBusinessServiceIF roleService = new RoleBusinessService();
  
  @Override
  @Transaction
  public void applyUserWithRoles(GeoprismUser user, Set<String> roleIds)
  {
    user.apply();

    roleService.assignRoles(user, roleIds);
  }
}
