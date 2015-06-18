package com.runwaysdk.geodashboard;

import java.lang.reflect.Constructor;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
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

  @SuppressWarnings("unchecked")
  public static GeneratedComponentQuery getQuery(MdClassDAOIF mdClass, ValueQuery vQuery)
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
