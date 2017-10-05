package net.geoprism.data.etl;

public class CompositeMonitor implements ProgressMonitorIF
{
  private ProgressMonitorIF[] monitors;

  public CompositeMonitor(ProgressMonitorIF... monitors)
  {
    this.monitors = monitors;
  }

  @Override
  public void setFilename(String filename)
  {
    for (ProgressMonitorIF monitor : monitors)
    {
      monitor.setFilename(filename);
    }
  }

  @Override
  public void setState(DataImportState state)
  {
    for (ProgressMonitorIF monitor : monitors)
    {
      monitor.setState(state);
    }
  }

  @Override
  public void setTotal(int total)
  {
    for (ProgressMonitorIF monitor : monitors)
    {
      monitor.setTotal(total);
    }
  }

  @Override
  public void setCurrentRow(int rowNum)
  {
    for (ProgressMonitorIF monitor : monitors)
    {
      monitor.setCurrentRow(rowNum);
    }
  }

  @Override
  public void finished()
  {
    for (ProgressMonitorIF monitor : monitors)
    {
      monitor.finished();
    }
  }

}
