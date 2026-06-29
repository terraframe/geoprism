package net.geoprism.registry.view;

import java.util.List;

public class OrganizationGroup<T>
{
  private String  oid;

  private String  code;

  private String  label;

  private Boolean write;

  private List<T> types;

  public String getOid()
  {
    return oid;
  }

  public void setOid(String oid)
  {
    this.oid = oid;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public Boolean getWrite()
  {
    return write;
  }

  public void setWrite(Boolean write)
  {
    this.write = write;
  }

  public List<T> getTypes()
  {
    return types;
  }

  public void setTypes(List<T> types)
  {
    this.types = types;
  }

}
