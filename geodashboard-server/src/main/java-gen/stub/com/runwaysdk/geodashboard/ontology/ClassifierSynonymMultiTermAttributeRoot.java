package com.runwaysdk.geodashboard.ontology;

public class ClassifierSynonymMultiTermAttributeRoot extends ClassifierSynonymMultiTermAttributeRootBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -402938720;
  
  public ClassifierSynonymMultiTermAttributeRoot(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public ClassifierSynonymMultiTermAttributeRoot(com.runwaysdk.system.metadata.MdAttributeMultiTerm parent, com.runwaysdk.geodashboard.ontology.ClassifierSynonym child)
  {
    this(parent.getId(), child.getId());
  }
  
}
