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
package net.geoprism;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdClass;

public class MappableAttribute extends MappableAttributeBase 
{
  private static final long serialVersionUID = -333559735;

  public MappableAttribute()
  {
    super();
  }

  public static MappableAttribute getMappableAttribute(MdAttributeDAOIF mdAttribute)
  {
    return getMappableAttribute(mdAttribute.getOid());
  }

  public static MappableAttribute getMappableAttribute(String oid)
  {
    MappableAttributeQuery query = new MappableAttributeQuery(new QueryFactory());
    query.WHERE(query.getWrappedMdAttribute().EQ(oid));

    OIterator<? extends MappableAttribute> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        return it.next();
      }

      return null;
    }
    finally
    {
      it.close();
    }
  }

  public static List<MappableAttribute> getMappableAttributes(MdClass mdClass)
  {
    List<MappableAttribute> list = new LinkedList<MappableAttribute>();

    OIterator<? extends MdAttribute> mdAttributes = mdClass.getAllAttribute();

    try
    {
      while (mdAttributes.hasNext())
      {
        MdAttribute mdAttribute = mdAttributes.next();
        MappableAttribute mAttribute = MappableAttribute.getMappableAttribute(mdAttribute.getOid());

        if (mAttribute != null)
        {
          list.add(mAttribute);
        }
      }
    }
    finally
    {
      mdAttributes.close();
    }

    return list;
  }
}
