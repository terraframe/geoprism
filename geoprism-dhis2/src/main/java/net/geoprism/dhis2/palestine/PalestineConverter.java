package net.geoprism.dhis2.palestine;

import java.util.ArrayList;

import net.geoprism.dhis2.connector.AbstractDHIS2Connector;
import net.geoprism.dhis2.palestine.PalestineContentHandler.PalestineRow;
import net.geoprism.dhis2.palestine.PalestineContentHandler.PalestineSheet;

public class PalestineConverter
{
  private AbstractDHIS2Connector dhis2;
  
  public PalestineConverter(AbstractDHIS2Connector dhis2)
  {
    this.dhis2 = dhis2;
  }
  
  public void processRow(PalestineRow row, PalestineSheet sheet)
  {
    ArrayList<String> headers = row.getHeaders();
    
    for (int i = 0; i < headers.size(); ++i)
    {
      String value = row.getValue(i);
      String header = headers.get(i);
      
      System.out.println("Processing sheet [" + sheet.getSheetName() + ", " + sheet.getSheetNumber() + "] row [" + row.getRowNum() + " " + header + " " + value + "].");
      
//      String enrollment = getEnrollment();
    }
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
