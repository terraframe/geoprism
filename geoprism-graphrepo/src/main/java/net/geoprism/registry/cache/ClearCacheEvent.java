package net.geoprism.registry.cache;
import java.time.Clock;

import org.springframework.context.ApplicationEvent;

public class ClearCacheEvent extends ApplicationEvent
{

  private static final long serialVersionUID = 1L;

  public ClearCacheEvent(Object source, Clock clock)
  {
    super(source, clock);
  }

  public ClearCacheEvent(Object source)
  {
    super(source);
  }

}
