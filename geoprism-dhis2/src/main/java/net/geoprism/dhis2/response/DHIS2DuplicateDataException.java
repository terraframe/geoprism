package net.geoprism.dhis2.response;

import java.util.List;

import org.json.JSONObject;

public class DHIS2DuplicateDataException extends RuntimeException
{
  private static final long serialVersionUID = -5902095640582124900L;
  
  JSONObject errorReport;
  
  List<String> msgs;
  
  public DHIS2DuplicateDataException(List<String> msgs)
  {
    this.msgs = msgs;
  }
  
  public DHIS2DuplicateDataException(JSONObject errorReport)
  {
    this.errorReport = errorReport;
  }
  
  public DHIS2DuplicateDataException(String message)
  {
    super(message);
  }
  
  public List<String> getErrorMessages()
  {
    return this.msgs;
  }
}
