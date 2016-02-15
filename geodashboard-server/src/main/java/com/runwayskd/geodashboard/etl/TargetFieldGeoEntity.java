package com.runwayskd.geodashboard.etl;

import java.util.ArrayList;
import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityProblem;
import com.runwaysdk.system.gis.geo.GeoEntityProblemType;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.LocatedInQuery;
import com.runwaysdk.system.gis.geo.SynonymQuery;
import com.runwaysdk.system.gis.geo.Universal;

public class TargetFieldGeoEntity extends TargetField implements TargetFieldIF
{
  public static class UniversalAttribute
  {
    private String    attributeName;

    private Universal universal;

    public UniversalAttribute(String attributeName, Universal universal)
    {
      this.attributeName = attributeName;
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
  }

  private List<UniversalAttribute> attributes;

  private GeoEntity                root;

  public TargetFieldGeoEntity()
  {
    this.attributes = new ArrayList<UniversalAttribute>();
  }

  public GeoEntity getRoot()
  {
    return root;
  }

  public void setRoot(GeoEntity root)
  {
    this.root = root;
  }

  public List<UniversalAttribute> getUniversalAttributes()
  {
    return attributes;
  }

  public void addUniversalAttribute(String attributeName, Universal universal)
  {
    this.attributes.add(new UniversalAttribute(attributeName, universal));
  }

  @Override
  public Object getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    List<String> labels = new ArrayList<String>();

    for (UniversalAttribute attribute : attributes)
    {
      labels.add(source.getValue(attribute.getAttributeName()));
    }

    this.getOrCreateLocation(this.root, labels);

    return null;
  }

  /**
   * 
   * 
   * @param labels
   * @return
   */
  private GeoEntity getOrCreateLocation(GeoEntity root, List<String> labels)
  {
    Universal rootUniversal = root.getUniversal();

    GeoEntity parent = root;

    for (int i = 0; i < attributes.size(); i++)
    {
      UniversalAttribute attribute = attributes.get(i);
      String label = labels.get(i);

      if (label != null && label.length() > 0)
      {
        Universal universal = attribute.getUniversal();

        if (rootUniversal.getKey().equals(universal))
        {
          parent = root;
        }
        else
        {
          GeoEntity entity = this.findGeoEntity(parent, universal, label);

          if (entity == null)
          {
            entity = new GeoEntity();
            entity.setUniversal(universal);
            entity.setGeoId(this.generateGeoId());
            entity.getDisplayLabel().setDefaultValue(label);
            entity.apply();

            entity.addLink(parent, LocatedIn.CLASS);

            // Create a new geo entity problem
            GeoEntityProblem.createProblems(entity, GeoEntityProblemType.UNMATCHED);
          }

          parent = entity;
        }
      }
    }

    return parent;
  }

  private String generateGeoId()
  {
    return null;
  }

  private GeoEntity findGeoEntity(GeoEntity parent, Universal universal, String label)
  {
    QueryFactory factory = new QueryFactory();

    LocatedInQuery lQuery = new LocatedInQuery(factory);
    lQuery.WHERE(lQuery.parentId().EQ(parent.getId()));

    SynonymQuery synonymQuery = new SynonymQuery(factory);
    synonymQuery.WHERE(synonymQuery.getDisplayLabel().localize().EQ(label));

    GeoEntityQuery query = new GeoEntityQuery(factory);
    query.WHERE(query.getUniversal().EQ(universal));
    query.AND(query.locatedIn(lQuery));
    query.AND(OR.get(query.getDisplayLabel().localize().EQ(label), query.synonym(synonymQuery)));

    OIterator<? extends GeoEntity> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        GeoEntity entity = iterator.next();

        if (iterator.hasNext())
        {
          // TODO Give a better error message
          
          throw new RuntimeException("Ambigious entity with the label [" + label + "] and universal [" + universal + "]");
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
}
