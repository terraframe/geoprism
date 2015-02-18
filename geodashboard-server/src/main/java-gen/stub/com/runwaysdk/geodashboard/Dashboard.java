package com.runwaysdk.geodashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
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
import com.runwaysdk.geodashboard.dashboard.DashboardBuilderIF;
import com.runwaysdk.geodashboard.dashboard.DashboardService;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.geodashboard.gis.persist.DashboardStyle;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierAllPathsTableQuery;
import com.runwaysdk.geodashboard.ontology.ClassifierAttributeRootQuery;
import com.runwaysdk.geodashboard.ontology.ClassifierQuery;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeBoolean;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.AttributeDate;
import com.runwaysdk.query.AttributeDouble;
import com.runwaysdk.query.AttributeFloat;
import com.runwaysdk.query.AttributeInteger;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.F;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableDecimal;
import com.runwaysdk.query.SelectableDouble;
import com.runwaysdk.query.SelectableFloat;
import com.runwaysdk.query.SelectableNumber;
import com.runwaysdk.query.SelectablePrimitive;
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

    List<DashboardBuilderIF> builders = DashboardService.getDashboardBuilders();

    int i = 0;

    for (DashboardBuilderIF builder : builders)
    {
      dashboard.lock();

      try
      {
        builder.initialize(dashboard, i++);
      }
      finally
      {
        dashboard.unlock();
      }
    }

    return dashboard;
  }

  @Override
  public void apply()
  {

    boolean isNew = isNew();

    if (isNew && this.getMap() == null)
    {
      DashboardMap map = new DashboardMap();
      map.setName(this.getDisplayLabel().getValue());
      map.apply();

      this.setMap(map);
    }

    super.apply();
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
  
  public static String[] getCategoryInputSuggestions(String mdAttributeId, String universalId, String aggregationVal, String text, Integer limit)
  {
	      OIterator<ValueObject> iterator = null;
	      List<String> suggestions = new LinkedList<String>();

	      MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();
	      MdClassDAOIF mdClass = mdAttributeConcrete.definedByClass();
	      QueryFactory factory = new QueryFactory();
	    
    	  ValueQuery innerQuery1 = new ValueQuery(factory);
          GeneratedComponentQuery query1 = QueryUtil.getQuery(mdClass, factory);

          // thematic attribute
          String attributeName = mdAttributeConcrete.definesAttribute();
          String displayLabel = mdAttributeConcrete.getDisplayLabel(Session.getCurrentLocale());

          Attribute thematicAttr = QueryUtil.get(query1, attributeName);
      
          // use the basic Selectable if no aggregate is selected
          Selectable thematicSel = thematicAttr;
          String aggregationId = AllAggregationType.valueOf(aggregationVal).getId();
          AllAggregationType aggregationType = AllAggregationType.get(aggregationId);

          boolean isAggregate = false;
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


          if (thematicSel instanceof SelectableDouble || thematicSel instanceof SelectableDecimal || thematicSel instanceof SelectableFloat)
          {
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
          }

          thematicSel.setColumnAlias(attributeName);

          innerQuery1.SELECT(thematicSel);

	      // geoentity label
          GeoEntityQuery geQ1 = new GeoEntityQuery(innerQuery1);
          Selectable label = geQ1.getDisplayLabel().localize();
          label.setColumnAlias(GeoEntity.DISPLAYLABEL);
          innerQuery1.SELECT(label);

	      // geo id (for uniqueness)
          Selectable geoId1 = geQ1.getGeoId(GeoEntity.GEOID);
          geoId1.setColumnAlias(GeoEntity.GEOID);
          innerQuery1.SELECT(geoId1);
          
          MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId);
          
          MdClassDAO md = (MdClassDAO) mdAttribute.definedByClass();
          MdAttributeDAOIF attr = QueryUtil.getGeoEntityAttribute(md);
          
          // Join the entity's GeoEntity reference with the all paths table
          MdAttributeReferenceDAOIF geoRef = MdAttributeReferenceDAO.get(attr.getId());
          Attribute geoAttr = QueryUtil.get(query1, geoRef.definesAttribute());

          // the entity's GeoEntity should match the all path's child
          GeoEntityAllPathsTableQuery geAllPathsQ = new GeoEntityAllPathsTableQuery(innerQuery1);
          innerQuery1.WHERE(geoAttr.LEFT_JOIN_EQ(geAllPathsQ.getChildTerm()));

          // the displayed GeoEntity should match the all path's parent
          innerQuery1.AND(geAllPathsQ.getParentTerm().EQ(geQ1));

          // make sure the parent GeoEntity is of the proper Universal
          Universal universal = Universal.get(universalId);
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
	        
	          if(!trackingList.contains(value) && value.matches(text+".*"))
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
	    
	    return suggestions.toArray(new String[suggestions.size()]);
  }
  
  
}
