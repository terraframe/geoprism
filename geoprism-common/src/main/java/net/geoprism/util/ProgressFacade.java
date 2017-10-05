package net.geoprism.util;

public class ProgressFacade
{
  private static final ProgressManager manager = new ProgressManager();

  public static void add(ProgressState state)
  {
    manager.add(state);
  }

  public static void remove(String id)
  {
    manager.remove(id);
  }

  public static ProgressState get(String id)
  {
    return manager.get(id);
  }
}
