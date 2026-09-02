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
package net.geoprism.registry.service.business;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.ConceptSet;
import net.geoprism.registry.graph.ConceptVertex;
import net.geoprism.registry.model.ConceptObject;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.view.ConceptClassDTO;
import net.geoprism.registry.view.NodeDTO;
import net.geoprism.registry.view.ObjectOverTimeDTO;
import net.geoprism.registry.view.Page;

@Service
public class ConceptObjectBusinessService extends ObjectEdgeBusinessService<ConceptObject, ConceptClass, ConceptClassDTO, ConceptEdgeType, ConceptObject> implements ConceptObjectBusinessServiceIF
{
  private final ConceptSetBusinessServiceIF   setService;

  private final ConceptClassBusinessServiceIF cClassService;

  public ConceptObjectBusinessService(ConceptClassBusinessServiceIF typeService, ConceptSetBusinessServiceIF setService, ConceptClassBusinessServiceIF cClassService)
  {
    super(typeService, ConceptVertex.CLASS);

    this.setService = setService;
    this.cClassService = cClassService;
  }

  @Override
  public ConceptObject newInstance(ConceptClass type)
  {
    VertexObject vertex = VertexObject.instantiate(VertexObjectDAO.newInstance(type.getMdVertexDAO()));

    return new ConceptObject(type, vertex, new TreeMap<>());
  }

  @Override
  protected ConceptObject build(ConceptClass type, VertexObject current, Map<String, List<VertexObject>> nodeMap, Date date)
  {
    return new ConceptObject(type, current, nodeMap, date);
  }

  @Override
  protected boolean isValidEdge(ConceptObject child, ConceptEdgeType type, ConceptObject parent, Date startDate, Date endDate)
  {
    boolean isValid = super.isValidEdge(child, type, parent, startDate, endDate);

    // TODO: Validate different types
    if (isValid)
    {
      // Concept edges must be a DAG
      return !this.isCycle(child, type, parent, startDate, endDate);
    }

    return true;
  }

  @Override
  public Optional<ConceptObject> getByCode(ConceptSet conceptSet, String code)
  {
    String rootOid = conceptSet.getRootTerm();

    if (StringUtils.isNotBlank(rootOid))
    {
      return this.getByOid(rootOid).map(rootTerm -> {
        List<ConceptEdgeType> edges = this.setService.getConceptEdgeTypes(conceptSet);
        String edgeNames = String.join(", ", edges.stream().map(e -> "'" + e.getMdEdge().getDbClassName() + "'").toList());

        StringBuilder statement = new StringBuilder();
        statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
        statement.append("  SELECT FROM (");
        statement.append("    TRAVERSE out(" + edgeNames + ") FROM " + rootTerm.getRID());
        statement.append("  )");
        statement.append("  WHERE code = :code");
        statement.append(")");

        GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
        query.setParameter("code", code);

        return this.processSingleResult(query.getResults(), null);
      });
    }
    else
    {
      Optional<ConceptObject> result = this.getCache().get(code, () -> {
        MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(ConceptVertex.CLASS);

        StringBuilder statement = new StringBuilder();
        statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
        statement.append("  SELECT FROM " + mdVertex.getDBClassName());
        statement.append("  WHERE code = :code");
        statement.append(")");

        GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
        query.setParameter("code", code);

        return Optional.ofNullable(this.processSingleResult(query.getResults(), null));
      });

      result.ifPresent(object -> {

        List<ConceptClass> classes = this.setService.getConceptClasses(conceptSet);

        if (!classes.contains(object.getType()))
        {
          throw new UnsupportedOperationException("The concept class [" + object.getType() + "] is not a member of the concept set");
        }
      });

      return result;

    }
  }

  @Override
  public Optional<ConceptObject> getByCode(AttributeClassificationType attribute, String code)
  {
    ConceptClass type = this.cClassService.getByCodeOrThrow(attribute.getRootTerm().getType());
    ConceptObject root = this.getByCode(type, attribute.getRootTerm().getCode()).get();

    String edgeNames = getEdgeNames(attribute);

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("  SELECT FROM (");
    statement.append("    TRAVERSE outE(" + edgeNames + ")[(:startDate BETWEEN startDate AND endDate)].in FROM " + root.getRID());
    statement.append("  )");
    statement.append("  WHERE code = :code");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("code", code);
    query.setParameter("startDate", attribute.getStartDate());

    return Optional.of(this.processSingleResult(query.getResults(), null));
  }

  @Override
  public List<ConceptObject> search(AttributeClassificationType attribute, String text)
  {
    ConceptClass type = this.cClassService.getByCodeOrThrow(attribute.getRootTerm().getType());
    ConceptObject root = this.getByCode(type, attribute.getRootTerm().getCode()).get();

    String edgeNames = getEdgeNames(attribute);

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("  SELECT FROM (");
    statement.append("    TRAVERSE outE(" + edgeNames + ")[(:startDate BETWEEN startDate AND endDate)].in FROM " + root.getRID());
    statement.append("  )");
    statement.append("  WHERE code.toUpperCase() LIKE :text");
    statement.append("  ORDER BY code");
    statement.append("  LIMIT 10");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("text", "%" + text.toUpperCase() + "%");
    query.setParameter("startDate", attribute.getStartDate());

    return this.processTraverseResults(query.getResults(), attribute.getStartDate());
  }

  @Override
  public List<ConceptObject> search(ConceptSet set, Date date, String text)
  {
    if (StringUtils.isNotBlank(set.getRootTerm()))
    {
      ConceptObject rootTerm = this.getByOid(set.getRootTerm()).orElseThrow(() -> new ProgrammingErrorException("Unable to find root term"));

      String edgeNames = getEdgeNames(set);

      StringBuilder statement = new StringBuilder();
      statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
      statement.append("  SELECT FROM (");
      statement.append("    TRAVERSE outE(" + edgeNames + ")[(:date BETWEEN startDate AND endDate)].in FROM :rid");
      statement.append("  )");
      statement.append("  WHERE code.toUpperCase() LIKE :text");
      statement.append("  ORDER BY code");
      statement.append("  LIMIT 10");
      statement.append(")");

      GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
      query.setParameter("text", "%" + text.toUpperCase() + "%");
      query.setParameter("date", date);
      query.setParameter("rid", rootTerm.getRID());

      return this.processTraverseResults(query.getResults(), date);
    }

    List<ConceptClass> conceptClasses = this.setService.getConceptClasses(set);

    if (conceptClasses.size() > 0)
    {
      return this.search(conceptClasses.get(0), text);
    }

    return new LinkedList<>();
  }

  @Override
  public List<ConceptObject> search(ConceptClass conceptClass, String text)
  {
    MdVertex mdVertex = conceptClass.getMdVertex();

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("  SELECT FROM " + mdVertex.getDbClassName());
    statement.append("  WHERE code.toUpperCase() LIKE :text");
    statement.append("  ORDER BY code");
    statement.append("  LIMIT 10");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("text", "%" + text.toUpperCase() + "%");

    return this.processTraverseResults(query.getResults(), null);
  }

  @Override
  public Integer getChildCount(ConceptObject object, AttributeClassificationType attribute)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("SELECT outE(" + this.getEdgeNames(attribute) + ")[(:date BETWEEN startDate AND endDate)].size()" + "\n");
    statement.append("FROM :rid " + "\n");

    GraphQuery<Integer> query = new GraphQuery<Integer>(statement.toString());
    query.setParameter("rid", object.getVertex().getRID());
    query.setParameter("date", attribute.getStartDate());

    return query.getSingleResult();
  }

  @Override
  public List<ConceptObject> getChildren(ConceptObject object, AttributeClassificationType attribute, Integer pageSize, Integer pageNumber)
  {
    int first = pageSize * ( pageNumber - 1 );
    int rows = pageSize;

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (" + "\n");
    statement.append("  SELECT EXPAND(outE(" + this.getEdgeNames(attribute) + ")[(:date BETWEEN startDate AND endDate)].in)" + "\n");
    statement.append("  FROM :rid " + "\n");
    statement.append("  ORDER BY code" + "\n");
    statement.append("  SKIP " + first + " LIMIT " + rows + "\n");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", object.getVertex().getRID());
    query.setParameter("date", attribute.getStartDate());

    return this.processTraverseResults(query.getResults(), attribute.getStartDate()).stream().sorted((a, b) -> {
      return a.getCode().compareTo(b.getCode());
    }).collect(Collectors.toList());
  }

  public String getEdgeNames(AttributeClassificationType attribute)
  {
    return getEdgeNames(this.setService.getByCodeOrThrow(attribute.getConceptSet()));
  }

  public String getEdgeNames(ConceptSet conceptSet)
  {
    List<ConceptEdgeType> edges = this.setService.getConceptEdgeTypes(conceptSet);

    return String.join(", ", edges.stream().map(e -> "'" + e.getMdEdge().getDbClassName() + "'").toList());
  }

  @Override
  public List<ConceptObject> getAncestors(AttributeClassificationType attribute, ConceptObject object)
  {
    ConceptClass type = this.cClassService.getByCodeOrThrow(attribute.getRootTerm().getType());
    ConceptObject root = this.getByCode(type, attribute.getRootTerm().getCode()).get();

    String edgeNames = getEdgeNames(attribute);

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("  SELECT FROM (");
    statement.append("    TRAVERSE inE(" + edgeNames + ")[(:startDate BETWEEN startDate AND endDate)].out FROM :rid WHILE $current != :root ");
    statement.append("  )");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("startDate", attribute.getStartDate());
    query.setParameter("rid", object.getRID());
    query.setParameter("root", root.getRID());

    List<ConceptObject> ancestors = this.processTraverseResults(query.getResults(), attribute.getStartDate());

    if (ancestors.size() == 0)
    {
      return new LinkedList<ConceptObject>(Arrays.asList(object));
    }

    return ancestors;
  }

  @Override
  public NodeDTO<ObjectOverTimeDTO> getAncestorTree(AttributeClassificationType attribute, ConceptObject object, Integer pageSize)
  {
    List<ConceptObject> ancestors = this.getAncestors(attribute, object);

    NodeDTO<ObjectOverTimeDTO> prev = null;

    for (ConceptObject ancestor : ancestors)
    {
      Integer count = this.getChildCount(ancestor, attribute);
      List<ConceptObject> children = this.getChildren(ancestor, attribute, pageSize, 1);

      List<NodeDTO<ObjectOverTimeDTO>> dtos = children.stream().map(r -> {
        return new NodeDTO<ObjectOverTimeDTO>(this.toDTO(r));
      }).collect(Collectors.toList());

      if (prev != null)
      {
        int index = dtos.indexOf(prev);

        if (index != -1)
        {
          dtos.set(index, prev);
        }
        else
        {
          dtos.add(prev);
        }
      }

      NodeDTO<ObjectOverTimeDTO> node = new NodeDTO<ObjectOverTimeDTO>();
      node.setObject(this.toDTO(ancestor));
      node.setChildren(new Page<NodeDTO<ObjectOverTimeDTO>>(count, 1, pageSize, dtos));

      prev = node;
    }

    return prev;
  }
}
