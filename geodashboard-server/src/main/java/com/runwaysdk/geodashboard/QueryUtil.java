package com.runwaysdk.geodashboard;

import java.lang.reflect.Constructor;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.GeneratedEntityQuery;
import com.runwaysdk.query.GeneratedViewQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class QueryUtil implements Reloadable
{

  @SuppressWarnings("unchecked")
  public static GeneratedComponentQuery getQuery(MdClassDAOIF mdClass, QueryFactory factory)
  {
    // Use reflection to generate the view query
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

  public static Attribute get(GeneratedComponentQuery query, String attributeName)
  {
    if (query instanceof GeneratedViewQuery)
    {
      return ( (GeneratedViewQuery) query ).get(attributeName);
    }

    return ( (GeneratedEntityQuery) query ).get(attributeName);
  }

  public static MdAttributeDAOIF getGeoEntityAttribute(MdClassDAOIF mdClass)
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
