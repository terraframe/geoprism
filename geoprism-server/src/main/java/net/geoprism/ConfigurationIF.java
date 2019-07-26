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

import java.io.InputStream;
import java.util.Collection;
import java.util.Set;

import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.system.gis.geo.GeoEntity;

/**
 * Interface for dependency injection for configuring the system
 * 
 * @author terraframe
 *
 */
public interface ConfigurationIF 
{

  /**
   * Returns the fully qualified package for types to include in the databrowser
   * 
   * @return
   */
  public Collection<String> getDatabrowserPackages();

  /**
   * Returns the fully qualified types to include in the databrowser
   * 
   * @return
   */
  public Collection<String> getDatabrowserTypes();

  /**
   * Filter the user roles. This method is called when a user is applied. It can be used to remove or add roles on roles
   * assignment.
   * 
   * @param roleIds
   */
  public void configureUserRoles(Set<String> roleIds);

  /**
   * Configures the permissions given to a MdClass on generation
   * 
   * @param mdClass
   */
  public void configurePermissions(MdClassDAOIF mdClass);

  /**
   * Check is the user has access to some system functionality
   * 
   * @param functionality
   * @return
   */
  public boolean hasAccess(String functionality);

  public GeoEntity getDefaultGeoEntity();

  /**
   * Returns if the configuration supports the location query
   * 
   * @param type
   * @return
   */
  public boolean hasLocationData(String type);

  /**
   * Returns if the location data corresponding to the query type
   * 
   * @param type
   * @param object 
   * 
   * @return
   */
  public InputStream getLocationData(String type, JSONObject object);

  /**
   * Extension point for deleting geo entities
   * 
   * @param entity
   */
  public void onEntityDelete(GeoEntity entity);
  
  /**
   * Extension point for deleting datasets
   * 
   * @param mClass
   */
  public void onMappableClassDelete(MappableClass mClass);

  /**
   * Extension point which returns the geo entity realtionship which corresponds to the universal relationship
   * 
   * @param mdRelationshipDAOIF
   * @return
   */
  public String getGeoEntityRelationship(MdRelationshipDAOIF mdRelationshipDAOIF);
}
