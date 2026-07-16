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
package net.geoprism.graph;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.view.ConceptClassDTO;

public class ConceptClassSnapshot extends ConceptClassSnapshotBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -130238130;

  public ConceptClassSnapshot()
  {
    super();
  }

  @Override
  public String getKey()
  {
    return super.getCode();
  }

  @Override
  public String toString()
  {
    return this.getCode();
  }

  public ConceptClassDTO toDTO()
  {
    ConceptClassDTO typeObject = new ConceptClassDTO();
    typeObject.setCode(this.getCode());
    typeObject.setOrganization(this.getOrgCode());
    typeObject.setOrigin(this.getOrigin());
    typeObject.setSequence(this.getSequence());
    typeObject.setDisplayLabel(LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()));
    typeObject.setAttributes(this.getAttributeTypes());

    return typeObject;
  }

}
