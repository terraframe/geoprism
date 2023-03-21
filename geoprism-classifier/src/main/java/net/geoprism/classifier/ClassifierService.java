package net.geoprism.classifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

@Component
public class ClassifierService implements ClassifierServiceIF
{
  @Autowired
  private ClassifierBusinessServiceIF service;
  
  @Request(RequestType.SESSION)
  @Override
  public String createSynonym(String sessionId, String classifierId, String label)
  {
    return this.service.createSynonym(classifierId, label);
  }

  @Request(RequestType.SESSION)
  @Override
  public void deleteSynonym(String sessionId, String synonymId)
  {
    this.service.deleteSynonym(synonymId);
  }
}
