/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.ontology;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.system.gis.geo.Universal;

public class GeoEntityUtil extends GeoEntityUtilBase
{
  private static final long           serialVersionUID = -395452858;

  public GeoEntityUtil()
  {
    super();
  }

  public static Collection<Term> getOrderedAncestors(Term root, Term term, String relationship)
  {
    Map<String, Term> map = new LinkedHashMap<String, Term>();

    OIterator<Term> iterator = term.getDirectAncestors(relationship);
    try
    {
      List<Term> parents = iterator.getAll();

      for (Term parent : parents)
      {
        if (!parent.getOid().equals(root.getOid()))
        {
          Collection<Term> ancestors = GeoEntityUtil.getOrderedAncestors(root, parent, relationship);

          for (Term ancestor : ancestors)
          {
            if (!map.containsKey(ancestor.getOid()))
            {
              map.put(ancestor.getOid(), ancestor);
            }
          }
        }
      }

      map.put(term.getOid(), term);
    }
    finally
    {
      iterator.close();
    }

    return map.values();
  }

  public static Collection<Term> getOrderedDescendants(Term term, String relationship)
  {
    Map<String, Term> map = new LinkedHashMap<String, Term>();

    OIterator<Term> iterator = term.getDirectDescendants(relationship);

    try
    {
      map.put(term.getOid(), term);

      List<Term> parents = iterator.getAll();

      for (Term parent : parents)
      {
        Collection<Term> descendants = GeoEntityUtil.getOrderedDescendants(parent, relationship);

        for (Term descendant : descendants)
        {
          if (!map.containsKey(descendant.getOid()))
          {
            map.put(descendant.getOid(), descendant);
          }
        }
      }
    }
    finally
    {
      iterator.close();
    }

    return map.values();
  }


  public static Universal[] getUniversals(String parentId, String mdRelationshipId)
  {
    Universal universal = Universal.get(parentId);
    MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.get(mdRelationshipId);

    List<Term> children = (List<Term>) universal.getDirectDescendants(mdRelationship.definesType()).getAll();

    return children.toArray(new Universal[children.size()]);
  }
}
