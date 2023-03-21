/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
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
