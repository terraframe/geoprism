package com.runwaysdk.geodashboard.ontology;

public class ClassifierMultiTermAttributeRoot extends ClassifierMultiTermAttributeRootBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -706955988;
  
  public ClassifierMultiTermAttributeRoot(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public ClassifierMultiTermAttributeRoot(com.runwaysdk.system.metadata.MdAttributeMultiTerm parent, com.runwaysdk.geodashboard.ontology.Classifier child)
  {
    this(parent.getId(), child.getId());
  }
  
}
