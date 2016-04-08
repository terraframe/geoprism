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
package net.geoprism.data;

import java.io.File;
import java.util.List;

public interface XMLEndpoint
{
  /**
   * Returns a list of all universal endpoint file keys which exist for the given country and version
   * 
   * @param country
   * @param version
   * @return
   */
  public List<String> listUniversalKeys(String country, String version);

  /**
   * Returns a list of all geoentity endpoint file keys which exist for the given country and version
   * 
   * @param country
   * @param version
   * @return
   */
  public List<String> listGeoEntityKeys(String country, String version);

  /**
   * Copies all files into the given directory
   * 
   * @param directory
   *          The directory in which to copy the files
   * @param keys
   *          List of endpoint file keys used to retrieve the files from the endpoint
   */
  public void copyFiles(File directory, List<String> keys);

  /**
   * Copies all files into the a generated temp directory
   * 
   * @param keys
   *          List of endpoint file keys used to retrieve the files from the endpoint
   * @return Temp directory containing files
   */
  public File copyFiles(List<String> keys);
}
