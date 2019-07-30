/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.dhis2;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class DHIS2IdMapping extends DHIS2IdMappingBase 
{
  private static final long serialVersionUID = 1512057276;
  
  public DHIS2IdMapping()
  {
    super();
  }
  
  public static DHIS2IdMapping getByRunwayId(String runwayId)
  {
    DHIS2IdMappingQuery q = new DHIS2IdMappingQuery(new QueryFactory());
    q.WHERE(q.getRunwayId().EQ(runwayId));
    OIterator<? extends DHIS2IdMapping> it = q.getIterator();
    
    try
    {
      if (it.hasNext())
      {
        return it.next();
      }
      else
      {
        String message = "Unable to find a [" + DHIS2IdMapping.CLASS + "] with a runwayId of [" + runwayId + "]";
  
        throw new DataNotFoundException(message, MdClassDAO.getMdClassDAO(DHIS2IdMapping.CLASS));
      }
    }
    finally
    {
      it.close();
    }
  }
  
  /**
   * MdMethod
   * 
   * @return JSON for displaying attributes pulled from DHIS2.
   */
  public static java.lang.String findAttributes()
  {
    return getDhis2Plugin().findAttributes();
  }
  
  /**
   * MdMethod
   * 
   * @return JSON for displaying programs pulled from DHIS2.
   */
  public static java.lang.String findPrograms()
  {
    return getDhis2Plugin().findPrograms();
  }
  
  /**
   * MdMethod
   * 
   * @return JSON for displaying Tracked entities pulled from DHIS2.
   */
  public static java.lang.String findTrackedEntityIds()
  {
    return getDhis2Plugin().findTrackedEntities();
  }
  
  public static DHIS2PluginIF getDhis2Plugin()
  {
    ServiceLoader<DHIS2PluginIF> loader = ServiceLoader.load(DHIS2PluginIF.class, Thread.currentThread().getContextClassLoader());

    try
    {
      Iterator<DHIS2PluginIF> it = loader.iterator();

      if (it.hasNext())
      {
        return it.next();
      }
      else
      {
        return null;
      }
    }
    catch (ServiceConfigurationError serviceError)
    {
      throw new ProgrammingErrorException(serviceError);
    }
  }
}
