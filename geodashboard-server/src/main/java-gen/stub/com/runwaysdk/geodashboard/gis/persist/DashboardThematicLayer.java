package com.runwaysdk.geodashboard.gis.persist;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.geodashboard.QueryUtil;
import com.runwaysdk.geodashboard.gis.EmptyLayerInformation;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicLayer;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.LocationCondition;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierQuery;
import com.runwaysdk.geodashboard.util.CollectionUtil;
import com.runwaysdk.query.AggregateFunction;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.F;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.OIterator;
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
import com.runwaysdk.system.metadata.MdAttribute;

public class DashboardThematicLayer extends DashboardThematicLayerBase implements
    com.runwaysdk.generation.loader.Reloadable, ThematicLayer
{
  private static final long serialVersionUID = -810007054;

  public DashboardThematicLayer()
  {
    super();
  }

  @Transaction
  protected void applyWithStyleInTransaction(DashboardStyle style, String mapId,
      DashboardCondition[] conditions)
  {

    boolean isNew = this.isNew();

    super.applyWithStyleInTransaction(style, mapId, conditions);

    if (isNew && style instanceof DashboardThematicStyle)
    {
      DashboardThematicStyle tStyle = (DashboardThematicStyle) style;

      MdAttribute md = MdAttribute.get(tStyle.getMdAttributeId());
      this.setMdAttribute(md);
    }
  }

  /**
   * Gets the min and max values of a data set to be used for styling based data
   * distributions
   */
  public HashMap<String, Double> getLayerMinMax(String _attribute)
  {

    HashMap<String, Double> minMaxMap = new HashMap<String, Double>();

    QueryFactory f = new QueryFactory();
    ValueQuery wrapper = new ValueQuery(f);
    wrapper.FROM(getViewName(), "");

    List<Selectable> selectables = new LinkedList<Selectable>();
    AllLayerType layerType = this.getLayerType().get(0);
    if (layerType == AllLayerType.BUBBLE || layerType == AllLayerType.GRADIENT)
    {

      selectables.add(wrapper.aSQLAggregateDouble("min_data", "MIN(" + _attribute + ")"));
      selectables.add(wrapper.aSQLAggregateDouble("max_data", "MAX(" + _attribute + ")"));
    }

    selectables.add(wrapper.aSQLAggregateLong("totalResults", "COUNT(*)"));

    wrapper.SELECT(selectables.toArray(new Selectable[selectables.size()]));

    OIterator<? extends ValueObject> iter = wrapper.getIterator();
    try
    {
      ValueObject row = iter.next();

      String min = row.getValue("min_data");
      String max = row.getValue("max_data");

      CollectionUtil.populateMap(minMaxMap, "min", min, new Double(0));
      CollectionUtil.populateMap(minMaxMap, "max", max, new Double(0));

      return minMaxMap;
    }
    finally
    {
      iter.close();
    }
  }

  public JSONObject toJSON()
  {
    try
    {
      JSONObject json = new JSONObject();
      json.put("viewName", getViewName());
      json.put("sldName", getSLDName());
      json.put("layerName", getName());
      json.put("layerId", getId());
      json.put("inLegend", this.getDisplayInLegend());
      json.put("legendXPosition", this.getDashboardLegend().getLegendXPosition());
      json.put("legendYPosition", this.getDashboardLegend().getLegendYPosition());
      json.put("groupedInLegend", this.getDashboardLegend().getGroupedInLegend());
      json.put("featureStrategy", getFeatureStrategy());
      json.put("mdAttributeId", this.getMdAttributeId());

      // Getting the aggregation method (i.e. avg, sum, min, max) and
      // aggregation attribute
      // (i.e. numberofunits) for the style representation
      OIterator<? extends DashboardStyle> iter = this.getAllHasStyle();
      try
      {
        while (iter.hasNext())
        {
          DashboardStyle style = iter.next();
          DashboardThematicStyle tStyle = (DashboardThematicStyle) style;
          String aggregationAttribute = tStyle.getAttribute();
          List<AllAggregationType> allAgg = this.getAggregationType();
          AllAggregationType aggregationMethod = allAgg.get(0);

          json.put("aggregationMethod", aggregationMethod);
          json.put("aggregationAttribute", aggregationAttribute);
        }
      }
      finally
      {
        iter.close();
      }

      JSONArray jsonStyles = new JSONArray();
      List<? extends DashboardStyle> styles = this.getStyles();
      for (int i = 0; i < styles.size(); ++i)
      {
        DashboardStyle style = styles.get(i);
        jsonStyles.put(style.toJSON());
      }
      json.put("styles", jsonStyles);

      return json;
    }
    catch (JSONException ex)
    {
      log.error("Could not properly form DashboardLayer [" + this.toString()
          + "] into valid JSON to send back to the client.");
      throw new ProgrammingErrorException(ex);
    }
  }

  /**
   * @prerequisite conditions is populated with any DashboardConditions
   *               necessary for restricting the view dataset.
   * 
   * @return A ValueQuery for use in creating/dropping the database view which
   *         will be used with GeoServer.
   */
  public ValueQuery getViewQuery()
  {
    QueryFactory factory = new QueryFactory();
    ValueQuery innerQuery1 = new ValueQuery(factory);
    ValueQuery innerQuery2 = new ValueQuery(factory);

    ValueQuery outerQuery = new ValueQuery(factory);

    OIterator<? extends DashboardStyle> iter = this.getAllHasStyle();
    try
    {
      while (iter.hasNext())
      {
        DashboardStyle style = iter.next();

        // IMPORTANT - Everything is going to be a 'thematic layer' in IDE,
        // but we need to define a non-thematic's behavior or even finalize
        // on the semantics of a layer without a thematic attribute...which
        // might
        // not even exist!
        if (style instanceof DashboardThematicStyle)
        {
          DashboardThematicStyle tStyle = (DashboardThematicStyle) style;
          String attribute = tStyle.getAttribute();
          MdAttributeDAOIF mdAttributeDAOIF = tStyle.getMdAttributeDAO();

          MdClassDAOIF mdClass = mdAttributeDAOIF.definedByClass();

          GeneratedComponentQuery query = QueryUtil.getQuery(mdClass, factory);

          // thematic attribute
          String attributeName = mdAttributeDAOIF.definesAttribute();
          String displayLabel = mdAttributeDAOIF.getDisplayLabel(Session.getCurrentLocale());

          SelectableSingle thematicAttr = QueryUtil.get(query, attributeName);
          // use the basic Selectable if no aggregate is selected
          Selectable thematicSel = thematicAttr;

          // geoentity label
          GeoEntityQuery geQ1 = new GeoEntityQuery(innerQuery1);
          SelectableSingle label = geQ1.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);
          label.setColumnAlias(GeoEntity.DISPLAYLABEL);

          // geo id (for uniqueness)
          Selectable geoId1 = geQ1.getGeoId(GeoEntity.GEOID);
          geoId1.setColumnAlias(GeoEntity.GEOID);

          List<AllAggregationType> allAgg = this.getAggregationType();
          boolean isAggregate = false;

          if (thematicSel instanceof SelectableNumber || thematicSel instanceof SelectableMoment)
          {
            if (allAgg.size() == 1)
            {
              AllAggregationType agg = allAgg.get(0);
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
              thematicSel = innerQuery1.aSQLAggregateDouble(thematicSel.getResultAttributeName(), sql,
                  attributeName, displayLabel);
            }
            else
            {
              thematicSel = innerQuery1.aSQLDouble(thematicSel.getResultAttributeName(), sql,
                  attributeName, displayLabel);
            }

            thematicSel.setColumnAlias(attribute);

            this.setCriteriaOnInnerQuery(innerQuery1, mdClass, query, geQ1);

            innerQuery1.SELECT(thematicSel);
            innerQuery1.SELECT(label);
            innerQuery1.SELECT(geoId1);

          }
          else
          {
            if (allAgg.size() == 1)
            {
              AllAggregationType agg = allAgg.get(0);

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

              isAggregate = true;

              ValueQuery winFuncQuery = new ValueQuery(factory);

              if (mdAttributeDAOIF.getMdAttributeConcrete() instanceof MdAttributeTermDAOIF)
              {
                MdAttributeTermDAOIF mdAttributeTermDAOIF = (MdAttributeTermDAOIF) mdAttributeDAOIF
                    .getMdAttributeConcrete();
                if (mdAttributeTermDAOIF.getReferenceMdBusinessDAO().definesType()
                    .equals(Classifier.CLASS))
                {
                  AttributeReference thematicTerm = (AttributeReference) thematicAttr;

                  ClassifierQuery classifierQ = new ClassifierQuery(winFuncQuery);
                  winFuncQuery.WHERE(classifierQ.EQ(thematicTerm));
                  thematicAttr = classifierQ.getDisplayLabel().localize();
                }
              }

              thematicSel = F.COUNT(thematicAttr, "COUNT");
              AggregateFunction stringAgg = F.STRING_AGG(thematicAttr, ", ", "AGG").OVER(
                  F.PARTITION_BY(F.COUNT(thematicAttr), geoId1));
              AggregateFunction rank = query.RANK("RANK").OVER(F.PARTITION_BY(geoId1),
                  new OrderBy(F.COUNT(thematicAttr), sortOrder));

              winFuncQuery.SELECT_DISTINCT(thematicSel);
              winFuncQuery.SELECT_DISTINCT(stringAgg);
              winFuncQuery.SELECT_DISTINCT(label);
              winFuncQuery.SELECT_DISTINCT(rank);
              winFuncQuery.SELECT_DISTINCT(geoId1);
              winFuncQuery.GROUP_BY(thematicAttr, (SelectableSingle) geoId1);
              winFuncQuery.ORDER_BY(thematicSel, sortOrder);

              this.setCriteriaOnInnerQuery(winFuncQuery, mdClass, query, geQ1);

              Selectable outerThematicSel = winFuncQuery.get("AGG");
              outerThematicSel.setUserDefinedAlias("AGG");
              outerThematicSel.setColumnAlias(attribute);

              Selectable outerLabel = winFuncQuery.get(GeoEntity.DISPLAYLABEL);
              outerLabel.setUserDefinedAlias(GeoEntity.DISPLAYLABEL);
              outerLabel.setColumnAlias(GeoEntity.DISPLAYLABEL);

              Selectable outerGeoId = winFuncQuery.get(GeoEntity.GEOID);
              outerGeoId.setColumnAlias(GeoEntity.GEOID);
              outerGeoId.setUserDefinedAlias(GeoEntity.GEOID);

              innerQuery1.SELECT(outerThematicSel);
              innerQuery1.SELECT(outerLabel);
              innerQuery1.SELECT(outerGeoId);
              innerQuery1.WHERE(winFuncQuery.aSQLAggregateInteger("RANK", rank.getColumnAlias()).EQ(1));
            }

            // Assumes isAggregate is true
          }

        } // if (style instanceof DashboardThematicStyle)
      } // while (iter.hasNext())
    }
    finally
    {
      iter.close();
    }

    if (log.isDebugEnabled())
    {
      // print the SQL if the generated
      log.debug("SLD for Layer [%s], this:\n" + innerQuery1.getSQL());
    }

    this.viewHasData = true;
    if (innerQuery1.getCount() == 0)
    {
      EmptyLayerInformation info = new EmptyLayerInformation();
      info.setLayerName(this.getName());
      info.apply();

      info.throwIt();

      this.viewHasData = false;
    }

    // Set the GeoID and the Geometry attribute for the second query
    GeoEntityQuery geQ2 = new GeoEntityQuery(innerQuery2);
    Selectable geoId2 = geQ2.getGeoId(GeoEntity.GEOID);
    geoId2.setColumnAlias(GeoEntity.GEOID);
    innerQuery2.SELECT(geoId2);
    // geometry
    Selectable geom;
    if (this.getFeatureType().equals(FeatureType.POINT))
    {
      geom = geQ2.get(GeoEntity.GEOPOINT);
    }
    else
    {
      geom = geQ2.get(GeoEntity.GEOMULTIPOLYGON);
    }

    geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);
    innerQuery2.SELECT(geom);

    for (Selectable selectable : innerQuery1.getSelectableRefs())
    {
      Attribute attribute = innerQuery1.get(selectable.getResultAttributeName());
      attribute.setColumnAlias(selectable.getColumnAlias());

      outerQuery.SELECT(attribute);
    }

    Attribute geomAttribute = innerQuery2.get(GeoserverFacade.GEOM_COLUMN);
    geomAttribute.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    outerQuery.SELECT(geomAttribute);
    outerQuery
        .WHERE(innerQuery2.aCharacter(GeoEntity.GEOID).EQ(innerQuery1.aCharacter(GeoEntity.GEOID)));

    return outerQuery;
  }

  private void setCriteriaOnInnerQuery(ValueQuery innerQuery1, MdClassDAOIF mdClass,
      GeneratedComponentQuery query, GeoEntityQuery geQ1)
  {
    // Join the entity's GeoEntity reference with the all paths table
    MdAttributeReferenceDAOIF geoRef = MdAttributeReferenceDAO.get(this.getGeoEntityId());
    Attribute geoAttr = QueryUtil.get(query, geoRef.definesAttribute());

    // the entity's GeoEntity should match the all path's child
    GeoEntityAllPathsTableQuery geAllPathsQ = new GeoEntityAllPathsTableQuery(innerQuery1);
    innerQuery1.WHERE(geoAttr.LEFT_JOIN_EQ(geAllPathsQ.getChildTerm()));

    // the displayed GeoEntity should match the all path's parent
    innerQuery1.AND(geAllPathsQ.getParentTerm().EQ(geQ1));

    // make sure the parent GeoEntity is of the proper Universal
    Universal universal = this.getUniversal();
    innerQuery1.AND(geQ1.getUniversal().EQ(universal));

    // Attribute condition filtering (i.e. sales unit is greater than 50)
    List<DashboardCondition> conditions = this.getConditions();

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
            Attribute attr = QueryUtil.get(query, mdAttribute.definesAttribute());

            condition.restrictQuery(innerQuery1, attr);
          }
        }
        else if (condition instanceof LocationCondition)
        {
          condition.restrictQuery(innerQuery1, geoAttr);
        }
      }
    }
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

}
