package net.geoprism.registry.business;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.DuplicateHierarchyTypeException;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.UndirectedGraphType;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.GeoVertex;

@Service
public class UndirectedGraphTypeBusinessService implements UndirectedGraphTypeBusinessServiceIF
{
  @Override
  public UndirectedGraphType create(JsonObject object)
  {
    String code = object.get(UndirectedGraphType.CODE).getAsString();
    LocalizedValue label = LocalizedValue.fromJSON(object.getAsJsonObject(UndirectedGraphType.JSON_LABEL));
    LocalizedValue description = LocalizedValue.fromJSON(object.getAsJsonObject(UndirectedGraphType.DESCRIPTION));

    return create(code, label, description);
  }

  @Override
  @Transaction
  public UndirectedGraphType create(String code, LocalizedValue label, LocalizedValue description)
  {
    try
    {
      MdVertexDAOIF mdBusGeoEntity = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS);

      MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
      mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.UNDIRECTED_GRAPH_PACKAGE);
      mdEdgeDAO.setValue(MdEdgeInfo.NAME, code);
      mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, mdBusGeoEntity.getOid());
      mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, mdBusGeoEntity.getOid());
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, label);
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, description);
      mdEdgeDAO.setValue(MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdEdgeDAO.apply();

      MdAttributeDateTimeDAO startDate = MdAttributeDateTimeDAO.newInstance();
      startDate.setValue(MdAttributeDateTimeInfo.NAME, GeoVertex.START_DATE);
      startDate.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Date");
      startDate.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Date");
      startDate.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      startDate.apply();

      MdAttributeDateTimeDAO endDate = MdAttributeDateTimeDAO.newInstance();
      endDate.setValue(MdAttributeDateTimeInfo.NAME, GeoVertex.END_DATE);
      endDate.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Date");
      endDate.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Date");
      endDate.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      endDate.apply();
      
      createPermissions(mdEdgeDAO);

      UndirectedGraphType graphType = new UndirectedGraphType();
      graphType.setCode(code);
      graphType.setMdEdgeId(mdEdgeDAO.getOid());
      RegistryLocalizedValueConverter.populate(graphType.getDisplayLabel(), label);
      RegistryLocalizedValueConverter.populate(graphType.getDescription(), description);
      graphType.apply();

      return graphType;
    }
    catch (DuplicateDataException ex)
    {
      DuplicateHierarchyTypeException ex2 = new DuplicateHierarchyTypeException();
      ex2.setDuplicateValue(code);
      throw ex2;
    }
  }
  
  protected void createPermissions(MdEdgeDAO mdEdgeDAO)
  {
    // None in the graph repo
  }
  
  @Override
  @Transaction
  public void update(UndirectedGraphType ugt, JsonObject object)
  {
    try
    {
      ugt.appLock();

      if (object.has(UndirectedGraphType.JSON_LABEL))
      {
        LocalizedValue label = LocalizedValue.fromJSON(object.getAsJsonObject(UndirectedGraphType.JSON_LABEL));

        RegistryLocalizedValueConverter.populate(ugt.getDisplayLabel(), label);
      }

      if (object.has(UndirectedGraphType.DESCRIPTION))
      {
        LocalizedValue description = LocalizedValue.fromJSON(object.getAsJsonObject(UndirectedGraphType.DESCRIPTION));

        RegistryLocalizedValueConverter.populate(ugt.getDescription(), description);
      }

      ugt.apply();
    }
    finally
    {
      ugt.unlock();
    }
  }

  @Override
  @Transaction
  public void delete(UndirectedGraphType ugt)
  {
    MdEdge mdEdge = ugt.getMdEdge();

    ugt.delete();

    mdEdge.delete();
  }
}
