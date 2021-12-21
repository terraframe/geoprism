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
package net.geoprism.data.etl;

import net.geoprism.localization.LocalizationFacade;
import net.geoprism.util.ProgressFacade;
import net.geoprism.util.ProgressState;

public class ProgressStateMonitor extends AbstractProgressMonitor
{
  private ProgressState progress;

  public ProgressStateMonitor(String uploadId)
  {
    this.progress = new ProgressState(uploadId);

    ProgressFacade.add(progress);
  }

  @Override
  public void setState(DataImportState state)
  {
    super.setState(state);
    
    String key = "dataUploader.state." + state.name().toLowerCase();
    String description = LocalizationFacade.getFromBundles(key);
    
    this.progress.setDescription(description);
  }

  @Override
  public void setTotalProgressUnits(int total)
  {
    super.setTotalProgressUnits(total);
    
    this.progress.setTotal(total);
  }
  
  @Override
  public void setCurrentProgressUnit(int unit)
  {
    super.setCurrentProgressUnit(unit);
    
    this.progress.setCurrent(unit);
  }

  @Override
  public void finished()
  {
    super.finished();
    
    ProgressFacade.remove(this.progress.getOid());
  }
}
