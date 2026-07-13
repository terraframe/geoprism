package net.geoprism.registry.service.request;

import java.util.List;

import org.springframework.stereotype.Component;

import net.geoprism.registry.graph.BusinessType;
import net.geoprism.registry.view.BusinessEdgeTypeView;
import net.geoprism.registry.view.BusinessTypeDTO;

@Component
public interface BusinessTypeServiceIF extends ObjectClassServiceIF<BusinessType, BusinessTypeDTO>
{

  List<BusinessEdgeTypeView> getEdgeTypes(String sessionId, String businessTypeCode);

}