package com.runwaysdk.geodashboard.ontology;

public class ClassifierAttributeRoot extends ClassifierAttributeRootBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1480065738;
  
  public ClassifierAttributeRoot(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public ClassifierAttributeRoot(com.runwaysdk.system.metadata.MdAttributeTerm parent, com.runwaysdk.geodashboard.ontology.Classifier child)
  {
    this(parent.getId(), child.getId());
  }
  
}
