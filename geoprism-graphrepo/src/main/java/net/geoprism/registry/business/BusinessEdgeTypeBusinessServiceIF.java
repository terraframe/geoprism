package net.geoprism.registry.business;

import java.util.List;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;

@Component
public interface BusinessEdgeTypeBusinessServiceIF
{

  BusinessType getParent(BusinessEdgeType edgeType);

  BusinessType getChild(BusinessEdgeType edgeType);

  void update(BusinessEdgeType edgeType, JsonObject object);

  void delete(BusinessEdgeType edgeType);

  JsonObject toJSON(BusinessEdgeType edgeType);

  List<BusinessEdgeType> getAll();

  BusinessEdgeType getByCode(String code);

  BusinessEdgeType getByMdEdge(MdEdge mdEdge);

  BusinessEdgeType create(JsonObject object);

  BusinessEdgeType create(String organizationCode, String code, LocalizedValue label, LocalizedValue description, String parentTypeCode, String childTypeCode);

}
