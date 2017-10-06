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
