package net.geoprism.dhis2.palestine;

import java.util.ArrayList;
import java.util.Collection;

import net.geoprism.data.etl.ConverterIF;
import net.geoprism.data.etl.ImportProblemIF;
import net.geoprism.data.etl.ProgressMonitorIF;
import net.geoprism.data.etl.TargetContextIF;
import net.geoprism.dhis2.connector.AbstractDHIS2Connector;
import net.geoprism.dhis2.palestine.PalestineContentHandler.PalestineRow;
import net.geoprism.dhis2.palestine.PalestineContentHandler.PalestineSheet;

import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.business.Transient;

public class PalestineConverter implements ConverterIF
{
  private AbstractDHIS2Connector dhis2;
  
  private Workbook        errors;
  
  private ProgressMonitorIF monitor;
  
  public PalestineConverter(AbstractDHIS2Connector dhis2, ProgressMonitorIF monitor)
  {
    this.dhis2 = dhis2;
    this.monitor = monitor;
  }
  
  public void processRow(PalestineRow row, PalestineSheet sheet)
  {
    boolean valid = true;
    
    ArrayList<String> headers = row.getHeaders();
    
    for (int i = 0; i < headers.size(); ++i)
    {
      String value = row.getValue(i);
      String header = headers.get(i);
      
      System.out.println("Processing sheet [" + sheet.getSheetName() + ", " + sheet.getSheetNumber() + "] row [" + row.getRowNum() + " " + header + " " + value + "].");
      
//      String enrollment = getEnrollment();
    }
    
    if (valid)
    {
      this.monitor.entityImported(null);
    }
  }

  @Override
  public void create(Transient source)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public TargetContextIF getTargetContext()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setErrors(Workbook errors)
  {
    this.errors = errors;
  }

  @Override
  public Workbook getErrors()
  {
    return this.errors;
  }

  public Collection<ImportProblemIF> getProblems()
  {
    return new ArrayList<ImportProblemIF>();
  }
  
//  private String getEnrollment(String trackedEntityInstance)
//  {
//    HTTPResponse response = this.dhis2.apiGet("enrollments", new NameValuePair[] {
//      new NameValuePair("ou", PalestineHardcoded.orgUnit),
//      new NameValuePair("trackedEntityInstance", trackedEntityInstance)
//    });
//    
//    DHIS2TrackerResponseProcessor.validateStatusCode(response);
//    
//    
//  }
}
