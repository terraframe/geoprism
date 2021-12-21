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
package net.geoprism;

import java.lang.reflect.Constructor;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdTableDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.GenericBusinessQuery;
import com.runwaysdk.query.GenericTableQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class QueryUtil 
{
  @SuppressWarnings("unchecked")
  public static GeneratedComponentQuery getQuery(MdClassDAOIF mdClass, QueryFactory factory)
  {
    // Use reflection to generate the view query
    if (mdClass.isGenerateSource())
    {
      String queryClassName = mdClass.definesType() + "Query";

      try
      {
        Class<GeneratedComponentQuery> clazz = (Class<GeneratedComponentQuery>) LoaderDecorator.loadClass(queryClassName);
        Constructor<GeneratedComponentQuery> constructor = clazz.getConstructor(factory.getClass());
        GeneratedComponentQuery query = constructor.newInstance(factory);

        return query;
      }
      catch (Exception e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
    else if (mdClass instanceof MdTableDAOIF)
    {
      return new GenericTableQuery((MdTableDAOIF) mdClass, factory);
    }    
    else if (mdClass instanceof MdBusinessDAOIF)
    {
      return new GenericBusinessQuery((MdBusinessDAOIF) mdClass, factory);
    }
    else
    {
      throw new ProgrammingErrorException("Unsupported generated query type [" + mdClass.definesType() + "]");
    }
  }

  @SuppressWarnings("unchecked")
  public static GeneratedComponentQuery getQuery(MdClassDAOIF mdClass, ValueQuery vQuery)
  {
    // Use reflection to generate the view query
    if (mdClass.isGenerateSource())
    {
      // Use reflection to generate the view query
      String queryClassName = mdClass.definesType() + "Query";

      try
      {
        Class<GeneratedComponentQuery> clazz = (Class<GeneratedComponentQuery>) LoaderDecorator.loadClass(queryClassName);
        Constructor<GeneratedComponentQuery> constructor = clazz.getConstructor(vQuery.getClass());
        GeneratedComponentQuery query = constructor.newInstance(vQuery);

        return query;
      }
      catch (Exception e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
    else if (mdClass instanceof MdTableDAOIF)
    {
      return new GenericTableQuery((MdTableDAOIF) mdClass, vQuery);
    }    
    else if (mdClass instanceof MdBusinessDAOIF)
    {
      return new GenericBusinessQuery((MdBusinessDAOIF) mdClass, vQuery);
    }
    else
    {
      throw new ProgrammingErrorException("Unsupported generated query type [" + mdClass.definesType() + "]");
    }
  }

  public static MdAttributeReferenceDAOIF getGeoEntityAttribute(MdClassDAOIF mdClass)
  {
    for (MdAttributeDAOIF mdAttr : mdClass.definesAttributes())
    {
      MdAttributeConcreteDAOIF mdAttributeConcrete = mdAttr.getMdAttributeConcrete();

      if (mdAttributeConcrete instanceof MdAttributeReferenceDAOIF)
      {
        MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttributeConcrete;

        if (mdAttributeReference.getReferenceMdBusinessDAO().definesType().equals(GeoEntity.CLASS))
        {
          return mdAttributeReference;
        }
      }
    }

    return null;
  }
}
