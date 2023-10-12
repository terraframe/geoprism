package net.geoprism.registry.business;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import net.geoprism.registry.DirectedAcyclicGraphType;

@Component
public interface DirectedAcyclicGraphTypeBusinessServiceIF
{

  public void update(DirectedAcyclicGraphType dagt, JsonObject object);

  public void delete(DirectedAcyclicGraphType dagt);

  public DirectedAcyclicGraphType create(JsonObject object);

  public DirectedAcyclicGraphType create(String code, LocalizedValue label, LocalizedValue description);

}