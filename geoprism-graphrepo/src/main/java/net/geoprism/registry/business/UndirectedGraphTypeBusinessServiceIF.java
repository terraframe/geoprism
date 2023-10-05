package net.geoprism.registry.business;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;

import net.geoprism.registry.UndirectedGraphType;

public interface UndirectedGraphTypeBusinessServiceIF
{

  public UndirectedGraphType create(JsonObject object);

  public UndirectedGraphType create(String code, LocalizedValue label, LocalizedValue description);

  public void update(UndirectedGraphType ugt, JsonObject object);

  public void delete(UndirectedGraphType ugt);

}