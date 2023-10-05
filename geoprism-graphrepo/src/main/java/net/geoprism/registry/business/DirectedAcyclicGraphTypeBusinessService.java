package net.geoprism.registry.business;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.stereotype.Component;

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

import net.geoprism.registry.DirectedAcyclicGraphType;
import net.geoprism.registry.DuplicateHierarchyTypeException;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.GeoVertex;

@Component
public class DirectedAcyclicGraphTypeBusinessService implements DirectedAcyclicGraphTypeBusinessServiceIF
{
  @Override
  @Transaction
  public void update(DirectedAcyclicGraphType dagt, JsonObject object)
  {
    try
    {
      dagt.appLock();

      if (object.has(DirectedAcyclicGraphType.DISPLAYLABEL))
      {
        LocalizedValue label = LocalizedValue.fromJSON(object.getAsJsonObject(DirectedAcyclicGraphType.DISPLAYLABEL));

        RegistryLocalizedValueConverter.populate(dagt.getDisplayLabel(), label);
      }

      if (object.has(DirectedAcyclicGraphType.DESCRIPTION))
      {
        LocalizedValue description = LocalizedValue.fromJSON(object.getAsJsonObject(DirectedAcyclicGraphType.DESCRIPTION));

        RegistryLocalizedValueConverter.populate(dagt.getDescription(), description);
      }

      dagt.apply();
    }
    finally
    {
      dagt.unlock();
    }
  }

  @Override
  @Transaction
  public void delete(DirectedAcyclicGraphType dagt)
  {
    MdEdge mdEdge = dagt.getMdEdge();

    dagt.delete();

    mdEdge.delete();
  }
  
  @Override
  public DirectedAcyclicGraphType create(DirectedAcyclicGraphType dagt, JsonObject object)
  {
    String code = object.get(DirectedAcyclicGraphType.CODE).getAsString();
    LocalizedValue label = LocalizedValue.fromJSON(object.getAsJsonObject(DirectedAcyclicGraphType.JSON_LABEL));
    LocalizedValue description = LocalizedValue.fromJSON(object.getAsJsonObject(DirectedAcyclicGraphType.DESCRIPTION));

    return create(code, label, description);
  }

  @Override
  @Transaction
  public DirectedAcyclicGraphType create(String code, LocalizedValue label, LocalizedValue description)
  {
    try
    {
      MdVertexDAOIF mdBusGeoEntity = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS);

      MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
      mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.DAG_PACKAGE);
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

      DirectedAcyclicGraphType graphType = new DirectedAcyclicGraphType();
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
}
