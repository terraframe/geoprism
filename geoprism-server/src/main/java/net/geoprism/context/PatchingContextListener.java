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
package net.geoprism.context;

import java.io.File;

import net.geoprism.GeoprismPatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.generation.loader.Reloadable;

public class PatchingContextListener implements Reloadable, ServerContextListener
{
  private static Logger   logger = LoggerFactory.getLogger(PatchingContextListener.class);

  private GeoprismPatcher patcher;

  @Override
  public void initialize()
  {
    patcher = new GeoprismPatcher(new File(DeployProperties.getDeployBin(), "metadata"));

    System.out.println(patcher.toString());
  }

  @Override
  public void startup()
  {
    if (patcher == null)
    {
      patcher = new GeoprismPatcher(new File(DeployProperties.getDeployBin(), "metadata"));
    }

    patcher.startup();
  }

  @Override
  public void shutdown()
  {
    patcher.shutdown();
  }
}
