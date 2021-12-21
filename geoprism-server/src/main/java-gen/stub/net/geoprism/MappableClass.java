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
package net.geoprism;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.IndicatorCompositeInfo;
import com.runwaysdk.constants.IndicatorPrimitiveInfo;
import com.runwaysdk.constants.MdAttributeIndicatorInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.IndicatorCompositeDAO;
import com.runwaysdk.dataaccess.IndicatorCompositeDAOIF;
import com.runwaysdk.dataaccess.IndicatorPrimitiveDAO;
import com.runwaysdk.dataaccess.IndicatorPrimitiveDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributePrimitiveDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeIndicatorDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.AllowedInAllPathsTableQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.AllowedInQuery;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.GeoNodeGeometry;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;
import com.runwaysdk.system.metadata.IndicatorAggregateFunction;
import com.runwaysdk.system.metadata.IndicatorOperator;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeIndicator;
import com.runwaysdk.system.metadata.MdClass;

import net.geoprism.dashboard.AttributeWrapper;
import net.geoprism.dashboard.AttributeWrapperQuery;
import net.geoprism.dashboard.Dashboard;
import net.geoprism.dashboard.DashboardAttributes;
import net.geoprism.dashboard.DashboardMetadata;
import net.geoprism.dashboard.MetadataWrapper;
import net.geoprism.dashboard.MetadataWrapperQuery;
import net.geoprism.data.DatabaseUtil;
import net.geoprism.data.GeoprismDatasetExporterIF;
import net.geoprism.data.GeoprismDatasetExporterService;
import net.geoprism.data.etl.TargetBinding;
import net.geoprism.ontology.Classifier;

public class MappableClass extends MappableClassBase 
{
  private static class LabelComparator implements Comparator<MdAttributeDAOIF>
  {
    private Locale locale;

    public LabelComparator(Locale _locale)
    {
      this.locale = _locale;
    }

    @Override
    public int compare(MdAttributeDAOIF o1, MdAttributeDAOIF o2)
    {
      return o1.getDisplayLabel(locale).compareTo(o2.getDisplayLabel(locale));
    }
  }

  private static final long serialVersionUID = -931571965;

  public MappableClass()
  {
    super();
  }

  @Override
  protected String buildKey()
  {
    MdClass mdClass = this.getWrappedMdClass();

    if (mdClass != null)
    {
      return mdClass.getKey();
    }

    return super.buildKey();
  }

  /**
   * MdMethod
   * 
   * Exports the dataset to all available exporters. (currently only DHIS2, if the DHIS2 exporter plugin is included)
   */
  @Override
  public void xport()
  {
    MdClass mdClass = this.getWrappedMdClass();

    Iterator<GeoprismDatasetExporterIF> it = GeoprismDatasetExporterService.getAllExporters();

    while (it.hasNext())
    {
      GeoprismDatasetExporterIF exporter = it.next();

      exporter.xport(mdClass);
    }
  }

  @Override
  @Transaction
  public void delete()
  {
    List<ConfigurationIF> configurations = ConfigurationService.getConfigurations();

    for (ConfigurationIF configuration : configurations)
    {
      configuration.onMappableClassDelete(this);
    }

    MdClass mdClass = this.getWrappedMdClass();

    if (mdClass.getGenerateSource())
    {
      String msg = "Cannot delete types that have generated source.";

      RequiredMappableClassException ex = new RequiredMappableClassException(msg);
      ex.setDataSetLabel(mdClass.getDisplayLabel().getValue());

      throw ex;
    }

    /*
     * Delete all layers which reference attributes on this type
     */
    MetadataWrapperQuery query = new MetadataWrapperQuery(new QueryFactory());
    query.WHERE(query.getWrappedMdClass().EQ(mdClass));

    OIterator<? extends MetadataWrapper> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        MetadataWrapper wrapper = iterator.next();
        wrapper.delete();
      }
    }
    finally
    {
      iterator.close();
    }

    /*
     * Delete all import mappings if they exist
     */
    TargetBinding binding = TargetBinding.getBinding(mdClass.definesType());

    if (binding != null)
    {
      binding.delete();
    }

    /*
     * Delete all geo nodes
     */
    OIterator<? extends GeoNode> nodes = this.getAllGeoNode();

    try
    {
      while (nodes.hasNext())
      {
        GeoNode node = nodes.next();
        node.delete();
      }
    }
    finally
    {
      nodes.close();
    }

    /*
     * Delete the corresponding MappableAttributes
     */
    List<MappableAttribute> mAttributes = MappableAttribute.getMappableAttributes(mdClass);

    for (MappableAttribute mAttribute : mAttributes)
    {
      mAttribute.delete();
    }

    super.delete();

    /*
     * Delete all of the data views which reference this type
     */
    List<String> viewNames = Database.getReferencingViews(MdElementDAO.getMdElementDAO(mdClass.definesType()));

    DatabaseUtil.dropViews(viewNames);

    mdClass.delete();
  }

  public static MappableClass getMappableClass(String type)
  {
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(type);

    return getMappableClass(mdClass);
  }

  public static MappableClass getMappableClass(MdClassDAOIF _mdClass)
  {
    MdClass mdClass = MdClass.get(_mdClass.getOid());

    MappableClassQuery query = new MappableClassQuery(new QueryFactory());
    query.WHERE(query.getWrappedMdClass().EQ(mdClass));

    OIterator<? extends MappableClass> iterator = query.getIterator();

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

  public static MappableClass[] getAll()
  {
    MappableClassQuery query = new MappableClassQuery(new QueryFactory());
    query.ORDER_BY_ASC(query.getWrappedMdClass().getDisplayLabel().localize());

    OIterator<? extends MappableClass> iterator = query.getIterator();

    try
    {
      List<? extends MappableClass> classes = iterator.getAll();

      return classes.toArray(new MappableClass[classes.size()]);
    }
    finally
    {
      iterator.close();
    }
  }

  public JSONObject toJSON() throws JSONException
  {
    return this.toJSON(null, new LinkedList<MetadataWrapper>());
  }

  public JSONObject toJSON(Dashboard dashboard) throws JSONException
  {
    List<? extends MetadataWrapper> wrappers = dashboard.getAllMetadata().getAll();

    return toJSON(dashboard, wrappers);
  }

  public JSONObject toJSON(Dashboard dashboard, List<? extends MetadataWrapper> wrappers) throws JSONException
  {
    boolean value = this.isSelected(wrappers);

    MdClassDAOIF mdClass = (MdClassDAOIF) MdClassDAO.get(this.getWrappedMdClassId());
    String label = mdClass.getDisplayLabel(Session.getCurrentLocale());
    String description = mdClass.getDescription(Session.getCurrentLocale());

    JSONObject object = new JSONObject();
    object.put("label", label);
    object.put("description", description);
    object.put("oid", this.getOid());
    object.put("type", mdClass.getKey());
    object.put("value", value);
    object.put("source", this.getDataSource());

    LinkedList<AttributeWrapper> attributes = new LinkedList<AttributeWrapper>();

    if (dashboard != null)
    {
      MetadataWrapper wrapper = dashboard.getMetadataWrapper(mdClass);

      if (wrapper != null)
      {
        attributes.addAll(wrapper.getAllAttributeWrapper().getAll());
      }
    }

    object.put("attributes", this.getAttributeJSON(mdClass, attributes));

    JSONArray aggregations = new JSONArray();
    aggregations.put(this.getAggregationJSON(IndicatorAggregateFunction.AVG));
    aggregations.put(this.getAggregationJSON(IndicatorAggregateFunction.COUNT));
    aggregations.put(this.getAggregationJSON(IndicatorAggregateFunction.MAX));
    aggregations.put(this.getAggregationJSON(IndicatorAggregateFunction.MIN));
    aggregations.put(this.getAggregationJSON(IndicatorAggregateFunction.STDEV));
    aggregations.put(this.getAggregationJSON(IndicatorAggregateFunction.SUM));

    object.put("aggregations", aggregations);

    return object;
  }

  private JSONObject getAggregationJSON(IndicatorAggregateFunction func) throws JSONException
  {
    JSONObject aggregation = new JSONObject();
    aggregation.put("value", func.getDisplayLabel());
    aggregation.put("oid", func.getEnumName());

    return aggregation;
  }

  private boolean isSelected(List<? extends MetadataWrapper> wrappers)
  {
    boolean selected = false;

    for (MetadataWrapper wrapper : wrappers)
    {
      if (wrapper.getWrappedMdClassId().equals(this.getWrappedMdClassId()))
      {
        selected = true;
      }
    }
    return selected;
  }

  public static String getClassesAsJSON(String dashboardId)
  {
    Dashboard dashboard = Dashboard.get(dashboardId);
    List<? extends MetadataWrapper> wrappers = dashboard.getAllMetadata().getAll();

    MappableClassQuery query = new MappableClassQuery(new QueryFactory());
    query.ORDER_BY_ASC(query.getWrappedMdClass().getDisplayLabel().localize());

    OIterator<? extends MappableClass> it = query.getIterator();

    try
    {
      JSONArray array = new JSONArray();

      while (it.hasNext())
      {
        MappableClass mClass = it.next();

        array.put(mClass.toJSON(dashboard, wrappers));
      }

      return array.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
    finally
    {
      it.close();
    }

  }

  public static void assign(Dashboard dashboard, JSONArray types)
  {
    try
    {
      int index = dashboard.getMaxOrder() + 1;

      for (int i = 0; i < types.length(); i++)
      {
        JSONObject type = types.getJSONObject(i);
        String oid = type.getString("oid");
        boolean checked = type.getBoolean("value");

        MappableClass mClass = MappableClass.get(oid);
        MdClass mdClass = mClass.getWrappedMdClass();

        MetadataWrapperQuery query = new MetadataWrapperQuery(new QueryFactory());
        query.WHERE(query.getDashboard().EQ(dashboard));
        query.AND(query.getWrappedMdClass().EQ(mdClass));

        if (checked)
        {
          if (query.getCount() == 0)
          {
            MetadataWrapper wrapper = new MetadataWrapper();
            wrapper.setWrappedMdClass(mdClass);
            wrapper.setDashboard(dashboard);
            wrapper.apply();

            DashboardMetadata metadata = dashboard.addMetadata(wrapper);
            metadata.setListOrder(index + i);
            metadata.apply();

            // Add all of the attributes
            JSONArray attributes = type.getJSONArray("attributes");

            MappableClass.assign(dashboard, wrapper, attributes);
          }
          else
          {
            OIterator<? extends MetadataWrapper> iterator = query.getIterator();

            try
            {
              while (iterator.hasNext())
              {
                MetadataWrapper wrapper = iterator.next();

                // Add all of the attributes
                JSONArray attributes = type.getJSONArray("attributes");

                MappableClass.assign(dashboard, wrapper, attributes);
              }
            }
            finally
            {
              iterator.close();
            }
          }
        }
        else if (!checked && query.getCount() > 0)
        {
          OIterator<? extends MetadataWrapper> iterator = query.getIterator();

          try
          {
            while (iterator.hasNext())
            {
              MetadataWrapper wrapper = iterator.next();
              wrapper.delete();
            }
          }
          finally
          {
            iterator.close();
          }
        }

      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private static void assign(Dashboard dashboard, MetadataWrapper wrapper, JSONArray attributes)
  {
    try
    {
      int index = wrapper.getMaxOrder() + 1;

      for (int i = 0; i < attributes.length(); i++)
      {
        JSONObject type = attributes.getJSONObject(i);
        String oid = type.getString("oid");
        boolean checked = type.getBoolean("selected");

        MdAttribute mdAttribute = MdAttribute.get(oid);

        QueryFactory factory = new QueryFactory();

        MetadataWrapperQuery wQuery = new MetadataWrapperQuery(factory);
        wQuery.WHERE(wQuery.getOid().EQ(wrapper.getOid()));

        AttributeWrapperQuery query = new AttributeWrapperQuery(factory);
        query.WHERE(query.dashboardMetadata(wQuery));
        query.AND(query.getWrappedMdAttribute().EQ(mdAttribute));

        if (checked && query.getCount() == 0)
        {
          AttributeWrapper attribute = new AttributeWrapper();
          attribute.setWrappedMdAttribute(mdAttribute);
          attribute.apply();

          DashboardAttributes dAttribute = wrapper.addAttributeWrapper(attribute);
          dAttribute.setListOrder(index + i);
          dAttribute.apply();
        }
        else if (!checked && query.getCount() > 0)
        {
          OIterator<? extends AttributeWrapper> iterator = query.getIterator();

          try
          {
            while (iterator.hasNext())
            {
              AttributeWrapper attribute = iterator.next();
              attribute.delete(dashboard);
            }
          }
          finally
          {
            iterator.close();
          }
        }
      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private JSONArray getAttributeJSON(MdClassDAOIF mdClass, List<? extends AttributeWrapper> attributes) throws JSONException
  {
    JSONArray array = new JSONArray();

    Collection<String> ids = this.getGeoNodeAttributeIds();

    List<? extends MdAttributeDAOIF> mdAttributes = mdClass.getAllDefinedMdAttributes();

    Collections.sort(mdAttributes, new LabelComparator(Session.getCurrentLocale()));

    for (MdAttributeDAOIF mdAttribute : mdAttributes)
    {
      if (this.isValid(mdAttribute, ids))
      {
        JSONObject object = getAttributeJSON(mdAttribute, attributes);

        array.put(object);
      }
    }

    return array;
  }

  private static JSONObject getAttributeJSON(MdAttributeDAOIF mdAttribute, List<? extends AttributeWrapper> attributes)
  {
    boolean selected = MappableClass.isSelected(mdAttribute, attributes);

    JSONObject object = new JSONObject();
    object.put("label", mdAttribute.getDisplayLabel(Session.getCurrentLocale()));
    object.put("oid", mdAttribute.getOid());
    object.put("selected", selected);
    object.put("type", mdAttribute.getMdBusinessDAO().getTypeName());
    object.put("numeric", ( mdAttribute instanceof MdAttributeNumberDAOIF || mdAttribute instanceof MdAttributeBooleanDAOIF ));

    if (mdAttribute instanceof MdAttributeTermDAOIF)
    {
      MdAttributeTermDAOIF mdAttributeTerm = (MdAttributeTermDAOIF) mdAttribute;
      MdTermDAOIF mdBusiness = mdAttributeTerm.getReferenceMdBusinessDAO();

      if (mdBusiness.definesType().equals(Classifier.CLASS))
      {
        Classifier classifier = Classifier.findClassifierRoot(mdAttributeTerm);

        if (classifier != null)
        {
          JSONObject root = new JSONObject();
          root.put("oid", classifier.getOid());
          root.put("label", classifier.getDisplayLabel().getValue());

          object.put("type", "Category");
          object.put("root", root);
        }
      }
    }
    return object;
  }

  private Collection<String> getGeoNodeAttributeIds()
  {
    Collection<String> collection = new TreeSet<String>();

    OIterator<? extends GeoNode> iterator = this.getAllGeoNode();

    try
    {
      while (iterator.hasNext())
      {
        GeoNode node = iterator.next();

        collection.add(node.getGeoEntityAttributeId());

        if (node instanceof GeoNodeGeometry)
        {
          GeoNodeGeometry geometry = (GeoNodeGeometry) node;

          collection.add(geometry.getDisplayLabelAttributeId());
          collection.add(geometry.getIdentifierAttributeId());
          collection.add(geometry.getGeometryAttributeId());
          collection.add(geometry.getMultiPolygonAttributeId());
          collection.add(geometry.getPointAttributeId());
        }
      }

    }
    finally
    {
      iterator.close();
    }

    return collection;
  }

  private boolean isValid(MdAttributeDAOIF mdAttribute, Collection<String> ids)
  {
    MdAttributeConcreteDAOIF mdAttributeConcrete = mdAttribute.getMdAttributeConcrete();

    if (mdAttributeConcrete instanceof MdAttributeReferenceDAOIF)
    {
      MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttributeConcrete;

      MdBusinessDAOIF referenceMdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();

      if (referenceMdBusiness != null)
      {
        boolean isClassifier = referenceMdBusiness.definesType().equals(Classifier.CLASS);

        return isClassifier;
      }

      return false;
    }
    else if (mdAttributeConcrete instanceof MdAttributeIndicatorDAOIF)
    {
      return true;
    }

    return !mdAttributeConcrete.isSystem() && !mdAttributeConcrete.definesAttribute().equals(BusinessInfo.KEY) && ( mdAttributeConcrete instanceof MdAttributePrimitiveDAOIF ) && !ids.contains(mdAttributeConcrete.getOid());
  }

  private static boolean isSelected(MdAttributeDAOIF mdAttribute, List<? extends AttributeWrapper> attributes)
  {
    for (AttributeWrapper attribute : attributes)
    {
      if (mdAttribute.getOid().equals(attribute.getWrappedMdAttributeId()))
      {
        return true;
      }
    }

    return false;
  }

  public static Collection<String> getLayersToDelete(Dashboard dashboard, JSONArray types)
  {
    try
    {
      Collection<String> layerNames = new HashSet<String>();

      for (int i = 0; i < types.length(); i++)
      {
        JSONObject type = types.getJSONObject(i);
        String oid = type.getString("oid");
        boolean checked = type.getBoolean("value");

        MappableClass mClass = MappableClass.get(oid);
        MdClass mdClass = mClass.getWrappedMdClass();

        MetadataWrapperQuery query = new MetadataWrapperQuery(new QueryFactory());
        query.WHERE(query.getDashboard().EQ(dashboard));
        query.AND(query.getWrappedMdClass().EQ(mdClass));

        if (checked && query.getCount() != 0)
        {
          OIterator<? extends MetadataWrapper> iterator = query.getIterator();

          try
          {
            while (iterator.hasNext())
            {
              MetadataWrapper wrapper = iterator.next();

              JSONArray attributes = type.getJSONArray("attributes");

              layerNames.addAll(MappableClass.getLayersToDelete(dashboard, wrapper, attributes));
            }
          }
          finally
          {
            iterator.close();
          }
        }
        else if (!checked && query.getCount() > 0)
        {
          OIterator<? extends MetadataWrapper> iterator = query.getIterator();

          try
          {
            while (iterator.hasNext())
            {
              MetadataWrapper wrapper = iterator.next();
              layerNames.addAll(wrapper.getLayersToDelete());
            }
          }
          finally
          {
            iterator.close();
          }
        }
      }

      return layerNames;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private static Collection<String> getLayersToDelete(Dashboard dashboard, MetadataWrapper wrapper, JSONArray attributes)
  {
    Collection<String> layerNames = new HashSet<String>();

    try
    {
      for (int i = 0; i < attributes.length(); i++)
      {
        JSONObject type = attributes.getJSONObject(i);
        String oid = type.getString("oid");
        boolean checked = type.getBoolean("selected");

        MdAttribute mdAttribute = MdAttribute.get(oid);

        QueryFactory factory = new QueryFactory();

        MetadataWrapperQuery wQuery = new MetadataWrapperQuery(factory);
        wQuery.WHERE(wQuery.getOid().EQ(wrapper.getOid()));

        AttributeWrapperQuery query = new AttributeWrapperQuery(factory);
        query.WHERE(query.dashboardMetadata(wQuery));
        query.AND(query.getWrappedMdAttribute().EQ(mdAttribute));

        if (!checked && query.getCount() > 0)
        {
          OIterator<? extends AttributeWrapper> iterator = query.getIterator();

          try
          {
            while (iterator.hasNext())
            {
              AttributeWrapper attribute = iterator.next();
              layerNames.addAll(attribute.getLayersToDelete(dashboard));
            }
          }
          finally
          {
            iterator.close();
          }
        }
      }

      return layerNames;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public String getAsJSON()
  {
    JSONObject dataset = null;
    try
    {
      dataset = this.toJSON();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }

    return dataset.toString();
  }

  public static String getAllAsJSON()
  {
    try
    {
      JSONObject jObject = new JSONObject();

      jObject.put("canExport", GeoprismDatasetExporterService.getAllExporters().hasNext());

      JSONArray array = new JSONArray();

      MappableClass[] mClasses = MappableClass.getAll();

      for (MappableClass mClass : mClasses)
      {
        array.put(mClass.toJSON());
      }

      jObject.put("datasets", array);

      return jObject.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Transaction
  @Authenticate
  public static void applyDatasetUpdate(String dataset)
  {
    try
    {
      JSONObject object = new JSONObject(dataset);
      String label = object.getString("label");
      String oid = object.getString("oid");
      String description = object.getString("description");
      String source = object.getString("source");

      MappableClass ds = MappableClass.get(oid);
      ds.lock();
      ds.setDataSource(source);
      ds.apply();

      MdClass mdClass = ds.getWrappedMdClass();
      mdClass.lock();
      mdClass.getDisplayLabel().setValue(label);
      mdClass.getDescription().setValue(description);
      mdClass.apply();

      if (object.has("attributes"))
      {
        JSONArray attributes = object.getJSONArray("attributes");

        for (int i = 0; i < attributes.length(); i++)
        {
          JSONObject attribute = attributes.getJSONObject(i);
          String attributeId = attribute.getString("oid");
          String attributeLabel = attribute.getString("label");

          MdAttributeConcrete mdAttribute = MdAttributeConcrete.lock(attributeId);
          mdAttribute.getDisplayLabel().setValue(attributeLabel);
          mdAttribute.apply();
        }
      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Transaction
  @Authenticate
  public static void remove(String oid)
  {
    MappableClass mClass = MappableClass.get(oid);
    mClass.delete();
  }

  public Universal getCountry()
  {
    QueryFactory factory = new QueryFactory();

    ClassUniversalQuery cuQuery = new ClassUniversalQuery(factory);
    cuQuery.WHERE(cuQuery.getParent().EQ(this));

    AllowedInAllPathsTableQuery aptQuery = new AllowedInAllPathsTableQuery(factory);
    aptQuery.WHERE(aptQuery.getChildTerm().EQ(cuQuery.getChild()));

    AllowedInQuery aiQuery = new AllowedInQuery(factory);
    aiQuery.WHERE(aiQuery.getParent().EQ(Universal.getRoot()));

    UniversalQuery uQuery = new UniversalQuery(factory);
    uQuery.WHERE(uQuery.allowedIn(aiQuery));
    uQuery.AND(uQuery.EQ(aptQuery.getParentTerm()));

    OIterator<? extends Universal> iterator = null;

    try
    {
      iterator = uQuery.getIterator();

      if (iterator.hasNext())
      {
        Universal universal = iterator.next();

        return universal;
      }
    }
    finally
    {
      if (iterator != null)
      {
        iterator.close();
      }
    }

    return null;
  }

  @Authenticate
  @Transaction
  public static String addIndicator(String datasetId, String indicator)
  {
    try
    {
      MappableClass mClass = MappableClass.get(datasetId);
      MdClass mdClass = mClass.getWrappedMdClass();

      MdAttributeIndicatorDAO mdAttribute = MappableClass.addIndicator(mdClass, new JSONObject(indicator));

      JSONObject response = MappableClass.getAttributeJSON(mdAttribute, new LinkedList<>());

      return response.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static MdAttributeIndicatorDAO addIndicator(MdClass mdClass, JSONObject indicator) throws JSONException
  {
    String displayLabel = indicator.getString("label");
    String name = DataUploader.getSystemName(displayLabel, "Attribute", false);
    String percentage = indicator.has("percentage") ? indicator.getString("percentage") : "false";
    String oid = indicator.has("oid") ? indicator.getString("oid") : "";

    if (oid.length() > 0)
    {
      MdAttributeIndicatorDAO mdAttributeIndicator = (MdAttributeIndicatorDAO) MdAttributeIndicatorDAO.get(oid).getBusinessDAO();
      IndicatorCompositeDAO composite = (IndicatorCompositeDAO) mdAttributeIndicator.getIndicator().getBusinessDAO();

      IndicatorPrimitiveDAO left = MappableClass.createIndicator(indicator.getJSONObject("left"), (IndicatorPrimitiveDAO) composite.getLeftOperand().getBusinessDAO());
      left.apply();

      IndicatorPrimitiveDAO right = MappableClass.createIndicator(indicator.getJSONObject("right"), (IndicatorPrimitiveDAO) composite.getRightOperand().getBusinessDAO());
      right.apply();

      composite.setValue(IndicatorCompositeInfo.PERCENTAGE, percentage);
      composite.apply();

      return mdAttributeIndicator;
    }
    else
    {
      IndicatorPrimitiveDAO left = MappableClass.createIndicator(indicator.getJSONObject("left"), IndicatorPrimitiveDAO.newInstance());
      left.apply();

      IndicatorPrimitiveDAO right = MappableClass.createIndicator(indicator.getJSONObject("right"), IndicatorPrimitiveDAO.newInstance());
      right.apply();

      IndicatorCompositeDAO composite = IndicatorCompositeDAO.newInstance();
      composite.setValue(IndicatorCompositeInfo.LEFT_OPERAND, left.getOid());
      composite.setValue(IndicatorCompositeInfo.OPERATOR, IndicatorOperator.DIV.getOid());
      composite.setValue(IndicatorCompositeInfo.RIGHT_OPERAND, right.getOid());
      composite.setValue(IndicatorCompositeInfo.PERCENTAGE, percentage);
      composite.apply();

      MdAttributeIndicatorDAO mdAttributeDAO = MdAttributeIndicatorDAO.newInstance();
      mdAttributeDAO.setValue(MdAttributeIndicatorInfo.NAME, name);
      mdAttributeDAO.setStructValue(MdAttributeIndicatorInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, displayLabel);
      mdAttributeDAO.setValue(MdAttributeIndicatorInfo.INDICATOR_ELEMENT, composite.getOid());
      mdAttributeDAO.setValue(MdAttributeIndicatorInfo.DEFINING_MD_CLASS, mdClass.getOid());
      mdAttributeDAO.apply();

      MdAttribute mdAttribute = (MdAttribute) BusinessFacade.get(mdAttributeDAO);

      MappableAttribute mAttribute = new MappableAttribute();
      mAttribute.setWrappedMdAttribute(mdAttribute);
      mAttribute.setAggregatable(false);
      mAttribute.apply();

      return mdAttributeDAO;
    }
  }

  private static IndicatorPrimitiveDAO createIndicator(JSONObject indicator, IndicatorPrimitiveDAO primitive) throws JSONException
  {
    String aggregation = indicator.getString("aggregation");
    String attributeId = indicator.getString("attribute");

    primitive.setValue(IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE, attributeId);
    primitive.setValue(IndicatorPrimitiveInfo.INDICATOR_FUNCTION, IndicatorAggregateFunction.valueOf(aggregation).getOid());

    return primitive;
  }

  @Transaction
  public static void removeIndicator(String oid)
  {

    /*
     * Delete the corresponding MappableAttributes
     */
    MappableAttribute mAttribute = MappableAttribute.getMappableAttribute(oid);

    if (mAttribute != null)
    {
      mAttribute.delete();
    }

    // Get and delete a new instance of the indicator because it might be stale if the web indicator is deleted
    MdAttributeIndicatorDAO.get(oid).getBusinessDAO().delete();
  }

  @Transaction
  public static String lockIndicator(String oid)
  {
    try
    {
      MdAttributeIndicator mdAttributeIndicator = MdAttributeIndicator.lock(oid);
      MdAttributeIndicatorDAOIF mdAttribute = (MdAttributeIndicatorDAOIF) MdAttributeIndicatorDAO.get(oid);

      IndicatorCompositeDAOIF composite = (IndicatorCompositeDAOIF) mdAttribute.getIndicator();
      JSONObject response = new JSONObject();
      response.put("oid", mdAttributeIndicator.getOid());
      response.put("label", mdAttributeIndicator.getDisplayLabel().getValue());
      response.put("percentage", composite.isPercentage());
      response.put("left", MappableClass.serializeIndicator((IndicatorPrimitiveDAOIF) composite.getLeftOperand()));
      response.put("right", MappableClass.serializeIndicator((IndicatorPrimitiveDAOIF) composite.getRightOperand()));

      return response.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private static JSONObject serializeIndicator(IndicatorPrimitiveDAOIF element) throws JSONException
  {
    JSONObject indicator = new JSONObject();
    indicator.put("aggregation", element.getAggregateFunction().getName());
    indicator.put("attribute", element.getMdAttributePrimitive().getOid());

    return indicator;
  }
}