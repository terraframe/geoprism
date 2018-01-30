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
package net.geoprism.dashboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.geoprism.AccessConstants;
import net.geoprism.ClassUniversalQuery;
import net.geoprism.GeoprismUser;
import net.geoprism.GeoprismUserIF;
import net.geoprism.GeoprismUserQuery;
import net.geoprism.KeyGeneratorIF;
import net.geoprism.MappableClass;
import net.geoprism.MappableClassGeoNodeQuery;
import net.geoprism.MappableClassQuery;
import net.geoprism.RoleView;
import net.geoprism.TaskExecutor;
import net.geoprism.dashboard.condition.DashboardCondition;
import net.geoprism.dashboard.condition.LocationCondition;
import net.geoprism.dashboard.layer.DashboardLayer;
import net.geoprism.dashboard.layer.UnsupportedAggregationException;
import net.geoprism.dashboard.query.GeoEntityThematicQueryBuilder;
import net.geoprism.dashboard.query.GeometryThematicQueryBuilder;
import net.geoprism.dashboard.query.ThematicQueryBuilder;
import net.geoprism.data.importer.SeedKeyGenerator;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierAllPathsTableQuery;
import net.geoprism.ontology.ClassifierIsARelationship;
import net.geoprism.ontology.ClassifierQuery;
import net.geoprism.ontology.ClassifierTermAttributeRoot;
import net.geoprism.ontology.ClassifierTermAttributeRootQuery;
import net.geoprism.ontology.GeoEntityUtil;
import net.geoprism.report.ReportItem;
import net.geoprism.report.ReportItemQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.SingleActorDAO;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.generated.system.gis.geo.UniversalAllPathsTableQuery;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.AttributeChar;
import com.runwaysdk.query.CONCAT;
import com.runwaysdk.query.Coalesce;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.F;
import com.runwaysdk.query.MAX;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.SelectableChar;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.ReadPermissionException;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.RolesQuery;
import com.runwaysdk.system.SingleActor;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.AllowedInQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.GeoNodeGeometry;
import com.runwaysdk.system.gis.geo.GeoNodeQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdClass;

public class Dashboard extends DashboardBase implements com.runwaysdk.generation.loader.Reloadable
{

  private static class ThumbnailTask implements Runnable, Reloadable
  {
    private Dashboard     dashboard;

    private SingleActor[] users;

    private String        sessionId;

    public ThumbnailTask(String sessionId, Dashboard dashboard, SingleActor... users)
    {
      this.dashboard = dashboard;
      this.users = users;
      this.sessionId = sessionId;
    }

    @Override
    public void run()
    {
      this.execute(this.sessionId);
    }

    @Request(RequestType.SESSION)
    public void execute(String sessionId)
    {
      dashboard.generateThumbnailImage(users);
    }

  }

  private static final long           serialVersionUID = 2043512251;

  private static final KeyGeneratorIF generator        = new SeedKeyGenerator();

  public Dashboard()
  {
    super();
  }

  @Override
  public String toString()
  {
    return this.getDisplayLabel().getValue();
  }

  public static DashboardQuery getSortedDashboards()
  {
    if (!GeoprismUser.hasAccess(AccessConstants.ADMIN))
    {
      SingleActor currentUser = GeoprismUser.getCurrentUser();

      QueryFactory f = new QueryFactory();

      GeoprismUserQuery userQuery = new GeoprismUserQuery(f);
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
  @Transaction
  public void delete()
  {
    if (this.getRemovable() != null && !this.getRemovable())
    {
      DashboardDeleteException ex = new DashboardDeleteException();
      ex.setLabel(this.getDisplayLabel().getValue());

      throw ex;
    }

    // Delete all saved states
    DashboardStateQuery query = new DashboardStateQuery(new QueryFactory());
    query.WHERE(query.getDashboard().EQ(this));

    OIterator<? extends DashboardState> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        DashboardState state = it.next();
        state.delete();
      }
    }
    finally
    {
      it.close();
    }

    // Delete the corresponding map
    this.getMap().delete();

    // Delete the wrapper mapping
    OIterator<? extends MetadataWrapper> mIterator = this.getAllMetadata();

    try
    {
      while (mIterator.hasNext())
      {
        MetadataWrapper metadata = mIterator.next();
        metadata.delete();
      }
    }
    finally
    {
      mIterator.close();
    }

    // Delete the corresponding report item
    ReportItem report = ReportItem.getByDashboard(this.getId());

    if (report != null)
    {
      report.delete();
    }

    Roles role = this.getDashboardRole();

    super.delete();

    // Delete the dashboard role
    role.delete();
  }

  public DashboardMap getMap()
  {
    DashboardMapQuery query = new DashboardMapQuery(new QueryFactory());
    query.WHERE(query.getDashboard().EQ(this));

    OIterator<? extends DashboardMap> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }

  @Override
  public String getMapId()
  {
    DashboardMap map = this.getMap();

    if (map != null)
    {
      return map.getId();
    }

    return null;
  }

  public MdClass[] getSortedTypes()
  {
    // This operation should use only cached objects
    DashboardMetadataQuery query = new DashboardMetadataQuery(new QueryFactory());
    query.WHERE(query.getParent().EQ(this));
    query.ORDER_BY_ASC(query.getListOrder());

    OIterator<? extends DashboardMetadata> iter = query.getIterator();

    List<MdClass> mdClasses = new LinkedList<MdClass>();

    try
    {
      while (iter.hasNext())
      {
        DashboardMetadata dm = iter.next();
        MetadataWrapper mw = dm.getChild();

        mdClasses.add(mw.getWrappedMdClass());
      }
    }
    finally
    {
      iter.close();
    }

    return mdClasses.toArray(new MdClass[mdClasses.size()]);
  }

  @Transaction
  public static Dashboard create(Dashboard dashboard)
  {
    dashboard.apply();

    return dashboard;
  }

  @Override
  @Transaction
  public void apply()
  {
    boolean isNew = isNew();

    if (this.isNew() && !this.isAppliedToDB())
    {
      if (this.getName() == null || this.getName().length() == 0)
      {
        this.setName(generator.generateKey(""));
      }

      String dashboardLabel = this.getDisplayLabel().getValue();
      String roleName = this.getName().replaceAll("\\s", "");

      // Create the Dashboard Role
      Roles role = new Roles();
      role.setRoleName(RoleView.DASHBOARD_NAMESPACE + "." + roleName);
      role.getDisplayLabel().setValue(dashboardLabel);
      role.apply();

      this.setDashboardRole(role);
    }

    super.apply();

    Roles role = this.getDashboardRole();
    role.lock();
    role.getDisplayLabel().setValue(this.getDisplayLabel().getValue());
    role.apply();

    if (isNew)
    {
      DashboardMap map = new DashboardMap();
      map.setName(this.getName());
      map.setDashboard(this);
      map.apply();

      DashboardState state = new DashboardState();
      state.setDashboard(this);
      state.apply();
    }
  }

  @Override
  public String applyWithOptions(String options)
  {
    // Loop until a name is selected which works
    while (true)
    {
      try
      {
        return this.applyWithOptionsInTransaction(options);
      }
      catch (DuplicateDataException e)
      {
        this.setName(generator.generateKey(""));
      }
    }
  }

  @Transaction
  private String applyWithOptionsInTransaction(String options)
  {
    try
    {
      JSONObject object = new JSONObject(options);

      if (object.has("label"))
      {
        this.getDisplayLabel().setValue(object.getString("label"));
      }

      this.apply();

      if (object.has("users"))
      {
        JSONArray users = object.getJSONArray("users");

        assignUsers(this.getId(), users);
      }

      if (object.has("types"))
      {
        JSONArray types = object.getJSONArray("types");

        MappableClass.assign(this, types);
      }

      /*
       * If this is a new instance always give the current user permissions to the dashboard
       */
      if (this.isNew())
      {
        String roleId = this.getDashboardRoleId();
        SingleActorDAOIF user = Session.getCurrentSession().getUser();

        RoleDAO roleDAO = RoleDAO.get(roleId).getBusinessDAO();
        roleDAO.assignMember(user);
      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }

    return this.getJSON();
  }

  @Override
  public String getLayersToDelete(String options)
  {
    try
    {
      JSONArray layers = new JSONArray();

      JSONObject object = new JSONObject(options);

      if (object.has("types"))
      {
        JSONArray types = object.getJSONArray("types");

        Collection<String> layerNames = MappableClass.getLayersToDelete(this, types);

        for (String layerName : layerNames)
        {
          layers.put(layerName);
        }
      }

      return layers.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }

  }

  @Override
  @Transaction
  public Dashboard clone(String name)
  {
    Dashboard clone = new Dashboard();
    clone.getDisplayLabel().setDefaultValue(name);
    clone.getDescription().setDefaultValue(this.getDescription().getValue());
    clone.setRemovable(true);
    clone.apply();

    OIterator<? extends DashboardMetadata> allMetadata = this.getAllMetadataRel();

    try
    {
      while (allMetadata.hasNext())
      {
        DashboardMetadata rel = allMetadata.next();

        MetadataWrapper existingWrapper = rel.getChild();

        MetadataWrapper cloneWrapper = existingWrapper.clone(clone);

        DashboardMetadata dm = clone.addMetadata(cloneWrapper);
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

    // Clone the global state
    DashboardState state = DashboardState.getDashboardState(this, null);
    state.clone(this);

    SingleActor user = GeoprismUser.getCurrentUser();

    if (user != null)
    {
      RoleDAO roleDAO = RoleDAO.get(clone.getDashboardRoleId()).getBusinessDAO();
      roleDAO.assignMember((SingleActorDAOIF) SingleActorDAO.get(user.getId()));
    }

    // Clone the report
    if (this.hasReport())
    {
      ReportItem item = ReportItem.getByDashboard(this.getId());
      item.clone(clone);
    }

    return clone;
  }

  public static Dashboard getDashboardByLabel(String label)
  {
    DashboardQuery query = new DashboardQuery(new QueryFactory());
    query.WHERE(query.getDisplayLabel().localize().EQ(label));
    OIterator<? extends Dashboard> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }

  public static String[] getTextSuggestions(String mdAttributeId, String text, Integer limit)
  {
    List<String> suggestions = new LinkedList<String>();

    MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();
    MdClassDAOIF mdClass = mdAttributeConcrete.definedByClass();

    QueryFactory factory = new QueryFactory();

    BusinessQuery bQuery = factory.businessQuery(mdClass.definesType());
    AttributeChar selectable = null;

    if (mdAttributeConcrete instanceof MdAttributeCharacterDAOIF)
    {
      selectable = bQuery.aCharacter(mdAttributeConcrete.definesAttribute());
    }
    else
    {
      selectable = bQuery.aText(mdAttributeConcrete.definesAttribute());
    }

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

  public static final net.geoprism.ontology.Classifier[] getClassifierRoots(String mdAttributeId)
  {
    MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();

    QueryFactory factory = new QueryFactory();

    ClassifierTermAttributeRootQuery rootQuery = new ClassifierTermAttributeRootQuery(factory);
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
    JSONArray nodes = Dashboard.getClassifierTreeJSON(mdAttributeId);

    return nodes.toString();
  }

  public static JSONArray getClassifierTreeJSON(String mdAttributeId)
  {
    MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();
    ClassifierTermAttributeRootQuery rootQuery = new ClassifierTermAttributeRootQuery(new QueryFactory());

    rootQuery.WHERE(rootQuery.getParent().EQ(mdAttributeConcrete.getId()));

    OIterator<? extends ClassifierTermAttributeRoot> iterator = null;

    try
    {
      iterator = rootQuery.getIterator();

      JSONArray nodes = new JSONArray();

      while (iterator.hasNext())
      {
        ClassifierTermAttributeRoot relationship = iterator.next();
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

      return nodes;
    }
    finally
    {
      if (iterator != null)
      {
        iterator.close();
      }
    }
  }

  public static long getOptionCount(String mdAttributeId)
  {
    MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();

    QueryFactory factory = new QueryFactory();

    ClassifierTermAttributeRootQuery rootQuery = new ClassifierTermAttributeRootQuery(factory);
    rootQuery.WHERE(rootQuery.getParent().EQ(mdAttributeConcrete.getId()));

    ClassifierAllPathsTableQuery aptQuery = new ClassifierAllPathsTableQuery(factory);
    aptQuery.WHERE(aptQuery.getParentTerm().EQ(rootQuery.getChild()));

    return aptQuery.getCount();
  }

  public static Classifier[] getClassifierSuggestions(String mdAttributeId, String text, Integer limit)
  {
    MdAttributeConcreteDAOIF mdAttributeConcrete = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();

    QueryFactory factory = new QueryFactory();
    ClassifierTermAttributeRootQuery rootQuery = new ClassifierTermAttributeRootQuery(factory);
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

  public static String[] getCategoryInputSuggestions(String mdAttributeId, String geoNodeId, String universalId, String aggregationVal, String text, Integer limit, String state)
  {
    DashboardCondition[] conditions = DashboardCondition.getConditionsFromState(state);

    Set<String> suggestions = new TreeSet<String>();

    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId);
    String attributeName = mdAttribute.definesAttribute();

    ThematicQueryBuilder builder = getBuilder(geoNodeId, universalId, aggregationVal, conditions, mdAttribute);
    ValueQuery query = builder.getThematicValueQuery();

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

  private static ThematicQueryBuilder getBuilder(String geoNodeId, String universalId, String aggregationVal, DashboardCondition[] conditions, MdAttributeDAOIF mdAttribute)
  {
    GeoNode geoNode = GeoNode.get(geoNodeId);
    List<DashboardCondition> conditionList = Arrays.asList(conditions);
    AllAggregationType aggregationType = AllAggregationType.valueOf(aggregationVal);

    if (universalId.equals(GeometryAggregationStrategy.VALUE))
    {
      return new GeometryThematicQueryBuilder(new QueryFactory(), mdAttribute, null, aggregationType, conditionList, geoNode);
    }
    else
    {
      Universal universal = Universal.get(universalId);

      return new GeoEntityThematicQueryBuilder(new QueryFactory(), mdAttribute, null, aggregationType, conditionList, universal, geoNode);
    }
  }

  public static Dashboard[] getDashboardsForCountry(GeoEntity country)
  {
    QueryFactory factory = new QueryFactory();

    UniversalAllPathsTableQuery aptQuery = new UniversalAllPathsTableQuery(factory);
    aptQuery.WHERE(aptQuery.getParentTerm().EQ(country.getUniversal()));

    ClassUniversalQuery cuQuery = new ClassUniversalQuery(factory);
    cuQuery.WHERE(cuQuery.getChild().EQ(aptQuery.getChildTerm()));

    MappableClassQuery mcQuery = new MappableClassQuery(factory);
    mcQuery.WHERE(mcQuery.universal(cuQuery));

    MetadataWrapperQuery mwQuery = new MetadataWrapperQuery(factory);
    mwQuery.WHERE(mwQuery.getWrappedMdClass().EQ(mcQuery.getWrappedMdClass()));

    DashboardQuery query = new DashboardQuery(factory);
    query.WHERE(query.metadata(mwQuery));

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

  public List<ValueObject> getCountries()
  {
    QueryFactory factory = new QueryFactory();

    // MetadataWrapperQuery mwQuery = new MetadataWrapperQuery(factory);
    // mwQuery.WHERE(mwQuery.getDashboard().EQ(this));
    //
    // MappableClassQuery mcQuery = new MappableClassQuery(factory);
    // mcQuery.WHERE(mcQuery.getWrappedMdClass().EQ(mwQuery.getWrappedMdClass()));
    //
    // ClassUniversalQuery cuQuery = new ClassUniversalQuery(factory);
    // cuQuery.WHERE(cuQuery.getParent().EQ(mcQuery));
    //
    // AllowedInQuery aiQuery = new AllowedInQuery(factory);
    // aiQuery.WHERE(aiQuery.getParent().EQ(Universal.getRoot()));
    //
    // UniversalAllPathsTableQuery aptQuery = new UniversalAllPathsTableQuery(factory);
    // aptQuery.WHERE(aptQuery.getParentTerm().EQ(aiQuery.getChild()));
    // aptQuery.AND(aptQuery.getChildTerm().EQ(cuQuery.getChild()));
    //
    // GeoEntityQuery query = new GeoEntityQuery(factory);
    // query.WHERE(query.getUniversal().EQ(aptQuery.getParentTerm()));
    // query.ORDER_BY_ASC(query.getDisplayLabel().localize());
    //
    // OIterator<? extends GeoEntity> it = query.getIterator();
    //
    // try
    // {
    // List<? extends GeoEntity> entities = it.getAll();
    //
    // return new LinkedList<GeoEntity>(entities);
    // }
    // finally
    // {
    // it.close();
    // }

    ValueQuery vQuery = new ValueQuery(factory);

    MetadataWrapperQuery mwQuery = new MetadataWrapperQuery(vQuery);
    vQuery.WHERE(mwQuery.getDashboard().EQ(this));

    MappableClassQuery mcQuery = new MappableClassQuery(vQuery);
    vQuery.WHERE(mcQuery.getWrappedMdClass().EQ(mwQuery.getWrappedMdClass()));

    ClassUniversalQuery cuQuery = new ClassUniversalQuery(vQuery);
    vQuery.WHERE(cuQuery.getParent().EQ(mcQuery));

    AllowedInQuery aiQuery = new AllowedInQuery(vQuery);
    vQuery.WHERE(aiQuery.getParent().EQ(Universal.getRoot()));

    UniversalAllPathsTableQuery aptQuery = new UniversalAllPathsTableQuery(vQuery);
    vQuery.WHERE(aptQuery.getParentTerm().EQ(aiQuery.getChild()));
    vQuery.AND(aptQuery.getChildTerm().EQ(cuQuery.getChild()));

    GeoEntityQuery geQuery = new GeoEntityQuery(vQuery);
    vQuery.WHERE(geQuery.getUniversal().EQ(aptQuery.getParentTerm()));
    vQuery.ORDER_BY_ASC(geQuery.getDisplayLabel().localize());

    vQuery.SELECT(geQuery.getId(), geQuery.getDisplayLabel().localize("displayLabel"), geQuery.getUniversal("universalId"));

    OIterator<ValueObject> iterator = vQuery.getIterator();

    try
    {
      return iterator.getAll();
    }
    finally
    {
      iterator.close();
    }

  }

  public ValueQuery getGeoEntitySuggestions(String text, Integer limit)
  {
    ValueQuery query = new ValueQuery(new QueryFactory());

    List<ValueObject> countries = this.getCountries();

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

    Condition cCondition = null;

    for (ValueObject country : countries)
    {
      String countryId = country.getValue("id");

      if (cCondition == null)
      {
        cCondition = aptQuery.getParentTerm().EQ(countryId);
      }
      else
      {
        cCondition = OR.get(cCondition, aptQuery.getParentTerm().EQ(countryId));
      }
    }

    query.SELECT(id, label);
    query.WHERE(label.LIKEi("%" + text + "%"));
    query.AND(entityQuery.EQ(aptQuery.getChildTerm()));
    query.AND(cCondition);

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

  public DashboardCondition[] getConditions()
  {
    DashboardState state = this.getDashboardState();

    if (state != null)
    {
      String json = state.getConditions();

      if (json != null && json.length() > 0)
      {
        DashboardCondition[] conditions = DashboardCondition.deserialize(json);

        return conditions;
      }
    }

    return new DashboardCondition[] {};
  }

  public Map<String, DashboardCondition> getConditionMap()
  {
    Map<String, DashboardCondition> map = new HashMap<String, DashboardCondition>();

    DashboardCondition[] conditions = this.getConditions();

    for (DashboardCondition condition : conditions)
    {
      map.put(condition.getJSONKey(), condition);
    }

    return map;
  }

  @Override
  public String getConditionsJSON()
  {
    DashboardState state = this.getDashboardState();
    String conditions = state.getConditions();

    if (conditions != null && conditions.length() > 0)
    {
      return conditions;
    }

    return "''";
  }

  @Override
  public Boolean hasAccess()
  {
    SingleActor currentUser = GeoprismUser.getCurrentUser();

    if (currentUser != null && currentUser instanceof GeoprismUserIF)
    {
      Boolean access = ( (GeoprismUserIF) currentUser ).isAssigned(this.getDashboardRole());

      if (!access)
      {
        return GeoprismUser.hasAccess(AccessConstants.ADMIN);
      }

      return true;
    }

    return false;
  }

  /*
   * Gets all active geoprism users in the system who are not Administrators
   */
  @Override
  public GeoprismUser[] getAllDashboardUsers()
  {
    ArrayList<GeoprismUser> nonAdminGDUsers = new ArrayList<GeoprismUser>();
    GeoprismUser[] gdUsers = GeoprismUser.getAllUsers();
    for (int i = 0; i < gdUsers.length; i++)
    {
      boolean isAdmin = false;
      GeoprismUser user = gdUsers[i];

      List<? extends Roles> userRoles = user.getAllAssignedRole().getAll();
      for (Roles role : userRoles)
      {
        if (role.getRoleName().equals(RoleView.ADMIN_NAMESPACE + ".Administrator"))
        {
          isAdmin = true;
        }
      }

      Boolean inactive = user.getInactive();

      if (!inactive && !isAdmin)
      {
        nonAdminGDUsers.add(user);
      }
    }

    GeoprismUser[] nonAdminGDUsersArr = nonAdminGDUsers.toArray(new GeoprismUser[nonAdminGDUsers.size()]);

    return nonAdminGDUsersArr;
  }

  /*
   * Gets all active geoprism users in the system who are not Administrators and whether they already have access to a
   * given dashboard.
   */
  @Override
  public String getAllDashboardUsersJSON()
  {
    JSONArray usersArr = this.getDashboardUsersJSON();

    return usersArr.toString();
  }

  private JSONArray getDashboardUsersJSON()
  {
    JSONArray usersArr = new JSONArray();

    GeoprismUser[] gdUsers = this.getAllDashboardUsers();

    for (int i = 0; i < gdUsers.length; i++)
    {
      JSONObject userObj = new JSONObject();
      GeoprismUser user = gdUsers[i];

      boolean hasAccess = this.userHasAccess(user);
      Boolean inactive = user.getInactive();

      if (!inactive)
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
    return usersArr;
  }

  public static void assignUsers(String dashboardId, JSONArray users)
  {
    Dashboard dashboard = Dashboard.get(dashboardId);
    String roleId = dashboard.getDashboardRoleId();
    RoleDAO roleDAO = RoleDAO.get(roleId).getBusinessDAO();

    for (int i = 0; i < users.length(); i++)
    {
      try
      {
        JSONObject userObj = users.getJSONObject(i);

        String userId = userObj.getString(GeoprismUser.ID);
        boolean assignToDashboard = (Boolean) userObj.get("hasAccess");

        UserDAOIF user = UserDAO.get(userId);

        if (assignToDashboard)
        {
          roleDAO.assignMember(user);
        }
        else
        {
          roleDAO.deassignMember(user);
        }
      }
      catch (JSONException e)
      {
        String msg = "Could not properly parse user json.";
        throw new ProgrammingErrorException(msg, e);
      }
    }
  }

  private Boolean userHasAccess(GeoprismUser user)
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
    List<ValueObject> countries = this.getCountries();

    Map<String, Integer> indices = new HashMap<String, Integer>();

    int count = 0;

    for (ValueObject country : countries)
    {
      String universalId = country.getValue("universalId");
      Universal universal = Universal.get(universalId);

      Collection<Term> children = GeoEntityUtil.getOrderedDescendants(universal, AllowedIn.CLASS);

      indices.put(universalId, count++);

      for (Term child : children)
      {
        indices.put(child.getId(), count++);
      }

    }

    return indices;
  }

  public GeoNode[] getGeoNodes(MdAttribute thematicAttribute)
  {
    MdAttributeDAOIF thematicAttributeDAO = MdAttributeDAO.get(thematicAttribute.getId());

    return this.getGeoNodes(thematicAttributeDAO);
  }

  @Override
  public String getGeoNodesJSON(MdAttribute thematicAttribute)
  {
    return this.getGeoNodesJSON(thematicAttribute, true);
  }

  public String getGeoNodesJSON(MdAttribute thematicAttribute, Boolean aggregatable)
  {
    JSONArray nodesArr = new JSONArray();
    GeoNode[] nodes = this.getGeoNodes(thematicAttribute);
    for (GeoNode node : nodes)
    {
      try
      {
        JSONObject nodeJSON = new JSONObject();
        nodeJSON.put("id", node.getId());
        nodeJSON.put("type", node.getType());
        nodeJSON.put("displayLabel", node.getGeoEntityAttribute().getDisplayLabel());
        nodesArr.put(nodeJSON);
      }
      catch (JSONException e)
      {
        String error = "Could not build GeoNode JSON.";
        throw new ProgrammingErrorException(error, e);
      }
    }

    if (nodesArr.length() == 0)
    {
      throw new UnsupportedAggregationException();
    }

    return nodesArr.toString();
  }

  public GeoNode[] getGeoNodes(MdAttributeDAOIF thematicAttribute)
  {
    QueryFactory factory = new QueryFactory();

    MappableClassQuery mcQuery = new MappableClassQuery(factory);
    mcQuery.AND(mcQuery.getWrappedMdClass().EQ(thematicAttribute.definedByClass()));

    MappableClassGeoNodeQuery mcgnQuery = new MappableClassGeoNodeQuery(factory);
    mcgnQuery.WHERE(mcgnQuery.getParent().EQ(mcQuery));

    GeoNodeQuery gnQuery = new GeoNodeQuery(factory);
    gnQuery.WHERE(gnQuery.EQ(mcgnQuery.getChild()));

    OIterator<? extends GeoNode> iterator = gnQuery.getIterator();

    List<GeoNode> nodes = new LinkedList<GeoNode>();

    try
    {
      while (iterator.hasNext())
      {
        GeoNode geoNode = iterator.next();

        nodes.add(geoNode);
      }

      return nodes.toArray(new GeoNode[nodes.size()]);

    }
    finally
    {
      iterator.close();
    }
  }

  public GeoNode getGeoNode(MdAttributeDAOIF mdAttribute)
  {
    MdAttributeConcreteDAOIF mdAttributeConcrete = mdAttribute.getMdAttributeConcrete();
    QueryFactory factory = new QueryFactory();

    MappableClassQuery mcQuery = new MappableClassQuery(factory);
    mcQuery.AND(mcQuery.getWrappedMdClass().EQ(mdAttribute.definedByClass()));

    MappableClassGeoNodeQuery mcgnQuery = new MappableClassGeoNodeQuery(factory);
    mcgnQuery.WHERE(mcgnQuery.getParent().EQ(mcQuery));

    GeoNodeQuery gnQuery = new GeoNodeQuery(factory);
    gnQuery.WHERE(gnQuery.EQ(mcgnQuery.getChild()));

    OIterator<? extends GeoNode> iterator = gnQuery.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        GeoNode geoNode = iterator.next();
        // Geo entity node
        String geoEntityAttributeId = geoNode.getGeoEntityAttributeId();

        if (geoEntityAttributeId.equals(mdAttribute.getId()) || geoEntityAttributeId.equals(mdAttributeConcrete.getId()))
        {
          return geoNode;
        }
      }

      throw new ProgrammingErrorException("Unable to find a Geo Node for the Dashboard [" + this.getId() + "] and Attribute [" + mdAttribute.getId() + "]");
    }
    finally
    {
      iterator.close();
    }
  }

  @Override
  public InputStream getThumbnailStream()
  {
    DashboardState state = this.getDashboardState();
    byte[] buffer = state.getMapThumbnail();

    return new ByteArrayInputStream(buffer);
  }

  public DashboardState getDashboardState()
  {
    SingleActor user = GeoprismUser.getCurrentUser();

    DashboardState state = null;

    if (user != null)
    {
      state = DashboardState.getDashboardState(this, user);
    }

    if (state == null)
    {
      state = DashboardState.getDashboardState(this, null);
    }

    return state;
  }

  private DashboardState getOrCreateDashboardState(SingleActor user)
  {
    DashboardState state = DashboardState.getDashboardState(this, user);

    if (state == null)
    {
      state = new DashboardState();
      state.setDashboard(this);
      state.setGeoprismUser(user);
    }
    else
    {
      state.lock();
    }
    return state;
  }

  @Override
  public void generateThumbnailImage()
  {
    /*
     * This method is only invoked when a new layer is created. As such, it generates a thumbnail for both the current
     * users state and the global state. Normally you just want to generate a thumbnail for one or the other.
     */
    this.executeThumbnailThread(GeoprismUser.getCurrentUser(), null);
  }

  private void executeThumbnailThread(SingleActor... users)
  {
    String sessionId = Session.getCurrentSession().getId();

    // Write the thumbnail
    TaskExecutor.addTask(new ThumbnailTask(sessionId, this, users));
  }

  @Transaction
  private void generateThumbnailImage(SingleActor[] users)
  {
    byte[] image = this.generateThumbnail();

    for (SingleActor user : users)
    {
      DashboardState state = DashboardState.getDashboardState(this, user);

      if (state != null)
      {
        state.lock();
        if (image == null)
        {
          state.setMapThumbnail(new byte[0]);
        }
        else
        {
          state.setMapThumbnail(image);
        }
        state.apply();
      }
    }
  }

  @Transaction
  public byte[] generateThumbnail()
  {
    String outFileFormat = "png";
    BufferedImage base = null;
    Graphics mapBaseGraphic = null;
    BufferedImage resizedImage = null;
    int defaultWidth = 1179;
    int defaultHeight = 750;
    Double bottom;
    Double top;
    Double right;
    Double left;
    JSONObject restructuredBounds = new JSONObject();

    DashboardMap dashMap = this.getMap();

    // Ordering the layers from the default map
    DashboardLayer[] orderedLayers = dashMap.getOrderedLayers();
    JSONArray mapBoundsArr = dashMap.getExpandedMapLayersBBox(orderedLayers, .2);

    if (mapBoundsArr != null)
    {

      // Get bounds of the map
      try
      {
        left = mapBoundsArr.getDouble(0);
        bottom = mapBoundsArr.getDouble(1);
        right = mapBoundsArr.getDouble(2);
        top = mapBoundsArr.getDouble(3);

        restructuredBounds.put("left", left);
        restructuredBounds.put("bottom", bottom);
        restructuredBounds.put("right", right);
        restructuredBounds.put("top", top);
      }
      catch (JSONException e)
      {
        String error = "Could not parse map bounds.";
        throw new ProgrammingErrorException(error, e);
      }

      int width = (int) Math.min(defaultWidth, Math.round( ( ( ( right - left ) / ( top - bottom ) ) * defaultHeight )));
      int height = (int) Math.min(defaultHeight, Math.round( ( ( ( top - bottom ) / ( right - left ) ) * defaultWidth )));

      base = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

      // Create the base canvas that all other map elements will be draped on top of
      mapBaseGraphic = base.getGraphics();
      mapBaseGraphic.setColor(Color.white);
      mapBaseGraphic.fillRect(0, 0, width, height);
      mapBaseGraphic.drawImage(base, 0, 0, null);

      // Ticket #412: Get base map
      String baseType = "osm";

      if (baseType.length() > 0)
      {
        BufferedImage baseMapImage = dashMap.getBaseMapCanvas(width, height, Double.toString(left), Double.toString(bottom), Double.toString(right), Double.toString(top), baseType);

        if (baseMapImage != null)
        {
          mapBaseGraphic.drawImage(baseMapImage, 0, 0, null);
        }
      }

      // Add layers to the base canvas
      BufferedImage layerCanvas = dashMap.getLayersExportCanvas(width, height, orderedLayers, restructuredBounds.toString());

      // Offset the layerCanvas so that it is center
      int widthOffset = (int) ( ( width - layerCanvas.getWidth() ) / 2 );
      int heightOffset = (int) ( ( height - layerCanvas.getHeight() ) / 2 );

      mapBaseGraphic.drawImage(layerCanvas, widthOffset, heightOffset, null);

      try
      {
        resizedImage = Thumbnails.of(base).size(330, 210).asBufferedImage();
      }
      catch (IOException e)
      {
        String error = "Could not resize map image to thumbnail size.";
        throw new ProgrammingErrorException(error, e);
      }

      ByteArrayOutputStream outStream = new ByteArrayOutputStream();

      try
      {
        ImageIO.write(resizedImage, outFileFormat, outStream);
      }
      catch (IOException e)
      {
        String error = "Could not write map image to the output stream.";
        throw new ProgrammingErrorException(error, e);
      }
      finally
      {
        if (outStream != null)
        {
          try
          {
            outStream.flush();
            outStream.close();
          }
          catch (IOException e)
          {
            String error = "Could not close stream.";
            throw new ProgrammingErrorException(error, e);
          }
        }
      }

      if (mapBaseGraphic != null)
      {
        mapBaseGraphic.dispose();
      }

      return outStream.toByteArray();
    }

    return null;
  }

  @Override
  @Transaction
  public void setBaseLayerState(String baseLayerState)
  {
    DashboardMap dm = this.getMap();
    dm.lock();
    dm.setActiveBaseMap(baseLayerState);
    dm.unlock();
  }

  public MetadataWrapper getMetadataWrapper(MdClass mdClass)
  {
    MetadataWrapperQuery query = new MetadataWrapperQuery(new QueryFactory());
    query.WHERE(query.getWrappedMdClass().EQ(mdClass));
    query.AND(query.getDashboard().EQ(this));

    OIterator<? extends MetadataWrapper> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }

  @Override
  public String getJSON()
  {
    try
    {
      return this.toJSON().toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public JSONObject toJSON() throws JSONException
  {
    /*
     * Ensure the user has permissions to read this dashboard
     */
    if (!this.hasAccess())
    {
      SingleActorDAOIF user = Session.getCurrentSession().getUser();
      throw new ReadPermissionException("", this, user);
    }

    return getJSON(this.getConditionMap());
  }

  public JSONObject getJSON(Map<String, DashboardCondition> conditions) throws JSONException
  {
    MdClass[] mdClasses = this.getSortedTypes();

    JSONArray types = new JSONArray();

    for (MdClass mdClass : mdClasses)
    {
      types.put(this.toJSON(mdClass, conditions));
    }

    DashboardMap map = this.getMap();

    JSONObject object = new JSONObject();
    object.put("id", this.getId());
    object.put("name", this.getName());
    object.put("label", this.getDisplayLabel().getValue());
    object.put("description", this.getDescription().getValue());
    object.put("hasReport", this.hasReport());
    object.put("editDashboard", GeoprismUser.hasAccess(AccessConstants.EDIT_DASHBOARD));
    object.put("editData", GeoprismUser.hasAccess(AccessConstants.EDIT_DATA));
    object.put("types", types);

    List<ValueObject> countries = this.getCountries();

    JSONArray areas = new JSONArray();

    for (ValueObject country : countries)
    {
      areas.put(country.getValue("displayLabel"));
    }

    object.put("focusAreas", areas);

    if (map != null)
    {
      object.put("mapId", map.getId());
    }

    String activeBaseMap = map.getActiveBaseMap();

    if (activeBaseMap != null && activeBaseMap.length() > 0)
    {
      object.put("activeBaseMap", new JSONObject(activeBaseMap));
    }
    else
    {
      JSONObject baseMap = new JSONObject();
      baseMap.put("LAYER_SOURCE_TYPE", "OSM");

      object.put("activeBaseMap", baseMap);
    }

    if (conditions.containsKey(LocationCondition.CONDITION_TYPE))
    {
      DashboardCondition condition = conditions.get(LocationCondition.CONDITION_TYPE);

      object.put("location", condition.getJSON());
    }
    else
    {
      LocationCondition condition = new LocationCondition();

      object.put("location", condition.getJSON());
    }

    return object;
  }

  private JSONObject toJSON(MdClass mdClass, Map<String, DashboardCondition> conditions) throws JSONException
  {
    JSONArray attributes = new JSONArray();

    MetadataWrapper wrapper = this.getMetadataWrapper(mdClass);
    MdAttributeView[] views = wrapper.getSortedAttributes();

    for (MdAttributeView view : views)
    {
      DashboardCondition condition = conditions.get(view.getMdAttributeId());

      attributes.put(view.toJSON(condition));
    }

    JSONObject object = new JSONObject();
    object.put("label", mdClass.getDisplayLabel().getValue());
    object.put("id", mdClass.getId());
    object.put("description", this.getDescription().getValue());
    object.put("attributes", attributes);

    return object;
  }

  @Override
  public String saveState(String json, Boolean global)
  {
    DashboardCondition[] conditions = DashboardCondition.getConditionsFromState(json);

    SingleActor user = null;

    if (!global)
    {
      user = GeoprismUser.getCurrentUser();
    }

    DashboardState state = this.getOrCreateDashboardState(user);

    state.setConditions(DashboardCondition.serialize(conditions));
    state.apply();

    this.executeThumbnailThread(user);

    return "";
  }

  @Override
  public String getDashboardInformation()
  {
    try
    {
      return this.getDashboardInformationJSON().toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public JSONObject getDashboardInformationJSON() throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("dashboardId", this.getId());
    object.put("label", this.getDisplayLabel().getValue());
    object.put("description", this.getDescription().getValue());

    return object;
  }

  public MetadataWrapper getMetadataWrapper(MdClassDAOIF mdClass)
  {
    MetadataWrapperQuery query = new MetadataWrapperQuery(new QueryFactory());
    query.WHERE(query.getDashboard().EQ(this));
    query.AND(query.getWrappedMdClass().EQ(mdClass.getId()));

    OIterator<? extends MetadataWrapper> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }

  private JSONArray getMappableClassJSON() throws JSONException
  {
    List<? extends MetadataWrapper> wrappers = this.getAllMetadata().getAll();

    JSONArray array = new JSONArray();

    MappableClass[] mClasses = MappableClass.getAll();

    for (MappableClass mClass : mClasses)
    {
      array.put(mClass.toJSON(this, wrappers));
    }
    return array;
  }

  @Override
  public String getDashboardDefinition()
  {
    try
    {
      if (!this.isNew())
      {
        this.lock();
      }

      JSONObject options = new JSONObject();
      options.put("types", this.getMappableClassJSON());
      options.put("users", this.getDashboardUsersJSON());

      JSONObject object = new JSONObject();
      object.put(Dashboard.NAME, this.getName());
      object.put(Dashboard.DISPLAYLABEL, this.getDisplayLabel().getValue());
      object.put(Dashboard.DESCRIPTION, this.getDescription().getValue());
      object.put(Dashboard.REMOVABLE, this.getRemovable());

      List<ValueObject> countries = this.getCountries();

      JSONArray areas = new JSONArray();

      for (ValueObject country : countries)
      {
        areas.put(country.getValue("displayLabel"));
      }

      object.put("focusAreas", areas);

      object.put("options", options);

      return object.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public int getMaxOrder()
  {
    ValueQuery vQuery = new ValueQuery(new QueryFactory());

    MetadataWrapperQuery wQuery = new MetadataWrapperQuery(vQuery);
    DashboardMetadataQuery dmQuery = new DashboardMetadataQuery(vQuery);

    vQuery.WHERE(wQuery.getDashboard().EQ(this));
    vQuery.AND(dmQuery.hasChild(wQuery));

    MAX selectable = F.MAX(dmQuery.getListOrder());
    selectable.setColumnAlias("order_max");
    selectable.setUserDefinedAlias("order_max");

    vQuery.SELECT(selectable);

    OIterator<ValueObject> iterator = vQuery.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        ValueObject result = iterator.next();
        String value = result.getValue("order_max");

        if (value != null && value.length() > 0)
        {
          return Integer.parseInt(value);
        }
      }
    }
    finally
    {
      iterator.close();
    }

    return 0;
  }

  @Override
  @Transaction
  public void setMetadataWrapperOrder(String[] typeIds)
  {
    DashboardMetadataQuery query = new DashboardMetadataQuery(new QueryFactory());
    query.WHERE(query.getParent().EQ(this));

    OIterator<? extends DashboardMetadata> it = query.getIterator();

    try
    {
      List<? extends DashboardMetadata> dms = it.getAll();

      for (DashboardMetadata dm : dms)
      {
        MetadataWrapper wrapper = dm.getChild();

        for (int i = 0; i < typeIds.length; i++)
        {
          String typeId = typeIds[i];

          if (wrapper.getWrappedMdClassId().equals(typeId))
          {
            dm.lock();
            dm.setListOrder(i);
            dm.apply();
          }
        }
      }
    }
    finally
    {
      it.close();
    }
  }

  @Override
  @Transaction
  public void setDashboardAttributesOrder(String classId, String[] attributeIds)
  {
    MetadataWrapper wrapper = MetadataWrapper.getByWrappedMdClassId(this, classId);

    if (wrapper != null)
    {
      DashboardAttributesQuery query = new DashboardAttributesQuery(new QueryFactory());
      query.WHERE(query.getParent().EQ(wrapper));

      OIterator<? extends DashboardAttributes> it = query.getIterator();

      try
      {
        List<? extends DashboardAttributes> attributes = it.getAll();

        for (DashboardAttributes attribute : attributes)
        {
          AttributeWrapper aw = attribute.getChild();

          for (int i = 0; i < attributeIds.length; i++)
          {
            String attributeId = attributeIds[i];

            if (aw.getWrappedMdAttributeId().equals(attributeId))
            {
              attribute.lock();
              attribute.setListOrder(i);
              attribute.apply();
            }
          }
        }
      }
      finally
      {
        it.close();
      }
    }
  }

  public static String getAvailableDashboardsAsJSON(String dashboardId)
  {
    DashboardQuery query = Dashboard.getSortedDashboards();
    OIterator<? extends Dashboard> iterator = query.getIterator();

    try
    {
      JSONArray dashboards = new JSONArray();
      boolean first = true;

      JSONObject response = new JSONObject();

      while (iterator.hasNext())
      {
        Dashboard dashboard = iterator.next();

        JSONObject object = dashboard.getDashboardInformationJSON();

        dashboards.put(object);

        if (first || dashboard.getId().equals(dashboardId))
        {
          response.put("state", dashboard.toJSON());

          first = false;
        }
      }

      response.put("dashboards", dashboards);
      response.put("editDashboard", GeoprismUser.hasAccess(AccessConstants.EDIT_DASHBOARD));

      return response.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
    finally
    {
      iterator.close();
    }

  }
}
