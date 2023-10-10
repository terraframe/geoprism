package net.geoprism.registry.business;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;

import net.geoprism.registry.DirectedAcyclicGraphType;

public interface DirectedAcyclicGraphTypeBusinessServiceIF
{

  public void update(DirectedAcyclicGraphType dagt, JsonObject object);

  public void delete(DirectedAcyclicGraphType dagt);

  public DirectedAcyclicGraphType create(JsonObject object);

  public DirectedAcyclicGraphType create(String code, LocalizedValue label, LocalizedValue description);

}