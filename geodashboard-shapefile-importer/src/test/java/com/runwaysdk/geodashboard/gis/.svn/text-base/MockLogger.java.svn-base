package dss.vector.solutions.gis;

import java.util.HashMap;

public class MockLogger implements GISImportLoggerIF
{
  private HashMap<String, Throwable> map;

  private HashMap<String, String> messages;

  public MockLogger()
  {
    this.map = new HashMap<String, Throwable>();
    this.messages = new HashMap<String, String>();
  }

  @Override
  public boolean hasLogged()
  {
    return ( map.size() > 0 || messages.size() > 0);
  }

  @Override
  public void log(String featureId, Throwable t)
  {
    map.put(featureId, t);
  }
  
  @Override
  public void log(String featureId, String message)
  {
    messages.put(featureId, message);
  }


  @Override
  public void close()
  {
  }

  public HashMap<String, Throwable> getMap()
  {
    return map;
  }
  
  public HashMap<String, String> getMessages()
  {
    return messages;
  }
}
