package net.geoprism.registry.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.registry.graph.transition.Transition;
import net.geoprism.registry.graph.transition.TransitionEvent;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

@Component
public class TransitionBusinessService implements TransitionBusinessServiceIF
{
  @Autowired
  protected GeoObjectTypeBusinessServiceIF gotServ;
  
  @Transaction
  public void delete(Transition tran)
  {
    tran.delete();
  }
  
  @Transaction
  public void apply(Transition tran, TransitionEvent event, int order, VertexServerGeoObject source, VertexServerGeoObject target)
  {
    this.validate(tran, event, source, target);

    tran.setOrder(order);
    tran.setValue(Transition.SOURCE, source.getVertex());
    tran.setValue(Transition.TARGET, target.getVertex());

    tran.apply();
  }
  
  public void validate(Transition tran, TransitionEvent event, VertexServerGeoObject source, VertexServerGeoObject target)
  {
    ServerGeoObjectType beforeType = ServerGeoObjectType.get(event.getBeforeTypeCode());
    ServerGeoObjectType afterType = ServerGeoObjectType.get(event.getAfterTypeCode());

    List<ServerGeoObjectType> beforeSubtypes = gotServ.getSubtypes(beforeType);
    List<ServerGeoObjectType> afterSubtypes = gotServ.getSubtypes(afterType);

    if (! ( beforeSubtypes.contains(source.getType()) || beforeType.getCode().equals(source.getType().getCode()) ))
    {
      // This should be prevented by the front-end
      throw new ProgrammingErrorException("Source type must be a subtype of (" + beforeType.getCode() + ")");
    }

    if (! ( afterSubtypes.contains(target.getType()) || afterType.getCode().equals(target.getType().getCode()) ))
    {
      // This should be prevented by the front-end
      throw new ProgrammingErrorException("Target type must be a subtype of (" + afterType.getCode() + ")");
    }
  }
}
