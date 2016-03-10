/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.data.etl;

import com.runwaysdk.system.metadata.MdAttribute;

public class TargetFieldBasicBinding extends TargetFieldBasicBindingBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -15027979;

  public TargetFieldBasicBinding()
  {
    super();
  }

  protected void populate(TargetField field)
  {
    super.populate(field);

    MdAttribute sourceAttribute = this.getSourceAttribute();

    TargetFieldBasic basic = (TargetFieldBasic) field;
    basic.setSourceAttributeName(sourceAttribute.getAttributeName());
  }

  @Override
  public TargetFieldIF getTargetField()
  {
    TargetFieldBasic field = new TargetFieldBasic();

    this.populate(field);

    return field;
  }
}
