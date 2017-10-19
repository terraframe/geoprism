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
package net.geoprism.data.etl;

import net.geoprism.localization.LocalizationFacade;
import net.geoprism.util.ProgressFacade;
import net.geoprism.util.ProgressState;

public class ProgressStateMonitor implements ProgressMonitorIF
{
  private ProgressState state;

  public ProgressStateMonitor(String uploadId)
  {
    this.state = new ProgressState(uploadId);

    ProgressFacade.add(state);
  }

  @Override
  public void setFilename(String filename)
  {
  }

  @Override
  public void setState(DataImportState state)
  {
    String key = "dataUploader.state." + state.name().toLowerCase();
    String description = LocalizationFacade.getFromBundles(key);
    
    this.state.setDescription(description);
  }

  @Override
  public void setCurrentRow(int rowNum)
  {
    this.state.setCurrent(rowNum);
  }

  @Override
  public void setTotal(int total)
  {
    this.state.setTotal(total);
  }

  @Override
  public void finished()
  {
    ProgressFacade.remove(this.state.getId());
  }
}
