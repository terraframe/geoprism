package net.geoprism.dhis2.exporter;

import java.util.ArrayList;

import net.geoprism.dhis2.exporter.PalestineContentHandler.PalestineRow;

public class PalestineConverter
{
  public void processRow(PalestineRow row)
  {
    ArrayList<String> headers = row.getHeaders();
    
    for (int i = 0; i < headers.size(); ++i)
    {
      String value = row.getValue(i);
      String header = headers.get(i);
      
      System.out.println("Processing row [" + row.getRowNum() + " " + header + " " + value + "].");
    }
  }
}
