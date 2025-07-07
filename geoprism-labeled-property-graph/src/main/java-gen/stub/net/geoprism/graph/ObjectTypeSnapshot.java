package net.geoprism.graph;

public abstract class ObjectTypeSnapshot extends ObjectTypeSnapshotBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1791617045;

  public ObjectTypeSnapshot()
  {
    super();
  }

  public abstract String getGraphMdVertexOid();

  public abstract String getCode();

}
