package net.geoprism.registry.lpg;

public class LPGPublishProgressMonitorNoOp implements LPGPublishProgressMonitorIF
{
  
  @Override
  public void appLock()
  {
    // No OP
  }

  @Override
  public void apply()
  {
    // No OP
  }

  @Override
  public void setWorkProgress(Long num)
  {
    // No OP
  }

  @Override
  public void setWorkTotal(Long total)
  {
    // No OP
  }

  @Override
  public void clearStage()
  {
    // No OP
  }

  @Override
  public void addStage(Object importStage)
  {
    // No OP
  }

}
