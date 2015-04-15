package com.runwaysdk.geodashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.dashboard.ConfigurationIF;
import com.runwaysdk.geodashboard.dashboard.ConfigurationService;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionQuery;
import com.runwaysdk.geodashboard.gis.persist.condition.LocationCondition;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierAllPathsTableQuery;
import com.runwaysdk.geodashboard.ontology.ClassifierAttributeRoot;
import com.runwaysdk.geodashboard.ontology.ClassifierAttributeRootQuery;
import com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship;
import com.runwaysdk.geodashboard.ontology.ClassifierQuery;
import com.runwaysdk.geodashboard.report.ReportItemQuery;
import com.runwaysdk.query.AggregateFunction;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.CONCAT;
import com.runwaysdk.query.Coalesce;
import com.runwaysdk.query.F;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OrderBy;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableChar;
import com.runwaysdk.query.SelectableDecimal;
import com.runwaysdk.query.SelectableDouble;
import com.runwaysdk.query.SelectableFloat;
import com.runwaysdk.query.SelectableNumber;
import com.runwaysdk.query.SelectablePrimitive;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdClass;

public class Dashboard extends DashboardBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static class MdClassComparator implements Comparator<MdClass>, Reloadable
  {

    @Override
    public int compare(MdClass m1, MdClass m2)
    {
      return m1.getDisplayLabel().getValue().compareTo(m2.getDisplayLabel().getValue());
    }
  }

  private static final long serialVersionUID = 2043512251;

  public Dashboard()
  {
    super();
  }

  public static DashboardQuery getSortedDashboards()
  {
    QueryFactory f = new QueryFactory();
    DashboardQuery q = new DashboardQuery(f);

    q.ORDER_BY_ASC(q.getDisplayLabel().localize());

    return q;
  }

  @Override
  public void delete()
  {
    for (MetadataWrapper mw : this.getAllMetadata())
    {
      mw.delete();
    }

    super.delete();
  }

  public MdClass[] getSortedTypes()
  {
    // This operation should use only cached objects
    OIterator<? extends MetadataWrapper> iter = this.getAllMetadata();

    List<MdClass> mdClasses = new LinkedList<MdClass>();
    try
    {
      while (iter.hasNext())
      {
        MetadataWrapper mw = iter.next();
        mdClasses.add(mw.getWrappedMdClass());
      }
    }
    finally
    {
      iter.close();
    }

    Collections.sort(mdClasses, new MdClassComparator());

    return mdClasses.toArray(new MdClass[mdClasses.size()]);
  }

  @Transaction
  public static Dashboard create(Dashboard dashboard)
  {
    dashboard.apply();

    List<ConfigurationIF> configurations = ConfigurationService.getConfigurations();

    int i = 0;

    for (ConfigurationIF configuration : configurations)
    {
      dashboard.lock();

      try
      {
        configuration.initialize(dashboard, i++);
      }
      finally
      {
        dashboard.unlock();
      }
    }

    return dashboard;
  }

  @Override
  @Transaction
  public void apply()
  {
    boolean isNew = isNew();

    super.apply();

    if (isNew && this.getMap() == null)
    {
      DashboardMap map = new DashboardMap();
      map.setName(this.getDisplayLabel().getValue());
      map.setDashboard(this);
      map.apply();

      this.setMap(map);
      super.apply();
    }
  }

  @Override
  @Transaction
  public Dashboard clone(String name)
  {
    Dashboard clone = new Dashboard();
    clone.getDisplayLabel().setDefaultValue(name);
    clone.setCountry(this.getCountry());
    clone.apply();

    OIterator<? extends DashboardMetadata> allMetadata = this.getAllMetadataRel();

    try
    {
      while (allMetadata.hasNext())
      {
        DashboardMetadata rel = allMetadata.next();

        DashboardMetadata dm = clone.addMetadata(rel.getChild());
        dm.setListOrder(rel.getListOrder());
        dm.apply();
      }
    }
    finally
    {
      allMetadata.close();
    }

    return clone;
  }

  public static String[] getTextSuggestions(String mdAttributeId, String text, Integer limit)
  {
    List<String> suggestions = new LinkedList<String>();

    MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();
    MdClassDAOIF mdClass = mdAttributeConcrete.definedByClass();

    QueryFactory factory = new QueryFactory();

    BusinessQuery bQuery = factory.businessQuery(mdClass.definesType());
    AttributeCharacter selectable = bQuery.aCharacter(mdAttributeConcrete.definesAttribute());

    ValueQuery query = new ValueQuery(factory);
    query.SELECT_DISTINCT(selectable);
    query.WHERE(selectable.LIKEi("%" + text + "%"));
    query.ORDER_BY_ASC(selectable);
    query.restrictRows(limit, 1);

    OIterator<ValueObject> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        ValueObject object = iterator.next();
        String value = object.getValue(mdAttributeConcrete.definesAttribute());

        suggestions.add(value);
      }
    }
    finally
    {
      iterator.close();
    }

    return suggestions.toArray(new String[suggestions.size()]);
  }

  public static final com.runwaysdk.geodashboard.ontology.Classifier[] getClassifierRoots(String mdAttributeId)
  {
    MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();

    QueryFactory factory = new QueryFactory();

    ClassifierAttributeRootQuery rootQuery = new ClassifierAttributeRootQuery(factory);
    rootQuery.WHERE(rootQuery.getParent().EQ(mdAttributeConcrete.getId()));

    ClassifierQuery classifierQuery = new ClassifierQuery(factory);
    classifierQuery.WHERE(classifierQuery.EQ(rootQuery.getChild()));

    OIterator<? extends Classifier> iterator = classifierQuery.getIterator();

    try
    {
      LinkedList<Classifier> roots = new LinkedList<Classifier>(iterator.getAll());
      return roots.toArray(new Classifier[roots.size()]);
    }
    finally
    {
      iterator.close();
    }
  }

  public static String getClassifierTree(String mdAttributeId)
  {
    MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();
    ClassifierAttributeRootQuery rootQuery = new ClassifierAttributeRootQuery(new QueryFactory());

    rootQuery.WHERE(rootQuery.getParent().EQ(mdAttributeConcrete.getId()));

    OIterator<? extends ClassifierAttributeRoot> iterator = null;

    try
    {
      iterator = rootQuery.getIterator();

      JSONArray nodes = new JSONArray();

      while (iterator.hasNext())
      {
        ClassifierAttributeRoot relationship = iterator.next();
        Classifier classifier = relationship.getChild();

        if (relationship.getSelectable())
        {
          nodes.put(classifier.getJSONObject());
        }
        else
        {
          OIterator<Term> children = null;

          try
          {
            children = classifier.getDirectDescendants(ClassifierIsARelationship.CLASS);

            while (children.hasNext())
            {
              Classifier child = (Classifier) children.next();

              nodes.put(child.getJSONObject());
            }
          }
          finally
          {
            if (children != null)
            {
              children.close();
            }
          }
        }
      }

      return nodes.toString();
    }
    finally
    {
      if (iterator != null)
      {
        iterator.close();
      }
    }
  }

  public static Classifier[] getClassifierSuggestions(String mdAttributeId, String text, Integer limit)
  {
    MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();

    QueryFactory factory = new QueryFactory();
    ClassifierAttributeRootQuery rootQuery = new ClassifierAttributeRootQuery(factory);
    ClassifierQuery classifierQuery = new ClassifierQuery(factory);
    ClassifierAllPathsTableQuery allPathQuery = new ClassifierAllPathsTableQuery(factory);

    rootQuery.WHERE(rootQuery.getParent().EQ(mdAttributeConcrete.getId()));
    allPathQuery.WHERE(allPathQuery.getParentTerm().EQ(rootQuery.getChild()));

    classifierQuery.WHERE(classifierQuery.EQ(allPathQuery.getChildTerm()));
    classifierQuery.AND(classifierQuery.getDisplayLabel().localize().LIKEi("%" + text + "%"));
    classifierQuery.restrictRows(limit, 1);

    OIterator<? extends Classifier> iterator = classifierQuery.getIterator();

    try
    {
      LinkedList<Classifier> suggestions = new LinkedList<Classifier>(iterator.getAll());
      return suggestions.toArray(new Classifier[suggestions.size()]);
    }
    finally
    {
      iterator.close();
    }
  }

  public static String[] getCategoryInputSuggestions(String mdAttributeId, String universalId, String aggregationVal, String text, Integer limit, DashboardCondition[] conditions)
  {
    OIterator<ValueObject> iterator = null;
    List<String> suggestions = new LinkedList<String>();
    Selectable outerThematicSel = null;

    MdAttributeDAOIF mdAttributeConcrete = MdAttributeDAO.get(mdAttributeId);
    MdClassDAOIF mdClass = mdAttributeConcrete.definedByClass();
    QueryFactory factory = new QueryFactory();

    ValueQuery innerQuery1 = new ValueQuery(factory);
    ValueQuery innerQuery2 = new ValueQuery(factory);
    ValueQuery outerQuery = new ValueQuery(factory);
    
    GeneratedComponentQuery query1 = QueryUtil.getQuery(mdClass, factory);

    // thematic attribute
    String attributeName = mdAttributeConcrete.definesAttribute();
    String displayLabel = mdAttributeConcrete.getDisplayLabel(Session.getCurrentLocale());

    SelectableSingle thematicAttr = QueryUtil.get(query1, attributeName);

    // use the basic Selectable if no aggregate is selected
    Selectable thematicSel = thematicAttr;
    
    // geoentity label
    GeoEntityQuery geQ1 = new GeoEntityQuery(innerQuery1);
    Selectable label = geQ1.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);

    // geo id (for uniqueness)
    Selectable geoId1 = geQ1.getGeoId(GeoEntity.GEOID);
    geoId1.setColumnAlias(GeoEntity.GEOID);
    
    Universal universal = Universal.get(universalId);
    
    String aggregationId = AllAggregationType.valueOf(aggregationVal).getId();
    AllAggregationType aggregationType = AllAggregationType.get(aggregationId);
    
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId);
    
    MdClassDAO md = (MdClassDAO) mdAttribute.definedByClass();
    MdAttributeDAOIF attr = QueryUtil.getGeoEntityAttribute(md);

    boolean isAggregate = false;
    if (thematicSel instanceof SelectableNumber)
    {
      if (aggregationType != null)
      {
        // String func = null;
        if (aggregationType == AllAggregationType.SUM)
        {
          // func = "SUM";
          thematicSel = F.SUM(thematicAttr);
        }
        else if (aggregationType == AllAggregationType.MIN)
        {
          // func = "MIN";
          thematicSel = F.MIN(thematicAttr);
        }
        else if (aggregationType == AllAggregationType.MAX)
        {
          // func = "MAX";
          thematicSel = F.MAX(thematicAttr);
        }
        else if (aggregationType == AllAggregationType.AVG)
        {
          // func = "AVG";
          thematicSel = F.AVG(thematicAttr);
        }
        isAggregate = true;
      }

      Integer length = GeoserverProperties.getDecimalLength();
      Integer precision = GeoserverProperties.getDecimalPrecision();

      String sql = thematicSel.getSQL() + "::decimal(" + length + "," + precision + ")";

      if (isAggregate)
      {
        thematicSel = innerQuery1.aSQLAggregateDouble(thematicSel.getResultAttributeName(), sql, attributeName, displayLabel);
      }
      else
      {
        thematicSel = innerQuery1.aSQLDouble(thematicSel.getResultAttributeName(), sql, attributeName, displayLabel);
      }
  
      thematicSel.setColumnAlias(attributeName);
  
      innerQuery1.SELECT(thematicSel);
      innerQuery1.SELECT(label);
      innerQuery1.SELECT(geoId1);
      
      // Join the entity's GeoEntity reference with the all paths table
      MdAttributeReferenceDAOIF geoRef = MdAttributeReferenceDAO.get(attr.getId());
      Attribute geoAttr = QueryUtil.get(query1, geoRef.definesAttribute());
  
      // the entity's GeoEntity should match the all path's child
      GeoEntityAllPathsTableQuery geAllPathsQ = new GeoEntityAllPathsTableQuery(innerQuery1);
      innerQuery1.WHERE(geoAttr.LEFT_JOIN_EQ(geAllPathsQ.getChildTerm()));
      
      // the displayed GeoEntity should match the all path's parent
      innerQuery1.AND(geAllPathsQ.getParentTerm().EQ(geQ1));
  
      // make sure the parent GeoEntity is of the proper Universal
      innerQuery1.AND(geQ1.getUniversal().EQ(universal));
      innerQuery1.ORDER_BY_ASC((SelectablePrimitive) thematicSel);
      
      iterator = innerQuery1.getIterator();

      try
      {
        List<String> trackingList = new ArrayList<String>();
        while (iterator.hasNext())
        {
          ValueObject object = iterator.next();
          String value = object.getValue(thematicSel.getResultAttributeName());

          if (!trackingList.contains(value) && value.matches(text + ".*"))
          {
            suggestions.add(value);
            trackingList.add(value);
          }
        }
      }
      finally
      {
        iterator.close();
      }
    }
    else
    {
      if (aggregationType != null)
      {
        OrderBy.SortOrder sortOrder;
        
        if (aggregationType == AllAggregationType.MAJORITY)
        {
          // func = "MAJORITY";
          sortOrder = OrderBy.SortOrder.DESC;
        }
        else // (agg == AllAggregationType.MINORITY)
        {
          // func = "MINORITY";
          sortOrder = OrderBy.SortOrder.ASC;
        }
        isAggregate = true;
        
        ValueQuery winFuncQuery = new ValueQuery(factory);
        
        thematicSel = F.COUNT(thematicAttr, "COUNT");
        AggregateFunction stringAgg = F.STRING_AGG(thematicAttr, ", ", "AGG").OVER(F.PARTITION_BY(F.COUNT(thematicAttr), geoId1));
        AggregateFunction rank = query1.RANK("RANK").OVER(F.PARTITION_BY(geoId1), new OrderBy(F.COUNT(thematicAttr), sortOrder));
        
        winFuncQuery.SELECT_DISTINCT(thematicSel);  
        winFuncQuery.SELECT_DISTINCT(stringAgg);
        winFuncQuery.SELECT_DISTINCT(label);
        winFuncQuery.SELECT_DISTINCT(rank);
        winFuncQuery.SELECT_DISTINCT(geoId1);
        winFuncQuery.GROUP_BY(thematicAttr, (SelectableSingle)geoId1);
        winFuncQuery.ORDER_BY(thematicSel, sortOrder);

        setCriteriaOnInnerQuery(winFuncQuery, mdClass, query1, geQ1, universal, attr.getId(), thematicAttr, conditions);
        
        outerThematicSel = winFuncQuery.get("AGG");
        outerThematicSel.setUserDefinedAlias("AGG");
        outerThematicSel.setColumnAlias(attributeName);
        
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
        
        // Set the GeoID and the Geometry attribute for the second query
        GeoEntityQuery geQ2 = new GeoEntityQuery(innerQuery2);
        Selectable geoId2 = geQ2.getGeoId(GeoEntity.GEOID);
        geoId2.setColumnAlias(GeoEntity.GEOID);
        innerQuery2.SELECT(geoId2);
        // geometry
        Selectable geom;
        geom = geQ2.get(GeoEntity.GEOMULTIPOLYGON);  // This will always be polygon because categories are only a feature of polygon

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
        outerQuery.WHERE(innerQuery2.aCharacter(GeoEntity.GEOID).EQ(innerQuery1.aCharacter(GeoEntity.GEOID)));
        outerQuery.ORDER_BY_ASC(outerThematicSel);
      }
      
      iterator = outerQuery.getIterator();
      try
      {
        List<String> trackingList = new ArrayList<String>();
        while (iterator.hasNext())
        {
          ValueObject object = iterator.next();
          String value = object.getValue(outerThematicSel.getResultAttributeName());

          // && value.matches(text + ".*")
          if (!trackingList.contains(value) && value.toLowerCase().startsWith(text.toLowerCase()))
          {
            suggestions.add(value);
            trackingList.add(value);
          }
        }
      }
      finally
      {
        iterator.close();
      }
    }
    
    return suggestions.toArray(new String[suggestions.size()]);
  }

  private static void setCriteriaOnInnerQuery(ValueQuery innerQuery1, MdClassDAOIF mdClass, GeneratedComponentQuery query, 
      GeoEntityQuery geQ1, Universal universal, String geoEntId, SelectableSingle thematicAttr, DashboardCondition[] conditions)
  {
    // Join the entity's GeoEntity reference with the all paths table
    MdAttributeReferenceDAOIF geoRef = MdAttributeReferenceDAO.get(geoEntId);
    Attribute geoAttr = QueryUtil.get(query, geoRef.definesAttribute());
  
    // the entity's GeoEntity should match the all path's child
    GeoEntityAllPathsTableQuery geAllPathsQ = new GeoEntityAllPathsTableQuery(innerQuery1);
    innerQuery1.WHERE(geoAttr.LEFT_JOIN_EQ(geAllPathsQ.getChildTerm()));
  
    // the displayed GeoEntity should match the all path's parent
    innerQuery1.AND(geAllPathsQ.getParentTerm().EQ(geQ1));
  
    // make sure the parent GeoEntity is of the proper Universal
    innerQuery1.AND(geQ1.getUniversal().EQ(universal));
    
    
    // Attribute condition filtering (i.e. sales unit is greater than 50)
    if (conditions != null)
    {
      for (DashboardCondition condition : conditions)
      {
        if (condition instanceof DashboardAttributeCondition)
        {
          String condAttributeId = ( (DashboardAttributeCondition) condition ).getDefiningMdAttributeId();
  
          MdAttributeDAOIF condAttribute = MdAttributeDAO.get(condAttributeId);
          MdClassDAOIF definedByClass = condAttribute.definedByClass();
  
          if (definedByClass.getId().equals(mdClass.getId()))
          {
            Attribute attr = QueryUtil.get(query, condAttribute.definesAttribute());

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

  public static ValueQuery getGeoEntitySuggestions(String text, Integer limit)
  {
    ValueQuery query = new ValueQuery(new QueryFactory());

    GeoEntityQuery entityQuery = new GeoEntityQuery(query);

    SelectableChar id = entityQuery.getId();
    Coalesce universalLabel = entityQuery.getUniversal().getDisplayLabel().localize();
    Coalesce geoLabel = entityQuery.getDisplayLabel().localize();
    SelectableChar geoId = entityQuery.getGeoId();

    CONCAT label = F.CONCAT(F.CONCAT(F.CONCAT(F.CONCAT(geoLabel, " ("), F.CONCAT(universalLabel, ")")), " : "), geoId);
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);
    label.setUserDefinedAlias(GeoEntity.DISPLAYLABEL);
    label.setUserDefinedDisplayLabel(GeoEntity.DISPLAYLABEL);

    query.SELECT(id, label);
    query.WHERE(label.LIKEi("%" + text + "%"));
    query.ORDER_BY_ASC(geoLabel);

    query.restrictRows(limit, 1);

    return query;
  }

  @Override
  public Boolean hasReport()
  {
    ReportItemQuery query = new ReportItemQuery(new QueryFactory());
    query.WHERE(query.getDashboard().EQ(this));

    return ( query.getCount() > 0 );
  }

  @Override
  public DashboardCondition[] getConditions()
  {
    DashboardConditionQuery query = new DashboardConditionQuery(new QueryFactory());
    query.WHERE(query.getDashboard().EQ(this));
    query.AND(query.getGeodashboardUser().EQ(GeodashboardUser.getCurrentUser()));

    OIterator<? extends DashboardCondition> iterator = null;

    try
    {
      iterator = query.getIterator();

      List<? extends DashboardCondition> list = iterator.getAll();

      DashboardCondition[] conditions = list.toArray(new DashboardCondition[list.size()]);

      return conditions;
    }
    finally
    {
      if (iterator != null)
      {
        iterator.close();
      }
    }
  }

  @Override
  @Transaction
  public void applyConditions(DashboardCondition[] conditions)
  {
    /*
     * First delete any conditions which exist
     */
    this.deleteConditions();

    GeodashboardUser user = GeodashboardUser.getCurrentUser();

    for (DashboardCondition condition : conditions)
    {
      condition.setDashboard(this);
      condition.setGeodashboardUser(user);
      condition.apply();
    }
  }

  public void deleteConditions()
  {
    DashboardCondition[] conditions = this.getConditions();

    for (DashboardCondition condition : conditions)
    {
      condition.delete();
    }
  }

  @Override
  public String getConditionsJSON()
  {
    JSONArray array = new JSONArray();

    DashboardCondition[] conditions = this.getConditions();

    for (DashboardCondition condition : conditions)
    {
      array.put(condition.getJSON());
    }

    return array.toString();
  }
}
