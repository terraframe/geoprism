package com.runwaysdk.geodashboard.databrowser;

import com.runwaysdk.system.metadata.MdType;

public class MetadataType extends MetadataTypeBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 181676503;
  
  public MetadataType()
  {
    super();
  }
  
  public MetadataType(String parentId, MdType mdType)
  {
    super();
    
    this.setParentId(parentId);
    this.setDisplayLabel(mdType.getDisplayLabel().getValue());
    this.setTypeName(mdType.definesType());
    this.setTypeId(mdType.getId());
  }
  
}
