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
package net.geoprism.gis.geoserver;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.runwaysdk.constants.VaultProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

import net.geoprism.data.DatabaseUtil;
import net.geoprism.util.Iterables;

public class CleanupFacade 
{
  @Request
  public static void cleanupUnusedLayers()
  {
    cleanupUnusedLayers_Transaction();
  }

  @Transaction
  private static void cleanupUnusedLayers_Transaction()
  {
    List<String> viewNames = DatabaseUtil.getViewsByPrefix(SessionPredicate.PREFIX);

    new Iterables<String>().remove(viewNames, new SessionPredicate());

    for (String viewName : viewNames)
    {
      // First remove the geoserver layer
      GeoserverFacade.removeLayer(viewName);

      // Clear the session map
//      String sessionId = SessionPredicate.getSessionId(viewName);
//      SessionParameterFacade.clear(sessionId);
    }

    // Second delete the database views
    DatabaseUtil.dropViews(viewNames);
  }

  @Request
  public static void cleanupUnusedFiles()
  {
    cleanupUnusedFiles_Transaction();
  }

  @Transaction
  private static void cleanupUnusedFiles_Transaction()
  {
    File root = new File(new File(VaultProperties.getPath("vault.default")), "files");

    if (!root.exists())
    {
      String[] list = root.list();

      if (list != null && list.length > 0)
      {
        LinkedList<String> names = new LinkedList<String>(Arrays.asList(list));

        new Iterables<String>().remove(names, new SessionPredicate());

        for (String name : names)
        {
          File directory = new File(new File(VaultProperties.getPath("vault.default"), "files"), name);

          try
          {
            FileUtils.deleteDirectory(directory);
          }
          catch (IOException e)
          {
            throw new ProgrammingErrorException(e);
          }
        }
      }
    }
  }
}
