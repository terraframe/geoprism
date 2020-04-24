package net.geoprism;

import com.runwaysdk.constants.ClientRequestIF;

public class SessionEvent
{
  public static enum EventType {
    LOGIN_SUCCESS, LOGIN_FAILURE
  }

  private EventType       type;

  private ClientRequestIF request;

  private String          username;

  public SessionEvent(EventType type, ClientRequestIF request, String username)
  {
    super();
    this.type = type;
    this.request = request;
    this.username = username;
  }

  public EventType getType()
  {
    return type;
  }

  public void setType(EventType type)
  {
    this.type = type;
  }

  public ClientRequestIF getRequest()
  {
    return request;
  }

  public void setRequest(ClientRequestIF request)
  {
    this.request = request;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

}
