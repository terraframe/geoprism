package com.runwaysdk.geodashboard;

import java.lang.reflect.Constructor;
import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.LocationCondition;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierQuery;
import com.runwaysdk.query.AggregateFunction;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.F;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.GeneratedEntityQuery;
import com.runwaysdk.query.GeneratedViewQuery;
import com.runwaysdk.query.OrderBy;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableMoment;
import com.runwaysdk.query.SelectableNumber;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;

public class QueryUtil implements Reloadable
{
  public static ValueQuery getThematicValueQuery(QueryFactory factory, MdAttributeDAOIF mdAttribute, AllAggregationType agg, Universal universal, List<DashboardCondition> conditions)
  {
    ValueQuery vQuery = new ValueQuery(factory);

    MdClassDAOIF mdClass = mdAttribute.definedByClass();

    GeneratedComponentQuery query = QueryUtil.getQuery(mdClass, factory);

    // thematic attribute
    String attributeName = mdAttribute.definesAttribute();
    String displayLabel = mdAttribute.getDisplayLabel(Session.getCurrentLocale());

    SelectableSingle thematicAttr = QueryUtil.get(query, attributeName);
    // use the basic Selectable if no aggregate is selected
    Selectable thematicSel = thematicAttr;

    // geoentity label
    GeoEntityQuery geoEntityQuery = new GeoEntityQuery(vQuery);
    SelectableSingle label = geoEntityQuery.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);

    // geo id (for uniqueness)
    Selectable geoId1 = geoEntityQuery.getGeoId(GeoEntity.GEOID);
    geoId1.setColumnAlias(GeoEntity.GEOID);

    if (thematicSel instanceof SelectableNumber || thematicSel instanceof SelectableMoment)
    {
      boolean isAggregate = false;

      if (agg != null)
      {
        // String func = null;
        if (agg == AllAggregationType.SUM)
        {
          // func = "SUM";
          thematicSel = F.SUM(thematicAttr);
        }
        else if (agg == AllAggregationType.MIN)
        {
          // func = "MIN";
          thematicSel = F.MIN(thematicAttr);
        }
        else if (agg == AllAggregationType.MAX)
        {
          // func = "MAX";
          thematicSel = F.MAX(thematicAttr);
        }
        else if (agg == AllAggregationType.AVG)
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

      setCriteriaOnInnerQuery(vQuery, query, geoEntityQuery, universal, conditions);

      vQuery.SELECT(thematicSel);
      vQuery.SELECT(label);
      vQuery.SELECT(geoId1);

    }
    else
    {
      if (agg != null)
      {
        OrderBy.SortOrder sortOrder;

        if (agg == AllAggregationType.MAJORITY)
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
        AggregateFunction stringAgg = F.STRING_AGG(thematicAttr, ", ", "AGG").OVER(F.PARTITION_BY(F.COUNT(thematicAttr), geoId1));
        AggregateFunction rank = query.RANK("RANK").OVER(F.PARTITION_BY(geoId1), new OrderBy(F.COUNT(thematicAttr), sortOrder));

        winFuncQuery.SELECT_DISTINCT(thematicSel);
        winFuncQuery.SELECT_DISTINCT(stringAgg);
        winFuncQuery.SELECT_DISTINCT(label);
        winFuncQuery.SELECT_DISTINCT(rank);
        winFuncQuery.SELECT_DISTINCT(geoId1);
        winFuncQuery.GROUP_BY(thematicAttr, (SelectableSingle) geoId1);
        winFuncQuery.ORDER_BY(thematicSel, sortOrder);

        setCriteriaOnInnerQuery(winFuncQuery, query, geoEntityQuery, universal, conditions);

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

  private static void setCriteriaOnInnerQuery(ValueQuery vQuery, GeneratedComponentQuery componentQuery, GeoEntityQuery geoEntityQuery, Universal universal, List<DashboardCondition> conditions)
  {
    MdClassDAOIF mdClass = componentQuery.getMdClassIF();
    MdAttributeReferenceDAOIF geoRef = QueryUtil.getGeoEntityAttribute(mdClass);

    // Join the entity's GeoEntity reference with the all paths table
    Attribute geoAttr = QueryUtil.get(componentQuery, geoRef.definesAttribute());

    // the entity's GeoEntity should match the all path's child
    GeoEntityAllPathsTableQuery geAllPathsQ = new GeoEntityAllPathsTableQuery(vQuery);
    vQuery.WHERE(geoAttr.LEFT_JOIN_EQ(geAllPathsQ.getChildTerm()));

    // the displayed GeoEntity should match the all path's parent
    vQuery.AND(geAllPathsQ.getParentTerm().EQ(geoEntityQuery));

    // make sure the parent GeoEntity is of the proper Universal
    vQuery.AND(geoEntityQuery.getUniversal().EQ(universal));

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
            Attribute attr = QueryUtil.get(componentQuery, mdAttribute.definesAttribute());

            condition.restrictQuery(vQuery, attr);
          }
        }
        else if (condition instanceof LocationCondition)
        {
          condition.restrictQuery(vQuery, geoAttr);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static GeneratedComponentQuery getQuery(MdClassDAOIF mdClass, QueryFactory factory)
  {
    // Use reflection to generate the view query
    String queryClassName = mdClass.definesType() + "Query";

    try
    {
      Class<GeneratedComponentQuery> clazz = (Class<GeneratedComponentQuery>) LoaderDecorator.loadClass(queryClassName);
      Constructor<GeneratedComponentQuery> constructor = clazz.getConstructor(factory.getClass());
      GeneratedComponentQuery query = constructor.newInstance(factory);

      return query;
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static Attribute get(GeneratedComponentQuery query, String attributeName)
  {
    if (query instanceof GeneratedViewQuery)
    {
      return ( (GeneratedViewQuery) query ).get(attributeName);
    }

    return ( (GeneratedEntityQuery) query ).get(attributeName);
  }

  public static MdAttributeReferenceDAOIF getGeoEntityAttribute(MdClassDAOIF mdClass)
  {
    for (MdAttributeDAOIF mdAttr : mdClass.definesAttributes())
    {
      MdAttributeConcreteDAOIF mdAttributeConcrete = mdAttr.getMdAttributeConcrete();

      if (mdAttributeConcrete instanceof MdAttributeReferenceDAOIF)
      {
        MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttributeConcrete;

        if (mdAttributeReference.getReferenceMdBusinessDAO().definesType().equals(GeoEntity.CLASS))
        {
          return mdAttributeReference;
        }
      }
    }

    return null;
  }
}
