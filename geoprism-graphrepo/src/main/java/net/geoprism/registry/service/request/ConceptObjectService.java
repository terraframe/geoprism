/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.request;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.springframework.stereotype.Service;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.model.ConceptObject;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.service.business.ConceptClassBusinessServiceIF;
import net.geoprism.registry.service.business.ConceptObjectBusinessServiceIF;
import net.geoprism.registry.service.business.ConceptSetBusinessServiceIF;
import net.geoprism.registry.view.ConceptClassDTO;
import net.geoprism.registry.view.NodeDTO;
import net.geoprism.registry.view.ObjectOverTimeDTO;
import net.geoprism.registry.view.Page;

@Service
public class ConceptObjectService extends ObjectService<ConceptObject, ConceptClass, ConceptClassDTO>
{
  private final ConceptSetBusinessServiceIF cSetService;

  public ConceptObjectService(ConceptObjectBusinessServiceIF objectService, ConceptClassBusinessServiceIF cTypeService, ConceptSetBusinessServiceIF cSetService)
  {
    super(cTypeService, objectService);

    this.cSetService = cSetService;
  }

  protected ConceptObjectBusinessServiceIF getObjectService()
  {
    return (ConceptObjectBusinessServiceIF) super.getObjectService();
  }

  protected ConceptClassBusinessServiceIF getTypeService()
  {
    return (ConceptClassBusinessServiceIF) super.getTypeService();
  }

  @Request(RequestType.SESSION)
  public Page<ObjectOverTimeDTO> getChildren(String sessionId, String concept, String typeCode, String attributeName, Integer pageSize, Integer pageNumber)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(typeCode);

    return type.getAttribute(attributeName) //
        .map(a -> (AttributeClassificationType) a.toDTO()) //
        .map(a -> {
          ConceptObject object = this.getObjectService().getByCode(a, concept).orElseThrow(() -> {
            return new ProgrammingErrorException("Unable to find concept object with the code [" + concept + "]");
          });

          Integer count = this.getObjectService().getChildCount(object, a);
          List<ObjectOverTimeDTO> results = this.getObjectService().getChildren(object, a, pageSize, pageNumber).stream() //
              .map(c -> this.getObjectService().toDTO(c)) //
              .toList();

          return new Page<ObjectOverTimeDTO>(count, pageNumber, pageSize, results);
        }).orElse(new Page<ObjectOverTimeDTO>());

  }

  @Request(RequestType.SESSION)
  public List<ObjectOverTimeDTO> search(String sessionId, String typeCode, String attributeName, String text)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(typeCode);

    return type.getAttribute(attributeName) //
        .map(a -> (AttributeClassificationType) a.toDTO()) //
        .map(a -> {
          List<ConceptObject> children = this.getObjectService().search(a, text);

          return children.stream() //
              .map(c -> this.getObjectService().toDTO(c)) //
              .toList();
        }).orElse(new LinkedList<ObjectOverTimeDTO>());
  }

  @Request(RequestType.SESSION)
  public List<ObjectOverTimeDTO> search(String sessionId, String conceptClassCode, String text)
  {
    return this.getTypeService().getByCode(conceptClassCode) //
        .map(conceptClass -> {
          List<ConceptObject> children = this.getObjectService().search(conceptClass, text);

          return children.stream() //
              .map(c -> this.getObjectService().toDTO(c)) //
              .toList();
        }).orElse(new LinkedList<ObjectOverTimeDTO>());
  }

  @Request(RequestType.SESSION)
  public List<ObjectOverTimeDTO> search(String sessionId, String setCode, Date date, String text)
  {
    return this.cSetService.getByCode(setCode) //
        .map(set -> {
          List<ConceptObject> children = this.getObjectService().search(set, date, text);

          return children.stream() //
              .map(c -> this.getObjectService().toDTO(c)) //
              .toList();
        }).orElse(new LinkedList<ObjectOverTimeDTO>());
  }

  @Request(RequestType.SESSION)
  public NodeDTO<ObjectOverTimeDTO> getAncestorTree(String sessionId, String concept, String typeCode, String attributeName, Integer pageSize)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(typeCode);

    return type.getAttribute(attributeName) //
        .map(a -> (AttributeClassificationType) a.toDTO()) //
        .map(a -> {
          ConceptObject object = this.getObjectService().getByCode(a, concept).orElseThrow(() -> {
            return new ProgrammingErrorException("Unable to find concept object with the code [" + concept + "]");
          });

          return this.getObjectService().getAncestorTree(a, object, pageSize);
        }).orElse(new NodeDTO<ObjectOverTimeDTO>());

  }

}
