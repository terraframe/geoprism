package net.geoprism.classifier;

public interface ClassifierServiceIF
{
  public String createSynonym(String sessionId, String classifierId, String label);
  
  public void deleteSynonym(String sessionId, String synonymId);
}
