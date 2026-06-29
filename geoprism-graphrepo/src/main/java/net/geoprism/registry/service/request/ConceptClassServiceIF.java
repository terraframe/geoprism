package net.geoprism.registry.service.request;

import org.springframework.stereotype.Component;

import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.view.ConceptClassDTO;

@Component
public interface ConceptClassServiceIF extends ObjectClassServiceIF<ConceptClass, ConceptClassDTO>
{
}