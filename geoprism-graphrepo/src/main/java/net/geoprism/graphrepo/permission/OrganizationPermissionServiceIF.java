package net.geoprism.graphrepo.permission;

import com.runwaysdk.business.rbac.SingleActorDAOIF;

public interface OrganizationPermissionServiceIF
{
  public void enforceActorCanCreate();

  public void enforceActorCanUpdate();

  public boolean canActorCreate();

  public boolean canActorUpdate();

  public void enforceActorCanDelete();

  public boolean canActorDelete();

  public boolean canActorRead(String orgCode);

  public void enforceActorCanRead(SingleActorDAOIF actor, String orgCode);
}
