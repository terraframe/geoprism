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

import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.gis.constants.MdGeoVertexInfo;
import com.runwaysdk.system.gis.geo.Universal;

import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.model.ServerGeoObjectType;

public class GeoVertexType extends GeoVertexTypeBase
{
  private static final long serialVersionUID = 224165207;

  public GeoVertexType()
  {
    super();
  }

  /**
   * 
   * 
   * @param code
   * @param ownerActorId
   *          = the ID of the {@link ActorDAOIF} that is the owner of the
   *          {@link MdVertexDAOIF}.
   * @param isAbstract
   *          TODO
   * @param parentType
   *          TODO
   * @return
   */
  public static MdVertexDAO create(String code, Boolean isAbstract, ServerGeoObjectType parentType)
  {
    MdVertexDAOIF parentVertexDAO = null;

    if (parentType == null)
    {
      parentVertexDAO = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS);
    }
    else
    {
      parentVertexDAO = parentType.getMdVertex();
    }

    MdVertexDAO child = MdVertexDAO.newInstance();
    child.setValue(MdGeoVertexInfo.PACKAGE, RegistryConstants.UNIVERSAL_GRAPH_PACKAGE);
    child.setValue(MdGeoVertexInfo.NAME, code);
    child.setValue(MdGeoVertexInfo.SUPER_MD_VERTEX, parentVertexDAO.getOid());
    child.setValue(MdGeoVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
    child.setValue(MdGeoVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    child.setValue(MdGeoVertexInfo.ABSTRACT, isAbstract.toString());
    child.apply();

    return child;
  }

  public static void remove(String code)
  {
    MdVertexDAO mdGeoVertex = getMdGeoVertex(code);
    mdGeoVertex.delete();
  }

  public static MdVertexDAO getMdGeoVertex(String code)
  {
    if (!code.equals(Universal.ROOT))
    {
      return MdVertexDAO.getMdVertexDAO(buildMdGeoVertexKey(code)).getBusinessDAO();
    }

    return null;
  }

  public static String buildMdGeoVertexKey(String mdGeoVertexCode)
  {
    return RegistryConstants.UNIVERSAL_GRAPH_PACKAGE + "." + mdGeoVertexCode;
  }
}
