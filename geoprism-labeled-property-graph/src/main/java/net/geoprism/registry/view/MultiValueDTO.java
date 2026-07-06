package net.geoprism.registry.view;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiValueDTO extends AttributeValueDTO
{
  public static final String          TYPE = "multi";

  @JsonProperty("type")
  private final String                type = TYPE;

  private List<ValueOverTimeEntryDTO> values;

  public MultiValueDTO()
  {
    super();
  }

  public MultiValueDTO(List<ValueOverTimeEntryDTO> values)
  {
    super();
    this.values = values;
  }

  public List<ValueOverTimeEntryDTO> getValues()
  {
    return values;
  }

  public void setValues(List<ValueOverTimeEntryDTO> values)
  {
    this.values = values;
  }

  @JsonIgnore
  public Optional<Object> getValue(Date date)
  {
    return this.values.stream().filter(entry -> entry.isValid(date)).findFirst().map(entry -> entry.getValue());
  }
}
