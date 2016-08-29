/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see
 * <http://www.gnu.org/licenses/>.
 */
package net.geoprism.data.etl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.geoprism.data.importer.ExclusionException;
import net.geoprism.ontology.NonUniqueEntityResultException;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.SynonymQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.util.IDGenerator;

public class TargetFieldGeoEntity extends TargetField implements TargetFieldGeoEntityIF, TargetFieldValidationIF
{
  public static class UniversalAttribute
  {
    private String    attributeName;

    private String    label;

    private Universal universal;

    public UniversalAttribute(String attributeName, String label, Universal universal)
    {
      this.attributeName = attributeName;
      this.label = label;
      this.universal = universal;
    }

    public String getAttributeName()
    {
      return attributeName;
    }

    public void setAttributeName(String attributeName)
    {
      this.attributeName = attributeName;
    }

    public Universal getUniversal()
    {
      return universal;
    }

    public void setUniversal(Universal universal)
    {
      this.universal = universal;
    }

    public String getLabel()
    {
      return label;
    }
  }

  private ArrayList<UniversalAttribute> attributes;

  private String                        id;

  private GeoEntity                     root;

  private Universal                     rootUniversal;

  public TargetFieldGeoEntity()
  {
    this.attributes = new ArrayList<UniversalAttribute>();
    this.id = IDGenerator.nextID();
  }

  public String getId()
  {
    return id;
  }

  public GeoEntity getRoot()
  {
    return root;
  }

  public void setRoot(GeoEntity root)
  {
    this.root = root;
    this.rootUniversal = root.getUniversal();
  }

  public void addUniversalAttribute(String attributeName, String label, Universal universal)
  {
    this.attributes.add(new UniversalAttribute(attributeName, label, universal));
  }

  @Override
  public Map<String, String> getUniversalAttributes()
  {
    Map<String, String> map = new HashMap<String, String>();

    for (UniversalAttribute attribute : this.attributes)
    {
      map.put(attribute.getAttributeName(), attribute.getUniversal().getId());
    }

    return map;
  }

  @Override
  public FieldValue getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    List<String> labels = new ArrayList<String>();

    for (UniversalAttribute attribute : attributes)
    {
      labels.add(source.getValue(attribute.getAttributeName()));
    }

    GeoEntity entity = this.getLocation(this.root, labels);

    return new FieldValue(entity.getId());
  }

  /**
   * 
   * 
   * @param labels
   * @return
   */
  private GeoEntity getLocation(GeoEntity root, List<String> labels)
  {
    GeoEntity parent = root;

    for (int i = 0; i < attributes.size(); i++)
    {
      UniversalAttribute attribute = attributes.get(i);
      String label = labels.get(i);

      if (label != null && label.length() > 0)
      {
        Universal universal = attribute.getUniversal();

        if (rootUniversal.getKey().equals(universal.getKey()))
        {
          parent = root;
        }
        else
        {
          GeoEntity entity = this.findGeoEntity(parent, universal, label);

          if (entity == null)
          {
            throw new ExclusionException("Location not found in system.");
          }

          parent = entity;
        }
      }
    }

    return parent;
  }

  private GeoEntity findGeoEntity(GeoEntity parent, Universal universal, String label)
  {
    QueryFactory factory = new QueryFactory();

    GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(factory);
    aptQuery.WHERE(aptQuery.getParentTerm().EQ(parent));

    SynonymQuery synonymQuery = new SynonymQuery(factory);
    synonymQuery.WHERE(synonymQuery.getDisplayLabel().localize().EQ(label));

    GeoEntityQuery query = new GeoEntityQuery(factory);
    query.WHERE(query.getUniversal().EQ(universal));
    query.AND(query.getId().EQ(aptQuery.getChildTerm().getId()));
    query.AND(OR.get(query.getDisplayLabel().localize().EQ(label), query.synonym(synonymQuery)));

    OIterator<? extends GeoEntity> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        GeoEntity entity = iterator.next();

        if (iterator.hasNext())
        {
          NonUniqueEntityResultException e = new NonUniqueEntityResultException();
          e.setLabel(label);
          e.setUniversal(universal.getDisplayLabel().getValue());
          e.setParent(parent.getDisplayLabel().getValue());

          throw e;
        }

        return entity;
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }

  @Override
  public void persist(TargetBinding binding)
  {
    MdAttribute targetAttribute = MdAttribute.getByKey(this.getKey());

    TargetFieldGeoEntityBinding field = new TargetFieldGeoEntityBinding();
    field.setTarget(binding);
    field.setTargetAttribute(targetAttribute);
    field.setColumnLabel(this.getLabel());
    field.setGeoEntity(this.root);
    field.apply();

    for (UniversalAttribute attribute : this.attributes)
    {
      MdAttribute sourceAttribute = MdAttribute.getByKey(binding.getSourceView().definesType() + "." + attribute.getAttributeName());

      UniversalAttributeBinding uAttribute = new UniversalAttributeBinding();
      uAttribute.setField(field);
      uAttribute.setSourceAttribute(sourceAttribute);
      uAttribute.setUniversal(attribute.getUniversal());
      uAttribute.apply();
    }
  }

  @Override
  public JSONObject toJSON() throws JSONException
  {
    JSONObject fields = new JSONObject();

    for (UniversalAttribute attribute : this.attributes)
    {
      fields.put(attribute.getUniversal().getId(), attribute.getLabel());
    }

    UniversalAttribute attribute = this.attributes.get(this.attributes.size() - 1);

    JSONObject object = new JSONObject();
    object.put("label", this.getLabel());
    object.put("universal", attribute.getUniversal().getId());
    object.put("fields", fields);
    object.put("id", this.id);

    return object;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.geoprism.data.etl.TargetFieldGeoEntityIF#getLocationProblem(com.runwaysdk.business.Transient,
   * com.runwaysdk.system.gis.geo.Universal)
   */
  @SuppressWarnings("unchecked")
  @Override
  public ImportProblemIF validate(Transient source, Map<String, Object> parameters)
  {
    GeoEntity parent = this.root;

    Map<String, Set<String>> locationExclusions = (Map<String, Set<String>>) parameters.get("locationExclusions");

    List<JSONObject> context = new LinkedList<JSONObject>();

    for (UniversalAttribute attribute : attributes)
    {
      String label = source.getValue(attribute.getAttributeName());
      Universal entityUniversal = attribute.getUniversal();

      if (label != null && label.length() > 0)
      {
        if (this.isExcluded(locationExclusions, entityUniversal, parent, label))
        {
          return null;
        }

        if (parent.getUniversalId().equals(entityUniversal.getId()))
        {
          GeoEntity entity = this.findGeoEntity(GeoEntity.getRoot(), entityUniversal, label);

          if (entity == null)
          {
            return new LocationProblem(label, context, GeoEntity.getRoot(), entityUniversal);
          }
        }
        else
        {
          GeoEntity entity = this.findGeoEntity(parent, entityUniversal, label);

          if (entity == null)
          {
            return new LocationProblem(label, context, parent, entityUniversal);
          }
          else
          {
            parent = entity;
          }
        }
      }

      try
      {
        JSONObject object = new JSONObject();
        object.put("label", label);
        object.put("universal", parent.getUniversal().getDisplayLabel().getValue());

        context.add(object);
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return null;
  }

  private boolean isExcluded(Map<String, Set<String>> locationExclusions, Universal universal, GeoEntity parent, String label)
  {
    String key = universal.getId() + "-" + parent.getId();

    if (universal.getId().equals(this.rootUniversal.getId()))
    {
      key = universal.getId() + "-" + GeoEntity.getRoot().getId();
    }

    if (locationExclusions.containsKey(key))
    {
      Set<String> labels = locationExclusions.get(key);

      return labels.contains(label);
    }

    return false;
  }
}
