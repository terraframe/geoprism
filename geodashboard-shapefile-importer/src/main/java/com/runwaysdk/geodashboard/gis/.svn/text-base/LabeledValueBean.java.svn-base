package dss.vector.solutions.gis;

public class LabeledValueBean
{
  private String value;

  private String label;

  public LabeledValueBean(String value)
  {
    super();
    this.value = value;
    this.label = value;
  }

  public LabeledValueBean(String value, String label)
  {
    super();
    this.value = value;
    this.label = label;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof LabeledValueBean)
    {
      LabeledValueBean bean = (LabeledValueBean) obj;
      String _value = bean.getValue();

      if (value != null && _value != null)
      {
        return value.equals(_value);
      }

      return value == _value;
    }

    return super.equals(obj);
  }

}
