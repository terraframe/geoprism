package net.geoprism.util;

import org.json.JSONException;
import org.json.JSONObject;

public class ProgressState
{
  private String id;

  private int    total;

  private int    current;

  private String description;

  public ProgressState(String id)
  {
    this.id = id;
    this.current = 0;
    this.total = 1;
  }

  public String getId()
  {
    return id;
  }

  public int getTotal()
  {
    return total;
  }

  public void setTotal(int total)
  {
    this.total = total;
  }

  public int getCurrent()
  {
    return current;
  }

  public void setCurrent(int current)
  {
    this.current = current;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public JSONObject toJSON() throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("total", this.total);
    object.put("current", this.current);
    object.put("description", this.description);

    return object;
  }

}
