package net.geoprism.data.etl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingProgressMonitor implements ProgressMonitorIF
{
  private static Logger logger = LoggerFactory.getLogger(ImportRunnable.class);
  
  private DataImportState state;
  
  private double currentRow;
  
  private String filename;
  
  public LoggingProgressMonitor(String filename)
  {
    this.currentRow = 0;
    this.setFilename(filename);
    this.setState(DataImportState.INITIAL);
  }
  
  public void setFilename(String filename)
  {
    this.filename = filename;
  }
  
  public void setState(DataImportState state)
  {
    this.state = state;
    
    logger.info("Spreadsheet importer entering state [" + state.toString() + "] on file [" + filename + "].");
  }
  
  public DataImportState getState()
  {
    return this.state;
  }
  
  public void setCurrentRow(double rowNum)
  {
    this.currentRow = rowNum;
    
    if (rowNum % 50 == 0)
    {
      logger.info("Spreadsheet importer processing row [" + rowNum + "]");
    }
  }
  
  public double getCurrentRow()
  {
    return this.currentRow;
  }
}
