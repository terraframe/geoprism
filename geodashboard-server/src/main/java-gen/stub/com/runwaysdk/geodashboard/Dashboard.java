package com.runwaysdk.geodashboard;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.dashboard.ConfigurationIF;
import com.runwaysdk.geodashboard.dashboard.ConfigurationService;
import com.runwaysdk.geodashboard.dashboard.TermComparator;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;
import com.runwaysdk.geodashboard.gis.persist.DashboardLayer;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionQuery;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierAllPathsTableQuery;
import com.runwaysdk.geodashboard.ontology.ClassifierAttributeRoot;
import com.runwaysdk.geodashboard.ontology.ClassifierAttributeRootQuery;
import com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship;
import com.runwaysdk.geodashboard.ontology.ClassifierQuery;
import com.runwaysdk.geodashboard.report.ReportItemQuery;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.CONCAT;
import com.runwaysdk.query.Coalesce;
import com.runwaysdk.query.F;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.SelectableChar;
import com.runwaysdk.query.ValueQuery;
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

    DashboardMap map = clone.getMap();

    // Clone the layer definitions
    DashboardLayer[] layers = this.getMap().getOrderedLayers();

    for (DashboardLayer layer : layers)
    {
      layer.clone(map);
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

            List<Term> list = children.getAll();

            Collections.sort(list, new TermComparator());

            for (Term term : list)
            {
              Classifier child = (Classifier) term;

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
    Set<String> suggestions = new TreeSet<String>();
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId);
    Universal universal = Universal.get(universalId);
    AllAggregationType aggregationType = AllAggregationType.valueOf(aggregationVal);
    String attributeName = mdAttribute.definesAttribute();

    ValueQuery query = QueryUtil.getThematicValueQuery(new QueryFactory(), mdAttribute, aggregationType, universal, Arrays.asList(conditions));

    OIterator<ValueObject> iterator = null;

    try
    {
      iterator = query.getIterator();

      while (iterator.hasNext())
      {
        ValueObject object = iterator.next();

        String value = object.getValue(attributeName);

        if (value.toLowerCase().contains(text.toLowerCase()))
        {
          suggestions.add(value);
        }
      }
    }
    finally
    {
      if (iterator != null)
      {
        iterator.close();
      }
    }

    String[] array = suggestions.toArray(new String[suggestions.size()]);

    return Arrays.copyOf(array, Math.min(limit, array.length));
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
