package net.geoprism.registry.service.request;

import java.util.List;

public interface AbstractCrudServiceIF<T>
{
  public List<T> getAll(String sessionId);

  public void delete(String sessionId, String code);

  public T apply(String sessionId, T object);

  public T getByCode(String sessionId, String code);

}
