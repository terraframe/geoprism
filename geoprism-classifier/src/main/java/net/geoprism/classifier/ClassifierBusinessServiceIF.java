package net.geoprism.classifier;

public interface ClassifierBusinessServiceIF
{
  public String createSynonym(String classifierId, String label);
  
  public void deleteSynonym(String synonymId);
}
