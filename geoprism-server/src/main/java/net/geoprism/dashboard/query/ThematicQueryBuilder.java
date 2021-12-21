/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.dashboard.query;

import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.query.AggregateFunction;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeChar;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.F;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.OrderBy;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableBoolean;
import com.runwaysdk.query.SelectableIndicator;
import com.runwaysdk.query.SelectableMoment;
import com.runwaysdk.query.SelectableNumber;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;

import net.geoprism.QueryUtil;
import net.geoprism.dashboard.AllAggregationType;
import net.geoprism.dashboard.DashboardStyle;
import net.geoprism.dashboard.DashboardThematicStyle;
import net.geoprism.dashboard.condition.DashboardAttributeCondition;
import net.geoprism.dashboard.condition.DashboardCondition;
import net.geoprism.dashboard.condition.LocationCondition;
import net.geoprism.dashboard.layer.DashboardThematicLayer;
import net.geoprism.gis.geoserver.GeoserverProperties;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierQuery;

public abstract class ThematicQueryBuilder 
{
  public static final String       LOCATION_ALIAS = "geo_id_00";

  public static final String       LABEL_ALIAS    = "display_label_00";

  private QueryFactory             factory;

  private MdAttributeDAOIF         thematicMdAttribute;

  private DashboardStyle           style;

  private AllAggregationType       aggregation;

  private List<DashboardCondition> conditions;

  public ThematicQueryBuilder(QueryFactory factory, DashboardThematicLayer layer)
  {
    this.factory = factory;
    this.thematicMdAttribute = layer.getMdAttributeDAO();
    this.style = layer.getStyle();
    this.aggregation = layer.getAggregationMethod();
    this.conditions = layer.getConditions();
  }

  public ThematicQueryBuilder(QueryFactory factory, MdAttributeDAOIF thematicMdAttribute, DashboardStyle style, AllAggregationType aggregation, List<DashboardCondition> conditions)
  {
    this.factory = factory;
    this.thematicMdAttribute = thematicMdAttribute;
    this.style = style;
    this.aggregation = aggregation;
    this.conditions = conditions;
  }

  protected abstract SelectableSingle getLabelSelectable(GeneratedComponentQuery query);

  protected abstract Selectable getOidentifierSelectable(GeneratedComponentQuery query);

  protected abstract void initialize(ValueQuery vQuery);

  protected abstract void addLocationCriteria(ValueQuery vQuery, GeneratedComponentQuery componentQuery);

  protected abstract void addLocationCondition(ValueQuery vQuery, GeneratedComponentQuery componentQuery, LocationCondition condition);

  public ValueQuery getThematicValueQuery()
  {

    // IMPORTANT - Everything is going to be a 'thematic layer' in IDE,
    // but we need to define a non-thematic's behavior or even finalize
    // on the semantics of a layer without a thematic attribute...which might
    // not even exist!
    if (style != null && style instanceof DashboardThematicStyle)
    {
      ValueQuery thematicQuery = this.build();

      DashboardThematicStyle tStyle = (DashboardThematicStyle) style;

      MdAttributeDAOIF secondaryMdAttribute = tStyle.getSecondaryAttributeDAO();

      if (secondaryMdAttribute != null)
      {
        AttributeCharacter thematicGeoId = thematicQuery.aCharacter(ThematicQueryBuilder.LOCATION_ALIAS);
        thematicGeoId.setColumnAlias(ThematicQueryBuilder.LOCATION_ALIAS);
        thematicGeoId.setUserDefinedAlias(ThematicQueryBuilder.LOCATION_ALIAS);

        AttributeChar thematicLabel = (AttributeChar) thematicQuery.get(ThematicQueryBuilder.LABEL_ALIAS);
        thematicLabel.setColumnAlias(ThematicQueryBuilder.LABEL_ALIAS);
        thematicLabel.setUserDefinedAlias(ThematicQueryBuilder.LABEL_ALIAS);

        Attribute thematicAttribute = thematicQuery.get(thematicMdAttribute.definesAttribute());
        thematicAttribute.setColumnAlias(thematicMdAttribute.definesAttribute().toLowerCase());

        ValueQuery innerQuery = new ValueQuery(factory);
        innerQuery.SELECT(thematicGeoId, thematicLabel, thematicAttribute);

        if (!secondaryMdAttribute.getOid().equals(thematicMdAttribute.getOid()))
        {
          AllAggregationType secondaryAggregation = tStyle.getSecondaryAttributeAggregationMethod();

          ValueQuery secondaryQuery = this.build(secondaryMdAttribute, secondaryAggregation);

          AttributeCharacter secondaryGeoId = secondaryQuery.aCharacter(ThematicQueryBuilder.LOCATION_ALIAS);
          secondaryGeoId.setColumnAlias(ThematicQueryBuilder.LOCATION_ALIAS);
          secondaryGeoId.setUserDefinedAlias(ThematicQueryBuilder.LOCATION_ALIAS);

          Attribute secondaryAttribute = secondaryQuery.get(secondaryMdAttribute.definesAttribute());
          secondaryAttribute.setColumnAlias(secondaryMdAttribute.definesAttribute());
          secondaryAttribute.setUserDefinedDisplayLabel(secondaryMdAttribute.getDisplayLabel(Session.getCurrentLocale()));

          innerQuery.SELECT(secondaryAttribute);
          innerQuery.WHERE(thematicGeoId.LEFT_JOIN_EQ(secondaryGeoId));
        }

        return innerQuery;
      }
      else
      {
        return thematicQuery;
      }
    }
    else
    {
      return this.build();
    }
  }

  private ValueQuery build()
  {
    return this.build(this.thematicMdAttribute, aggregation);
  }

  private ValueQuery build(MdAttributeDAOIF mdAttribute, AllAggregationType aggregation)
  {
    ValueQuery vQuery = new ValueQuery(factory);

    this.initialize(vQuery);

    MdClassDAOIF mdClass = mdAttribute.definedByClass();

    GeneratedComponentQuery query = QueryUtil.getQuery(mdClass, factory);

    // thematic attribute
    String attributeName = mdAttribute.definesAttribute();
    String displayLabel = mdAttribute.getDisplayLabel(Session.getCurrentLocale());

    Selectable thematicAttr = query.getS(attributeName);
    // use the basic Selectable if no aggregate is selected
    Selectable thematicSel = thematicAttr;

    SelectableSingle label = this.getLabelSelectable(query);
    Selectable oid = this.getOidentifierSelectable(query);

    if (thematicSel instanceof SelectableNumber || thematicSel instanceof SelectableMoment || thematicSel instanceof SelectableBoolean || thematicSel instanceof SelectableIndicator)
    {
      boolean isAggregate = false;

      if (aggregation != null)
      {
        // String func = null;
        if (aggregation == AllAggregationType.SUM)
        {
          // func = "SUM";
          thematicSel = F.SUM(thematicAttr);
        }
        else if (aggregation == AllAggregationType.MIN)
        {
          // func = "MIN";
          thematicSel = F.MIN(thematicAttr);
        }
        else if (aggregation == AllAggregationType.MAX)
        {
          // func = "MAX";
          thematicSel = F.MAX(thematicAttr);
        }
        else if (aggregation == AllAggregationType.AVG)
        {
          // func = "AVG";
          thematicSel = F.AVG(thematicAttr);
        }
        isAggregate = true;
      }

      Integer length = GeoserverProperties.getDecimalLength();
      Integer precision = GeoserverProperties.getDecimalPrecision();

      String sql;

      if (thematicSel instanceof SelectableMoment)
      {
        sql = thematicSel.getSQL();
      }
      else
      {
        sql = thematicSel.getSQL() + "::decimal(" + length + "," + precision + ")";
      }

      if (isAggregate)
      {
        thematicSel = vQuery.aSQLAggregateDouble(thematicSel.getResultAttributeName(), sql, attributeName, displayLabel);
      }
      else
      {
        thematicSel = vQuery.aSQLDouble(thematicSel.getResultAttributeName(), sql, attributeName, displayLabel);
      }

      thematicSel.setColumnAlias(attributeName.toLowerCase());

      this.setCriteriaOnInnerQuery(vQuery, query);

      vQuery.SELECT(thematicSel, label, oid);
    }
    else
    {
      if (aggregation != null)
      {
        OrderBy.SortOrder sortOrder;

        if (aggregation == AllAggregationType.MAJORITY)
        {
          // func = "MAJORITY";
          sortOrder = OrderBy.SortOrder.DESC;
        }
        else
        // (agg == AllAggregationType.MINORITY)
        {
          // func = "MINORITY";
          sortOrder = OrderBy.SortOrder.ASC;
        }

        ValueQuery winFuncQuery = new ValueQuery(factory);

        if (mdAttribute.getMdAttributeConcrete() instanceof MdAttributeTermDAOIF)
        {
          MdAttributeTermDAOIF mdAttributeTermDAOIF = (MdAttributeTermDAOIF) mdAttribute.getMdAttributeConcrete();
          if (mdAttributeTermDAOIF.getReferenceMdBusinessDAO().definesType().equals(Classifier.CLASS))
          {
            AttributeReference thematicTerm = (AttributeReference) thematicAttr;

            ClassifierQuery classifierQ = new ClassifierQuery(winFuncQuery);
            winFuncQuery.WHERE(classifierQ.EQ(thematicTerm));

            thematicAttr = classifierQ.getDisplayLabel().localize();
            thematicAttr.setUserDefinedDisplayLabel(mdAttributeTermDAOIF.getDisplayLabel(Session.getCurrentLocale()));
          }
        }

        thematicSel = F.COUNT(thematicAttr, "COUNT");
        thematicSel.setColumnAlias(attributeName.toLowerCase());
        
        AggregateFunction stringAgg = F.STRING_AGG(thematicAttr, ", ", "AGG").OVER(F.PARTITION_BY(F.COUNT(thematicAttr), oid));
        stringAgg.setUserDefinedDisplayLabel(thematicAttr.getUserDefinedDisplayLabel());

        AggregateFunction rank = query.RANK("RANK").OVER(F.PARTITION_BY(oid), new OrderBy(F.COUNT(thematicAttr), sortOrder));

        winFuncQuery.SELECT_DISTINCT(thematicSel);
        winFuncQuery.SELECT_DISTINCT(stringAgg);
        winFuncQuery.SELECT_DISTINCT(rank);
        winFuncQuery.SELECT_DISTINCT(label);
        winFuncQuery.SELECT_DISTINCT(oid);
        winFuncQuery.GROUP_BY((SelectableSingle) thematicAttr, (SelectableSingle) oid);
        winFuncQuery.ORDER_BY(thematicSel, sortOrder);

        this.setCriteriaOnInnerQuery(winFuncQuery, query);

        Selectable outerThematicSel = winFuncQuery.get("AGG");
        outerThematicSel.setUserDefinedAlias(attributeName);
        outerThematicSel.setColumnAlias(attributeName);

        Selectable outerLabel = winFuncQuery.get(ThematicQueryBuilder.LABEL_ALIAS);
        outerLabel.setUserDefinedAlias(ThematicQueryBuilder.LABEL_ALIAS);
        outerLabel.setColumnAlias(ThematicQueryBuilder.LABEL_ALIAS);

        Selectable outerGeoId = winFuncQuery.get(ThematicQueryBuilder.LOCATION_ALIAS);
        outerGeoId.setColumnAlias(ThematicQueryBuilder.LOCATION_ALIAS);
        outerGeoId.setUserDefinedAlias(ThematicQueryBuilder.LOCATION_ALIAS);

        vQuery.SELECT(outerThematicSel);
        vQuery.SELECT(outerLabel);
        vQuery.SELECT(outerGeoId);
        vQuery.WHERE(winFuncQuery.aSQLAggregateInteger("RANK", rank.getColumnAlias()).EQ(1));
      }

      // Assumes isAggregate is true
    }

    return vQuery;
  }

  private void setCriteriaOnInnerQuery(ValueQuery vQuery, GeneratedComponentQuery componentQuery)
  {
    MdClassDAOIF mdClass = componentQuery.getMdClassIF();

    this.addLocationCriteria(vQuery, componentQuery);

    // Attribute condition filtering (i.e. sales unit is greater than 50)

    if (conditions != null)
    {
      for (DashboardCondition condition : conditions)
      {
        if (condition instanceof DashboardAttributeCondition)
        {
          String mdAttributeId = ( (DashboardAttributeCondition) condition ).getMdAttributeId();

          MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId);
          MdClassDAOIF definedByClass = mdAttribute.definedByClass();

          if (definedByClass.getOid().equals(mdClass.getOid()))
          {
            Selectable attr = componentQuery.getS(mdAttribute.definesAttribute());

            condition.restrictQuery(vQuery, attr);
          }
        }
        else if (condition instanceof LocationCondition)
        {
          this.addLocationCondition(vQuery, componentQuery, (LocationCondition) condition);
        }
      }
    }
  }
}
