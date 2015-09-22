package com.runwaysdk.geodashboard.ontology;

public class ClassifierTermAttributeRoot extends ClassifierTermAttributeRootBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1968621482;
  
  public ClassifierTermAttributeRoot(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public ClassifierTermAttributeRoot(com.runwaysdk.system.metadata.MdAttributeTerm parent, com.runwaysdk.geodashboard.ontology.Classifier child)
  {
    this(parent.getId(), child.getId());
  }
  
}
