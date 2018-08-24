package net.geoprism.dhis2.palestine;

import java.util.ArrayList;
import java.util.HashMap;

import org.aspectj.ajdt.ajc.BuildArgParser;

public class PalestineHardcoded
{
  public static final String orgUnit = "ImspTQPwCqd";
  
  // Spreadsheet definitions
  public static final String spreadsheetName = "Avicenna Data points.xlsx";
//  public static final String[] spreadsheetTabNames = new String[]{
//	  "Mother's Details_Avicenna", 
//	  "Baby Birth_Avicenna", 
//	  "Cause of CS_Avicenna", 
//	  "LAB_Avicenna", 
//	  "Observations_Avicenna"
//	  }; 
  
  public static final String[] tab1Columns = new String[]{
	  "Mother ID NO",
	  "Mother Full Name",
	  "Mother Birth date",
	  "ADDRESS",
	  "CITYID",
	  "NAME",
	  "MIDDLE NAME",
	  "SURNAME",
	  "FATHER NAME",
	  "Baby Record date creation"
  };
  
  public static final String[] tab2Columns = new String[]{
	  "Mother ID NO",
	  "Mother Full Name",
	  "Baby Birth Mark",
	  "Baby Birth Comment",
	  "Baby Birthdate",
	  "Baby Weight",
	  "Baby Height",
	  "Baby APGAR 1 MIN Score",
	  "Baby APGAR 5 MIN Score",
	  "Baby APGAR 10 MIN Score",
	  "Baby Pregnancy no of weeks",
	  "Baby Gender",
	  "Baby Anus status",
	  "Baby Record date creation",
	  "Baby Birth Result",
	  "Baby Birth Type",
	  "NAME"
  };
  
  public static final String[] tab3Columns = new String[]{
	  "Mother ID NO",
	  "Mother Full Name",
	  "DATATEXT",
	  "BARADMISSIONID",
	  "NAME",
	  "DATECREATED"
  };
  
  public static final String[] tab4Columns = new String[]{
	  "Mother Full Name",
	  "Mother ID NO",
	  "Test Name",
	  "Test Unit",
	  "Test Min Val",
	  "Test Max Val",
	  "Test Result",
	  "Test Date",
	  "NAME"
  };
  
  public static final String[] tab5Columns = new String[]{
	  "Mother ID NO",
	  "Mother Full Name",
	  "DATEPROCESS",
	  "SISTOLIC",
	  "DIASTOLIC",
	  "NAME"
  };

  public static final HashMap<String, String[]> map = new HashMap<String, String[]>();
  static
  {
  
	  map.put("Mother's Details_Avicenna", tab1Columns);
	  map.put("Baby Birth_Avicenna", tab2Columns);
	  map.put("Cause of CS_Avicenna", tab3Columns);
	  map.put("LAB_Avicenna", tab4Columns);
	  map.put("Observations_Avicenna", tab5Columns);
  }
  
}
