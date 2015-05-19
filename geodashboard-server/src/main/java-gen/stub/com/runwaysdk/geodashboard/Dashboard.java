package com.runwaysdk.geodashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
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
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.RolesQuery;
import com.runwaysdk.system.gis.geo.AllowedIn;
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
    if (!GeodashboardUser.hasAccess(AccessConstants.ADMIN))
    {
      GeodashboardUser currentUser = GeodashboardUser.getCurrentUser();

      QueryFactory f = new QueryFactory();

      GeodashboardUserQuery userQuery = new GeodashboardUserQuery(f);
      userQuery.WHERE(userQuery.getId().EQ(currentUser.getId()));

      RolesQuery rolesQuery = new RolesQuery(f);
      rolesQuery.WHERE(rolesQuery.singleActor(userQuery));

      DashboardQuery q = new DashboardQuery(f);
      q.WHERE(q.getDashboardRole().EQ(rolesQuery));

      q.ORDER_BY_ASC(q.getDisplayLabel().localize());

      return q;
    }
    else
    {
      QueryFactory f = new QueryFactory();
      DashboardQuery q = new DashboardQuery(f);

      q.ORDER_BY_ASC(q.getDisplayLabel().localize());

      return q;
    }
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

    if (this.isNew() && !this.isAppliedToDB())
    {
      String dashboardLabel = this.getDisplayLabel().getValue();
      String roleName = dashboardLabel.replaceAll("\\s", "");

      // Create the Dashboard Role
      Roles role = new Roles();
      role.setRoleName(RoleView.DASHBOARD_NAMESPACE + "." + roleName);
      role.getDisplayLabel().setValue(dashboardLabel);
      role.apply();

      this.setDashboardRole(role);
    }

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

    // Clone the global conditions
    DashboardCondition[] conditions = this.getConditions((GeodashboardUser) null);

    for (DashboardCondition condition : conditions)
    {
      condition.clone(clone);
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

  public static Dashboard[] getDashboardsForCountry(GeoEntity country)
  {
    DashboardQuery query = new DashboardQuery(new QueryFactory());
    query.WHERE(query.getCountry().EQ(country));

    OIterator<? extends Dashboard> it = query.getIterator();

    try
    {
      List<? extends Dashboard> dashboards = it.getAll();

      return dashboards.toArray(new Dashboard[dashboards.size()]);
    }
    finally
    {
      it.close();
    }
  }

  public ValueQuery getGeoEntitySuggestions(String text, Integer limit)
  {
    ValueQuery query = new ValueQuery(new QueryFactory());

    GeoEntity country = this.getCountry();

    GeoEntityQuery entityQuery = new GeoEntityQuery(query);
    GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(query);

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
    query.AND(aptQuery.getParentTerm().EQ(country));
    query.AND(entityQuery.EQ(aptQuery.getChildTerm()));

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
    GeodashboardUser user = GeodashboardUser.getCurrentUser();

    DashboardCondition[] conditions = this.getConditions(user);

    // There are no user specific conditions, return the global conditions
    if (conditions.length == 0)
    {
      conditions = this.getConditions((GeodashboardUser) null);
    }

    return conditions;
  }

  private DashboardCondition[] getConditions(GeodashboardUser user)
  {
    DashboardConditionQuery query = new DashboardConditionQuery(new QueryFactory());
    query.WHERE(query.getDashboard().EQ(this));

    if (user != null)
    {
      query.AND(query.getGeodashboardUser().EQ(user));
    }
    else
    {
      query.AND(query.getGeodashboardUser().EQ((String) null));
    }

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
    GeodashboardUser user = GeodashboardUser.getCurrentUser();

    DashboardCondition[] existing = this.getConditions(user);

    for (DashboardCondition condition : existing)
    {
      condition.delete();
    }

    for (DashboardCondition condition : conditions)
    {
      condition.setDashboard(this);
      condition.setGeodashboardUser(user);
      condition.apply();
    }
  }

  @Override
  @Transaction
  public void applyGlobalConditions(DashboardCondition[] conditions)
  {
    /*
     * First delete any conditions which exist
     */
    DashboardCondition[] existing = this.getConditions((GeodashboardUser) null);

    for (DashboardCondition condition : existing)
    {
      condition.delete();
    }

    for (DashboardCondition condition : conditions)
    {
      condition.setDashboard(this);
      condition.setGeodashboardUser(null);
      condition.apply();
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

  @Override
  public Boolean hasAccess()
  {
    GeodashboardUser currentUser = GeodashboardUser.getCurrentUser();

    Boolean access = currentUser.isAssigned(this.getDashboardRole());

    if (!access)
    {
      return GeodashboardUser.hasAccess(AccessConstants.ADMIN);
    }

    return true;
  }
  
  @Override
  public String getAllDashboardUsers()
  {
    JSONArray usersArr = new JSONArray();
    
    GeodashboardUser[] gdUsers = GeodashboardUser.getAllUsers();
    for(int i=0; i<gdUsers.length; i++)
    {
      JSONObject userObj = new JSONObject();
      
      GeodashboardUser user = gdUsers[i];
      boolean hasAccess = this.userHasAccess(user);
      Boolean inactive = user.getInactive();
      
      if(!inactive)
      {
        try
        {
          userObj.put("firstName", user.getFirstName());
          userObj.put("lastName", user.getLastName());
          userObj.put("id", user.getId());
          userObj.put("hasAccess", hasAccess);
          usersArr.put(userObj);
        }
        catch (JSONException e)
        {
          String msg = "Could not properly build user json.";
          throw new ProgrammingErrorException(msg, e);
        }
      }
    }
    
    return usersArr.toString();
  }
  
  public static void assignUsers(String dashboardId, String[] userIds)
  {
    Dashboard dashboard = Dashboard.get(dashboardId);
    Roles dbRole = dashboard.getDashboardRole();
    String dbRoleId = dbRole.getId();
    Roles[] allGeodashRoles = RoleView.getGeodashboardRoles();
    
    for(String userJSON : userIds)
    {
      List<String> roleIds = new ArrayList<String>();
      Set<String> set;
      UserDAOIF user = null;
      try
      {
        String userId = null;
        JSONObject userObj = new JSONObject(userJSON);
        
        @SuppressWarnings("unchecked")
        Iterator<String> userObjKeys = userObj.keys();
        while(userObjKeys.hasNext())
        {
          userId = userObjKeys.next().toString();
        }
        
        GeodashboardUser gdUser = GeodashboardUser.get(userId);
        user = UserDAO.get(userId);
        
        boolean assignToDashboard = (Boolean) userObj.get(userId);
        if(assignToDashboard)
        {
          roleIds.add(dbRoleId);
        }
        
        List<? extends Roles> userRoles = gdUser.getAllAssignedRole().getAll();
        for(Roles existingRole : userRoles)
        {
          // filter out roles for this dashboard if it already exists on the user
          // because the dashboard role assignment happens above
          if(!dbRoleId.equals(existingRole.getId()))
          {
            roleIds.add(existingRole.getId());
          }
        }
      }
      catch (JSONException e)
      {
        String msg = "Could not properly parse user json.";
        throw new ProgrammingErrorException(msg, e);
      }
      
      set = new HashSet<String>(roleIds);
      
      /*
       * Assign roles
       */
      for (Roles role : allGeodashRoles)
      {
        RoleDAO roleDAO = RoleDAO.get(role.getId()).getBusinessDAO();
  
        if (set.contains(role.getId()))
        {
          roleDAO.assignMember(user);
        }
        else
        {
          roleDAO.deassignMember(user);
        }
      }
    }
  }
  
  
  private Boolean userHasAccess(GeodashboardUser user)
  {
    Boolean access = user.isAssigned(this.getDashboardRole());

    if (access)
    {
      return true;
    }

    return false;
  }

  public Map<String, Integer> getUniversalIndices()
  {
    Universal universal = this.getCountry().getUniversal();

    List<Term> children = universal.getAllDescendants(AllowedIn.CLASS).getAll();

    Map<String, Integer> indices = new HashMap<String, Integer>();

    int count = 0;

    indices.put(universal.getId(), count++);

    for (Term child : children)
    {
      indices.put(child.getId(), count++);
    }

    return indices;
  }
}
