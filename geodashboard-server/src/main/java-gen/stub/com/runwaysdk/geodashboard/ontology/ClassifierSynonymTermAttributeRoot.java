package com.runwaysdk.geodashboard.ontology;

public class ClassifierSynonymTermAttributeRoot extends ClassifierSynonymTermAttributeRootBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -91099530;
  
  public ClassifierSynonymTermAttributeRoot(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public ClassifierSynonymTermAttributeRoot(com.runwaysdk.system.metadata.MdAttributeTerm parent, com.runwaysdk.geodashboard.ontology.ClassifierSynonym child)
  {
    this(parent.getId(), child.getId());
  }
  
}
