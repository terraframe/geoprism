package net.geoprism;

import org.json.JSONString;

public class JSONStringImpl implements JSONString
{
  private String json;

  public JSONStringImpl(String json)
  {
    this.json = json;
  }

  @Override
  public String toJSONString()
  {
    return this.json;
  }

  @Override
  public String toString()
  {
    return this.json;
  }
}
