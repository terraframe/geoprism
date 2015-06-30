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
package com.runwaysdk.geodashboard.gis.locatedIn;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.system.metadata.MdClass;

public class QueryUtil implements Reloadable
{
  public static String getIdColumn()
  {
    return getColumnName(MdClass.getIdMd());
  }

  /**
   * Returns the column name of the given attribute.
   * 
   * @param md
   * @param attribute
   * @return
   */
  public static String getColumnName(MdEntityDAOIF md, String attribute)
  {
    if (attribute.equals("childId"))
    {
      return RelationshipDAOIF.CHILD_ID_COLUMN;
    }
    else if (attribute.equals("parentId"))
    {
      return RelationshipDAOIF.PARENT_ID_COLUMN;
    }
    else
    {
      return getColumnName(md.getAllDefinedMdAttributeMap().get(attribute.toLowerCase()));
    }
  }

  /**
   * Returns the column name of the given MdAttribute.
   * 
   * @param md
   * @return
   */
  public static String getColumnName(MdAttributeDAOIF md)
  {
    if (md instanceof MdAttributeVirtualDAOIF)
    {
      return ( (MdAttributeVirtualDAO) md ).getMdAttributeConcrete().getColumnName();
    }
    else
    {
      return ( (MdAttributeConcreteDAOIF) md ).getColumnName();
    }
  }
}
