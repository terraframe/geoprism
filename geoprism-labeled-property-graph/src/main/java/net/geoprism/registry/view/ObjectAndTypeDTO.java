package net.geoprism.registry.view;

public class ObjectAndTypeDTO
{
  private ObjectClassDTO     type;

  private ObjectOverTimeDTO object;

  public ObjectClassDTO getType()
  {
    return type;
  }

  public void setType(ObjectClassDTO type)
  {
    this.type = type;
  }

  public ObjectOverTimeDTO getObject()
  {
    return object;
  }

  public void setObject(ObjectOverTimeDTO object)
  {
    this.object = object;
  }
}
