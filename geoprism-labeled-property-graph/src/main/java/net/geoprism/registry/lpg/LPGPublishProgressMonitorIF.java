package net.geoprism.registry.lpg;

public interface LPGPublishProgressMonitorIF
{
  
  public void appLock();
  
  public void apply();
  
  public void setWorkProgress(Long num);
  
  public void setWorkTotal(Long total);
  
  public void clearStage();
  
  public void addStage(Object importStage);
  
}
