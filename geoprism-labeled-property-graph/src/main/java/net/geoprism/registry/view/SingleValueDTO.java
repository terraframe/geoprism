package net.geoprism.registry.view;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleValueDTO extends AttributeValueDTO
{
  public static final String TYPE = "single";

  @JsonProperty("type")
  private final String       type = TYPE;

  private Object             value;

  public SingleValueDTO()
  {
    super();
  }

  public SingleValueDTO(Object value)
  {
    super();
    this.value = value;
  }

  public Object getValue()
  {
    return value;
  }

  public void setValue(Object value)
  {
    this.value = value;
  }
}
