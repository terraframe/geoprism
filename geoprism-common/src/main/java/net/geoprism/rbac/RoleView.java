package net.geoprism.rbac;

import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;

import com.runwaysdk.session.Session;
import com.runwaysdk.system.Roles;

public class RoleView implements Comparable<RoleView>
{
  @NotEmpty
  public Boolean assigned;
  
  @NotEmpty
  public String roleName;
  
  @NotEmpty
  public String displayLabel;
  
  @NotEmpty
  public String oid;

  public RoleView(String roleName, String displayLabel, String oid, Boolean assigned)
  {
    super();
    this.roleName = roleName;
    this.displayLabel = displayLabel;
    this.oid = oid;
    this.assigned = assigned;
  }
  
  public RoleView(Roles role, Boolean assigned)
  {
    super();
    this.roleName = role.getRoleName();
    this.displayLabel = role.getDisplayLabel().getValue();
    this.oid = role.getOid();
    this.assigned = assigned;
  }
  
  public Boolean getAssigned()
  {
    return assigned;
  }

  public void setAssigned(Boolean assigned)
  {
    this.assigned = assigned;
  }

  public String getRoleName()
  {
    return roleName;
  }

  public void setRoleName(String roleName)
  {
    this.roleName = roleName;
  }

  public String getDisplayLabel()
  {
    return displayLabel;
  }

  public void setDisplayLabel(String displayLabel)
  {
    this.displayLabel = displayLabel;
  }

  public String getOid()
  {
    return oid;
  }

  public void setOid(String oid)
  {
    this.oid = oid;
  }
  
  @Override
  public int hashCode()
  {
    return Objects.hash(oid);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RoleView other = (RoleView) obj;
    return Objects.equals(oid, other.oid);
  }

  @Override
  public int compareTo(RoleView o)
  {
    return this.getRoleName().compareTo(o.getRoleName());
  }
}
