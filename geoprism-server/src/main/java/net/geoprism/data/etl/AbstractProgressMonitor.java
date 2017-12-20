package net.geoprism.data.etl;

public class AbstractProgressMonitor implements ProgressMonitorIF
{
  private int progressUnit;
  
  private int importCount;
  
  private int totalProgressUnits;
  
  private DataImportState state;
  
  private String          filename;
  
  public AbstractProgressMonitor()
  {
    this.importCount = 0;
    this.totalProgressUnits = 0;
  }
  
  @Override
  public void setFilename(String filename)
  {
    this.filename = filename;
  }
  
  @Override
  public String getFilename()
  {
    return this.filename;
  }

  @Override
  public void setState(DataImportState state)
  {
    this.state = state;
  }
  
  @Override
  public DataImportState getState()
  {
    return this.state;
  }

  @Override
  public void setTotalProgressUnits(int totalProgressUnits)
  {
    this.totalProgressUnits = totalProgressUnits;
  }

  @Override
  public int getTotalProgressUnits()
  {
    return totalProgressUnits;
  }
  
  @Override
  public void setCurrentProgressUnit(int unit)
  {
    this.progressUnit = unit;
  }

  @Override
  public int getCurrentProgressUnit()
  {
    return this.progressUnit;
  }

  @Override
  public int getImportCount()
  {
    return this.importCount;
  }

  @Override
  public void entityImported(TargetDefinitionIF entity)
  {
    this.importCount++;
  }
  
  @Override
  public void finished()
  {
    
  }
}
