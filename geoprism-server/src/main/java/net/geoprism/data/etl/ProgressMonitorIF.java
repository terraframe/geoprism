package net.geoprism.data.etl;

public interface ProgressMonitorIF
{
  public void setFilename(String filename);
  
  public void setState(DataImportState state);
  
  public void setCurrentRow(double rowNum);
}
