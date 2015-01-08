package com.runwaysdk.geodashboard.ontology;

public class ClassifierSynonymAttributeRoot extends ClassifierSynonymAttributeRootBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1314811906;
  
  public ClassifierSynonymAttributeRoot(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public ClassifierSynonymAttributeRoot(com.runwaysdk.system.metadata.MdAttributeTerm parent, com.runwaysdk.geodashboard.ontology.ClassifierSynonym child)
  {
    this(parent.getId(), child.getId());
  }
  
}
