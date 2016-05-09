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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.geoprism.KeyGeneratorIF;
import net.geoprism.ontology.GeoEntityUtil;

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
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.generation.loader.Reloadable;
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
import com.runwaysdk.system.gis.geo.SynonymQuery;
import com.runwaysdk.system.gis.geo.Universal;

public class GeoEntityColumnListener extends ExcelAdapter implements ExcelExportListener, ImportListener, Reloadable
{
  public static final String        DELIMETER = "-#-#-";

  private MdAttributeReferenceDAOIF mdAttributeReference;

  private String                    attributeName;

  private KeyGeneratorIF            generator;

  private GeoEntity                 entity;

  public GeoEntityColumnListener(MdAttributeReferenceDAOIF _mdAttributeReference, GeoEntity _entity, KeyGeneratorIF _generator)
  {
    this.mdAttributeReference = _mdAttributeReference;
    this.entity = _entity;
    this.generator = _generator;
    this.attributeName = this.mdAttributeReference.definesAttribute();
  }

  @Override
  public void addColumns(List<ExcelColumn> _extraColumns)
  {
    Locale locale = Session.getCurrentLocale();
    String attributeLabel = this.mdAttributeReference.getDisplayLabel(locale);

    Collection<Term> children = GeoEntityUtil.getOrderedDescendants(this.entity.getUniversal(), AllowedIn.CLASS);

    int i = 0;

    for (Term child : children)
    {
      Universal universal = (Universal) child;

      String columnName = attributeName + DELIMETER + universal.getKey() + DELIMETER + ( i++ );
      String label = attributeLabel + " (" + universal.getDisplayLabel().getValue(locale) + ")";
      _extraColumns.add(new GeoEntityColumn(columnName, label, universal));
    }
  }

  @Override
  public void handleExtraColumns(Mutable _instance, List<ExcelColumn> _extraColumns, Row _row) throws Exception
  {
    Map<LocationColumn, String> map = new HashMap<LocationColumn, String>();
    boolean hasValue = false;

    for (ExcelColumn column : _extraColumns)
    {
      String header = column.getAttributeName();

      if (header.startsWith(this.attributeName + DELIMETER))
      {
        String[] matcher = header.split(DELIMETER);
        String attributeName = matcher[0];
        String universalKey = matcher[1];
        Integer index = Integer.parseInt(matcher[2]);
        
        Cell cell = _row.getCell(column.getIndex());
        String value = ExcelUtil.getString(cell);

        if (value != null && value.length() > 0)
        {
          map.put(new LocationColumn(attributeName, universalKey, index), value);

          hasValue = true;
        }
      }
    }

    if (hasValue)
    {
      GeoEntity location = this.getOrCreateLocation(this.entity, map);

      if (location != null)
      {
        _instance.setValue(this.attributeName, location.getId());
      }
    }
  }

  /**
   * 
   * 
   * @param _locations
   * @return
   */
  private GeoEntity getOrCreateLocation(GeoEntity _root, Map<LocationColumn, String> _locations)
  {
    List<LocationColumn> columns = new LinkedList<LocationColumn>(_locations.keySet());

    Collections.sort(columns);

    Universal rootUniversal = _root.getUniversal();

    GeoEntity parent = _root;

    for (LocationColumn column : columns)
    {
      String universal = column.getUniversalType();
      String label = _locations.get(column);

      if (label != null && label.length() > 0)
      {

        if (rootUniversal.getKey().equals(universal))
        {
          parent = _root;
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

  private GeoEntity findGeoEntity(GeoEntity _parent, String _universal, String _label)
  {
    QueryFactory factory = new QueryFactory();

    GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(factory);
    aptQuery.WHERE(aptQuery.getParentTerm().EQ(_parent));

    SynonymQuery synonymQuery = new SynonymQuery(factory);
    synonymQuery.WHERE(synonymQuery.getDisplayLabel().localize().EQ(_label));

    GeoEntityQuery query = new GeoEntityQuery(factory);
    query.WHERE(query.getUniversal().getKeyName().EQ(_universal));
    query.AND(query.EQ(aptQuery.getChildTerm()));
    query.AND(OR.get(query.getDisplayLabel().localize().EQ(_label), query.synonym(synonymQuery)));

    OIterator<? extends GeoEntity> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        GeoEntity entity = iterator.next();

        if (iterator.hasNext())
        {
          String message = "Ambigious entity with the label [" + _label + " (" + _universal + ")] and ancestor [" + _parent.getDisplayLabel().getValue() + "]";

          throw new AmbigiousGeoEntityException(message, _label, _universal);
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
