/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data.browser;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.Entity;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

import net.geoprism.ConfigurationIF;
import net.geoprism.ConfigurationService;

public class DataBrowserUtil extends DataBrowserUtilBase 
{
  private static final long serialVersionUID = 884335891;

  public DataBrowserUtil()
  {
    super();
  }

  public static MetadataTypeQuery getDefaultTypes()
  {
    List<String> packages = new LinkedList<String>();
    List<String> types = new LinkedList<String>();

    List<ConfigurationIF> configurations = ConfigurationService.getConfigurations();

    for (ConfigurationIF configuration : configurations)
    {
      packages.addAll(configuration.getDatabrowserPackages());
      types.addAll(configuration.getDatabrowserTypes());
    }

    QueryFactory f = new QueryFactory();

    MetadataTypeQuery query = new MetadataTypeQuery(f, packages.toArray(new String[packages.size()]), types.toArray(new String[types.size()]));

    return query;
  }

  @Transaction
  public static void deleteData(String type)
  {
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(type);

    if (mdClass instanceof MdEntityDAOIF)
    {
      QueryFactory factory = new QueryFactory();
      EntityQuery query = (EntityQuery) factory.componentQueryEntity((MdEntityDAOIF) mdClass);
      OIterator<? extends ComponentIF> iterator = query.getIterator();

      try
      {
        while (iterator.hasNext())
        {
          Entity entity = (Entity) iterator.next();
          entity.delete();
        }
      }
      finally
      {
        iterator.close();
      }
    }
  }

  public static MetadataTypeQuery getTypes(String[] packages, String[] types)
  {
    QueryFactory f = new QueryFactory();

    MetadataTypeQuery query = new MetadataTypeQuery(f, packages, types);

    return query;
  }
}
