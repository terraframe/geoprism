package net.geoprism.registry.model;

public abstract class CachableObjectWrapper<T>
{
  private boolean dirty;

  private T       object;

  public CachableObjectWrapper()
  {
  }

  public CachableObjectWrapper(T object)
  {
    this();
    this.object = object;
  }

  public void markAsDirty()
  {
    this.dirty = true;
  }

  protected void setObject(T object)
  {
    this.object = object;
  }

  public T getObject()
  {
    synchronized (this)
    {
      if (this.dirty)
      {
        this.refresh(object);

        this.dirty = false;
      }

    }

    return object;
  }

  protected abstract void refresh(T object);
}
