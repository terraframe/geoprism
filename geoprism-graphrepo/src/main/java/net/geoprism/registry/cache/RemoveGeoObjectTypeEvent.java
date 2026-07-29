package net.geoprism.registry.cache;
import java.time.Clock;

import org.springframework.context.ApplicationEvent;

public class RemoveGeoObjectTypeEvent extends ApplicationEvent
{

  private static final long serialVersionUID = 1L;

  public RemoveGeoObjectTypeEvent(Object source, Clock clock)
  {
    super(source, clock);
  }

  public RemoveGeoObjectTypeEvent(Object source)
  {
    super(source);
  }

}
