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
package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.view.TypeClass;
import net.geoprism.registry.view.TypeInfo;

public class ConceptClass extends ConceptClassBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -694947919;

  public ConceptClass()
  {
    super();
  }

  @Override
  public void apply()
  {
    super.apply();
  }

  @Override
  public void delete()
  {
    super.delete();
  }

  @Override
  public TypeInfo getTypeInfo()
  {
    return new TypeInfo(TypeClass.CONCEPT_CLASS, this.getCode());
  }

  @Override
  public GraphOrganization getOrganization()
  {
    return GraphOrganization.get((String) this.getObjectValue(BusinessEdgeType.ORGANIZATION));
  }

  public MdVertexDAOIF getMdVertexDAO()
  {
    return MdVertexDAO.get(this.getMdVertexOid());
  }

  public LocalizedValue getLabel()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(BusinessType.DISPLAYLABEL));
  }

  public String getOrganizationGraphId()
  {
    return this.getObjectValue(ORGANIZATION);
  }

  public ServerOrganization getServerOrganization()
  {
    return ServerOrganization.getByGraphId(this.getOrganizationGraphId());
  }

}
