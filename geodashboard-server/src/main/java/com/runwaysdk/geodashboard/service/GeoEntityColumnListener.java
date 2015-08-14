package com.runwaysdk.geodashboard.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.io.ExcelExportListener;
import com.runwaysdk.dataaccess.io.excel.ExcelAdapter;
import com.runwaysdk.dataaccess.io.excel.ExcelColumn;
import com.runwaysdk.dataaccess.io.excel.ExcelUtil;
import com.runwaysdk.dataaccess.io.excel.ImportListener;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.GeoEntityUtil;
import com.runwaysdk.geodashboard.KeyGeneratorIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityProblem;
import com.runwaysdk.system.gis.geo.GeoEntityProblemType;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.LocatedInQuery;
import com.runwaysdk.system.gis.geo.SynonymQuery;
import com.runwaysdk.system.gis.geo.Universal;

public class GeoEntityColumnListener extends ExcelAdapter implements ExcelExportListener, ImportListener, Reloadable
{
  public static final String        DELIMETER = "-";

  private MdAttributeReferenceDAOIF mdAttributeReference;

  private String                    attributeName;

  private KeyGeneratorIF            generator;

  private GeoEntity                 entity;

  private HashMap<String, Integer>  map;

  public GeoEntityColumnListener(MdAttributeReferenceDAOIF _mdAttributeReference, GeoEntity _entity, KeyGeneratorIF _generator)
  {
    this.mdAttributeReference = _mdAttributeReference;
    this.entity = _entity;
    this.generator = _generator;
    this.attributeName = this.mdAttributeReference.definesAttribute();

    Collection<Term> children = GeoEntityUtil.getOrderedDescendants(this.entity.getUniversal(), AllowedIn.CLASS);

    this.map = new HashMap<String, Integer>();

    int i = 0;

    for (Term child : children)
    {
      this.map.put(child.getKey(), i++);
    }

  }

  @Override
  public void addColumns(List<ExcelColumn> extraColumns)
  {
    Locale locale = Session.getCurrentLocale();
    String attributeLabel = this.mdAttributeReference.getDisplayLabel(locale);

    Collection<Term> children = GeoEntityUtil.getOrderedDescendants(this.entity.getUniversal(), AllowedIn.CLASS);

    for (Term child : children)
    {
      Universal universal = (Universal) child;

      String columnName = attributeName + DELIMETER + universal.getKey();
      String label = attributeLabel + " (" + universal.getDisplayLabel().getValue(locale) + ")";
      extraColumns.add(new GeoEntityColumn(columnName, label, universal));
    }
  }

  @Override
  public void handleExtraColumns(Mutable instance, List<ExcelColumn> extraColumns, Row row) throws Exception
  {
    Map<LocationColumn, String> map = new HashMap<LocationColumn, String>();

    for (ExcelColumn column : extraColumns)
    {
      String header = column.getAttributeName();

      if (header.startsWith(this.attributeName + DELIMETER))
      {
        String[] matcher = header.split(DELIMETER);
        String attributeName = matcher[0];
        String universalKey = matcher[1];

        Cell cell = row.getCell(column.getIndex());
        String value = ExcelUtil.getString(cell);

        Integer index = this.map.get(universalKey);

        map.put(new LocationColumn(attributeName, universalKey, index), value);
      }
    }

    GeoEntity location = this.getOrCreateLocation(this.entity, map);

    if (location != null)
    {
      instance.setValue(this.attributeName, location.getId());
    }
  }

  /**
   * 
   * 
   * @param locations
   * @return
   */
  private GeoEntity getOrCreateLocation(GeoEntity root, Map<LocationColumn, String> locations)
  {
    List<LocationColumn> columns = new LinkedList<LocationColumn>(locations.keySet());

    Collections.sort(columns);

    Universal rootUniversal = root.getUniversal();

    GeoEntity parent = root;

    for (LocationColumn column : columns)
    {
      String universal = column.getUniversalType();
      String label = locations.get(column);

      if (label != null && label.length() > 0)
      {

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
            entity.setUniversal(Universal.getByKey(universal));
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

  private GeoEntity findGeoEntity(GeoEntity parent, String universal, String label)
  {
    QueryFactory factory = new QueryFactory();

    LocatedInQuery lQuery = new LocatedInQuery(factory);
    lQuery.WHERE(lQuery.parentId().EQ(parent.getId()));

    SynonymQuery synonymQuery = new SynonymQuery(factory);
    synonymQuery.WHERE(synonymQuery.getDisplayLabel().localize().EQ(label));

    GeoEntityQuery query = new GeoEntityQuery(factory);
    query.WHERE(query.getUniversal().getKeyName().EQ(universal));
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

  /**
   * Returns the geo id of the
   * 
   * @return
   */
  public String generateGeoId()
  {
    return this.generator.generateKey(this.entity.getGeoId());
  }
}
