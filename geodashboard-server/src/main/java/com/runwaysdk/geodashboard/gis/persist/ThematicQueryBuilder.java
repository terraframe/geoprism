package com.runwaysdk.geodashboard.gis.persist;

import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.QueryUtil;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.LocationCondition;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierQuery;
import com.runwaysdk.query.AggregateFunction;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.F;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.OrderBy;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableMoment;
import com.runwaysdk.query.SelectableNumber;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;

public abstract class ThematicQueryBuilder implements Reloadable
{
  private QueryFactory           factory;

  private DashboardThematicLayer layer;

  public ThematicQueryBuilder(QueryFactory factory, DashboardThematicLayer layer)
  {
    this.factory = factory;
    this.layer = layer;
  }

  protected abstract SelectableSingle getLabelSelectable(GeneratedComponentQuery query);

  protected abstract Selectable getIdentifierSelectable(GeneratedComponentQuery query);

  protected abstract void initialize(ValueQuery vQuery);

  protected abstract void addLocationCriteria(ValueQuery vQuery, GeneratedComponentQuery componentQuery);

  protected abstract void addLocationCondition(ValueQuery vQuery, GeneratedComponentQuery componentQuery, LocationCondition condition);

  protected DashboardThematicLayer getLayer()
  {
    return this.layer;
  }

  public ValueQuery getThematicValueQuery()
  {
    MdAttributeDAOIF thematicMdAttribute = layer.getMdAttributeDAO();
    DashboardStyle style = layer.getStyle();

    // IMPORTANT - Everything is going to be a 'thematic layer' in IDE,
    // but we need to define a non-thematic's behavior or even finalize
    // on the semantics of a layer without a thematic attribute...which might
    // not even exist!
    if (style instanceof DashboardThematicStyle)
    {
      DashboardThematicStyle tStyle = (DashboardThematicStyle) style;

      ValueQuery thematicQuery = this.build();

      MdAttributeDAOIF secondaryMdAttribute = tStyle.getSecondaryAttributeDAO();

      if (secondaryMdAttribute != null)
      {
        AttributeCharacter thematicGeoId = thematicQuery.aCharacter(GeoEntity.GEOID);
        thematicGeoId.setColumnAlias(GeoEntity.GEOID);

        AttributeCharacter thematicLabel = thematicQuery.aCharacter(GeoEntity.DISPLAYLABEL);
        thematicLabel.setColumnAlias(GeoEntity.DISPLAYLABEL);

        Attribute thematicAttribute = thematicQuery.get(thematicMdAttribute.definesAttribute());
        thematicAttribute.setColumnAlias(thematicMdAttribute.definesAttribute());

        ValueQuery innerQuery = new ValueQuery(factory);
        innerQuery.SELECT(thematicGeoId, thematicLabel, thematicAttribute);

        if (!secondaryMdAttribute.getId().equals(thematicMdAttribute.getId()))
        {
          AllAggregationType secondaryAggregation = tStyle.getSecondaryAttributeAggregationMethod();

          ValueQuery secondaryQuery = this.build(secondaryMdAttribute, secondaryAggregation);

          AttributeCharacter secondaryGeoId = secondaryQuery.aCharacter(GeoEntity.GEOID);
          secondaryGeoId.setColumnAlias(GeoEntity.GEOID);

          Attribute secondaryAttribute = secondaryQuery.get(secondaryMdAttribute.definesAttribute());
          secondaryAttribute.setColumnAlias(secondaryMdAttribute.definesAttribute());

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
      return new ValueQuery(factory);
    }
  }

  private ValueQuery build()
  {
    MdAttributeDAOIF mdAttribute = this.layer.getMdAttributeDAO();
    AllAggregationType aggregation = this.layer.getAggregationMethod();

    return this.build(mdAttribute, aggregation);
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

    SelectableSingle thematicAttr = query.get(attributeName);
    // use the basic Selectable if no aggregate is selected
    Selectable thematicSel = thematicAttr;

    SelectableSingle label = this.getLabelSelectable(query);
    Selectable id = this.getIdentifierSelectable(query);

    if (thematicSel instanceof SelectableNumber || thematicSel instanceof SelectableMoment)
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

      thematicSel.setColumnAlias(attributeName);

      this.setCriteriaOnInnerQuery(vQuery, query);

      vQuery.SELECT(thematicSel, label, id);
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
          }
        }

        thematicSel = F.COUNT(thematicAttr, "COUNT");
        AggregateFunction stringAgg = F.STRING_AGG(thematicAttr, ", ", "AGG").OVER(F.PARTITION_BY(F.COUNT(thematicAttr), id));
        AggregateFunction rank = query.RANK("RANK").OVER(F.PARTITION_BY(id), new OrderBy(F.COUNT(thematicAttr), sortOrder));

        winFuncQuery.SELECT_DISTINCT(thematicSel);
        winFuncQuery.SELECT_DISTINCT(stringAgg);
        winFuncQuery.SELECT_DISTINCT(rank);
        winFuncQuery.SELECT_DISTINCT(label);
        winFuncQuery.SELECT_DISTINCT(id);
        winFuncQuery.GROUP_BY(thematicAttr, (SelectableSingle) id);
        winFuncQuery.ORDER_BY(thematicSel, sortOrder);

        this.setCriteriaOnInnerQuery(winFuncQuery, query);

        Selectable outerThematicSel = winFuncQuery.get("AGG");
        outerThematicSel.setUserDefinedAlias(attributeName);
        outerThematicSel.setColumnAlias(attributeName);

        Selectable outerLabel = winFuncQuery.get(GeoEntity.DISPLAYLABEL);
        outerLabel.setUserDefinedAlias(GeoEntity.DISPLAYLABEL);
        outerLabel.setColumnAlias(GeoEntity.DISPLAYLABEL);

        Selectable outerGeoId = winFuncQuery.get(GeoEntity.GEOID);
        outerGeoId.setColumnAlias(GeoEntity.GEOID);
        outerGeoId.setUserDefinedAlias(GeoEntity.GEOID);

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
    List<DashboardCondition> conditions = this.layer.getConditions();

    this.addLocationCriteria(vQuery, componentQuery);

    // Attribute condition filtering (i.e. sales unit is greater than 50)

    if (conditions != null)
    {
      for (DashboardCondition condition : conditions)
      {
        if (condition instanceof DashboardAttributeCondition)
        {
          String mdAttributeId = ( (DashboardAttributeCondition) condition ).getDefiningMdAttributeId();

          MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId);
          MdClassDAOIF definedByClass = mdAttribute.definedByClass();

          if (definedByClass.getId().equals(mdClass.getId()))
          {
            Attribute attr = componentQuery.get(mdAttribute.definesAttribute());

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
