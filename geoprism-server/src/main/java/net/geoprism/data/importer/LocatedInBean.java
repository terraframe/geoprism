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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.runwaysdk.generated.system.gis.geo.UniversalAllPathsTableQuery;

import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.AllowedInQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;

/**
 * Bean which holds all data required for rebuilding the located in table;
 * 
 * @author Justin Smethie
 */
public class LocatedInBean 
{
  public enum BuildTypes  {

    REBUILD_ALL(1), ORPHANED_ONLY(2);

    private int code;

    private BuildTypes(int code)
    {
      this.code = code;
    }

    int getCode()
    {
      return code;
    }
  }

  /**
   * PropertyChangeSupport
   */
  private PropertyChangeSupport         propertyChangeSupport;

  private BuildTypes                    option;

  /**
   * Percent of area two entites must over lap before one is considered located in the other.
   */
  private int                           overlapPercent;

  private Map<String, List<PathOption>> paths;

  public LocatedInBean()
  {
    this.option = null;
    this.overlapPercent = 80;
    this.paths = LocatedInBean.buildDefaultPaths();

    this.propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  public BuildTypes getOption()
  {
    return option;
  }

  public void setOption(BuildTypes option)
  {
    propertyChangeSupport.firePropertyChange("option", this.option, this.option = option);
  }

  public int getOverlapPercent()
  {
    return overlapPercent;
  }

  public void setOverlapPercent(int overlapPercent)
  {
    propertyChangeSupport.firePropertyChange("overlapPercent", this.overlapPercent, this.overlapPercent = overlapPercent);
  }

  public Map<String, List<PathOption>> getPaths()
  {
    return this.paths;
  }

  public static Map<String, List<PathOption>> buildDefaultPaths()
  {
    Universal root = Universal.getRoot();

    Map<String, PathOption> map = new LinkedHashMap<String, PathOption>();

    List<? extends Universal> universals = new UniversalQuery(new QueryFactory()).getIterator().getAll();

    for (Universal universal : universals)
    {
      // Parent-map : Map of nodes which have the key as a parent
      Map<String, List<PathOption>> parentMap = new LinkedHashMap<String, List<PathOption>>();

      // Child-map : Map of nodes which have the key as a child
      Map<String, List<PathOption>> childMap = new LinkedHashMap<String, List<PathOption>>();

      String universalId = universal.getOid();

      QueryFactory factory = new QueryFactory();

      UniversalAllPathsTableQuery parentQuery = new UniversalAllPathsTableQuery(factory);
      parentQuery.WHERE(parentQuery.getChildTerm().EQ(universal));
      parentQuery.AND(parentQuery.getParentTerm().NE(root));
      parentQuery.AND(parentQuery.getParentTerm().NE(universal));

      UniversalAllPathsTableQuery aptQuery = new UniversalAllPathsTableQuery(factory);
      aptQuery.WHERE(aptQuery.getChildTerm().EQ(universal));

      AllowedInQuery aiQuery = new AllowedInQuery(factory);
      aiQuery.WHERE(aiQuery.getParent().EQ(parentQuery.getParentTerm()));
      aiQuery.AND(aiQuery.getChild().EQ(aptQuery.getParentTerm()));

      List<? extends AllowedIn> relationships = aiQuery.getIterator().getAll();

      for (AllowedIn relationship : relationships)
      {
        String oid = relationship.getOid();
        String key = universalId + "-" + oid;
        String parentOid = relationship.getParentOid();
        String childOid = relationship.getChildOid();

        PathOption node = new PathOption(universalId, relationship);

        map.putIfAbsent(key, node);

        parentMap.putIfAbsent(parentOid, new LinkedList<PathOption>());
        parentMap.get(parentOid).add(node);

        childMap.putIfAbsent(childOid, new LinkedList<PathOption>());
        childMap.get(childOid).add(node);

        // Link this node to its children
        if (parentMap.containsKey(childOid))
        {
          List<PathOption> children = parentMap.get(childOid);

          for (PathOption child : children)
          {
            node.addChild(child);
            child.addParent(node);
          }
        }

        // Link this node to its parents
        if (childMap.containsKey(parentOid))
        {
          List<PathOption> parents = childMap.get(parentOid);

          for (PathOption parent : parents)
          {
            node.addParent(parent);
            parent.addChild(node);
          }
        }
      }
    }

    Map<String, List<PathOption>> paths = new LinkedHashMap<String, List<PathOption>>();

    Set<Entry<String, PathOption>> entries = map.entrySet();

    for (Entry<String, PathOption> entry : entries)
    {
      PathOption option = entry.getValue();

      if (option.getChildren().size() == 0)
      {
        paths.putIfAbsent(option.getRoot(), new LinkedList<PathOption>());

        paths.get(option.getRoot()).add(option);
      }
    }

    return paths;
  }

}
