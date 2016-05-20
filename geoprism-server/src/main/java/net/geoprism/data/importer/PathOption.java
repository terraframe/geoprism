/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.data.importer;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.system.gis.geo.AllowedIn;

/**
 * Represents a path option in the located in builder process. Really this class is a wrapper around an AllowedIn
 * relationship which can be enabled and disabled.
 * 
 * @author Justin Smethie
 */
public class PathOption
{
  /**
   * Id of the root universal the (root, parent, child) tuple
   */
  private String           root;

  /**
   * Id of the parent universal the (root, parent, child) tuple
   */
  private String           id;

  /**
   * Id of the parent universal of this node
   */
  private String           parentId;

  /**
   * Id of the child universal of this node
   */
  private String           childId;

  /**
   * Flag indicating if the path is enabled
   */
  private boolean          enabled;

  /**
   * Label of the parent side of this node
   */
  private String           parentLabel;

  /**
   * Label of the child side of this node
   */
  private String           childLabel;

  /**
   * List of parent nodes
   */
  private List<PathOption> parents;

  /**
   * List of children nodes
   */
  private List<PathOption> children;

  public PathOption(String root, AllowedIn allowedIn)
  {
    this.root = root;
    this.id = allowedIn.getId();
    this.parentId = allowedIn.getParentId();
    this.childId = allowedIn.getChildId();
    this.parentLabel = allowedIn.getParent().getDisplayLabel().getValue();
    this.childLabel = allowedIn.getChild().getDisplayLabel().getValue();
    this.enabled = true;

    this.children = new LinkedList<PathOption>();
    this.parents = new LinkedList<PathOption>();
  }

  public String getRoot()
  {
    return root;
  }

  public void setRoot(String root)
  {
    this.root = root;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public boolean isEnabled()
  {
    return enabled;
  }

  public void setEnabled(boolean enabled)
  {
    this.enabled = enabled;
  }

  public String getParentLabel()
  {
    return this.parentLabel;
  }

  public void setParentLabel(String parentLabel)
  {
    this.parentLabel = parentLabel;
  }

  public String getChildLabel()
  {
    return childLabel;
  }

  public void setChildLabel(String childLabel)
  {
    this.childLabel = childLabel;
  }

  public String getParentId()
  {
    return parentId;
  }

  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }

  public String getChildId()
  {
    return childId;
  }

  public void setChildId(String childId)
  {
    this.childId = childId;
  }

  public void addChild(PathOption child)
  {
    this.children.add(child);
  }

  public List<PathOption> getChildren()
  {
    return children;
  }

  public void addParent(PathOption parent)
  {
    this.parents.add(parent);
  }

  public List<PathOption> getParents()
  {
    return this.parents;
  }
}
