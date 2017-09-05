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
package net.geoprism.data.etl;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.geoprism.ontology.GeoEntityUtil;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttribute;

public class TargetFieldGeoEntityBinding extends TargetFieldGeoEntityBindingBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static class UniversalAttributeBindingComparator implements Comparator<UniversalAttributeBinding>
  {
    private Map<String, Integer> indices;

    public UniversalAttributeBindingComparator(Collection<Term> terms)
    {
      this.indices = new HashMap<String, Integer>();

      int index = 0;

      for (Term term : terms)
      {
        this.indices.put(term.getId(), index++);
      }
    }

    @Override
    public int compare(UniversalAttributeBinding o1, UniversalAttributeBinding o2)
    {
      Integer i1 = this.indices.get(o1.getUniversalId());
      Integer i2 = this.indices.get(o2.getUniversalId());

      return i1.compareTo(i2);
    }

  }

  private static final long serialVersionUID = -2005836550;

  public TargetFieldGeoEntityBinding()
  {
    super();
  }

  @Override
  @Transaction
  public void delete()
  {
    List<UniversalAttributeBinding> attributes = this.getUniversalAttributes();

    for (UniversalAttributeBinding attribute : attributes)
    {
      attribute.delete();
    }

    super.delete();
  }

  public List<UniversalAttributeBinding> getUniversalAttributes()
  {
    List<UniversalAttributeBinding> list = new LinkedList<UniversalAttributeBinding>();

    UniversalAttributeBindingQuery query = new UniversalAttributeBindingQuery(new QueryFactory());
    query.WHERE(query.getField().EQ(this));

    OIterator<? extends UniversalAttributeBinding> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        UniversalAttributeBinding binding = iterator.next();

        list.add(binding);
      }

      return list;
    }
    finally
    {
      iterator.close();
    }
  }

  @Override
  protected void populate(TargetField field)
  {
    super.populate(field);

    GeoEntity root = this.getGeoEntity();
    Collection<Term> descendants = GeoEntityUtil.getOrderedDescendants(root.getUniversal(), AllowedIn.CLASS);

    TargetFieldGeoEntity tField = (TargetFieldGeoEntity) field;
    tField.setRoot(root);

    List<UniversalAttributeBinding> attributes = this.getUniversalAttributes();

    Collections.sort(attributes, new UniversalAttributeBindingComparator(descendants));

    for (UniversalAttributeBinding attribute : attributes)
    {
      MdAttribute sourceAttribute = attribute.getSourceAttribute();

      String attributeName = sourceAttribute.getAttributeName();
      String label = sourceAttribute.getDisplayLabel().getValue();
      Universal universal = attribute.getUniversal();

      tField.addUniversalAttribute(attributeName, label, universal);
    }
    
    tField.setUseCoordinatesForLocationAssignment(this.getUseCoordinatesForLocationAssignment());
    tField.setCoordinateObject(this.getLatitudeAttributeName(), this.getLongitudeAttributeName());
  }

  @Override
  public TargetFieldIF getTargetField()
  {
    TargetFieldGeoEntity field = new TargetFieldGeoEntity();

    populate(field);

    return field;
  }
}
