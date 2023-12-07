/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.registry.service.business;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.resource.ApplicationResource;
import com.runwaysdk.system.AbstractClassification;

import net.geoprism.registry.CannotDeleteClassificationWithChildrenException;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationNode;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.query.graph.AbstractVertexRestriction;
import net.geoprism.registry.view.Page;

@Service
public class ClassificationBusinessService implements ClassificationBusinessServiceIF
{
  @Autowired
  private ClassificationTypeBusinessServiceIF typeService;

  @Override
  public void populate(Classification classification, JsonObject object)
  {
    classification.setCode(object.get(AbstractClassification.CODE).getAsString());

    LocalizedValue displayLabel = LocalizedValue.fromJSON(object.get(AbstractClassification.DISPLAYLABEL).getAsJsonObject());
    classification.setDisplayLabel(displayLabel);

    LocalizedValue description = LocalizedValue.fromJSON(object.get(AbstractClassification.DESCRIPTION).getAsJsonObject());
    classification.setDescription(description);
  }

  @Override
  public void populate(Classification classification, Term term)
  {
    classification.setCode(term.getCode());
    classification.setDisplayLabel(term.getLabel());
    classification.setDescription(term.getDescription());
  }

  @Transaction
  @Override
  public void apply(Classification classification, Classification parent)
  {
    boolean isNew = classification.getVertex().isNew() && !classification.getVertex().isAppliedToDb();

    classification.getVertex().apply();

    if (isNew)
    {
      if (parent != null)
      {
        this.addParent(classification, parent);
      }
      else
      {
        classification.getType().setRoot(classification);
      }
    }
  }

  @Transaction
  @Override
  public void delete(Classification classification)
  {
    if (this.getChildren(classification, null, null).getCount() > 0)
    {
      throw new CannotDeleteClassificationWithChildrenException();
    }

    classification.getVertex().delete();
  }

  @Transaction
  @Override
  public void addParent(Classification classification, Classification parent)
  {
    if (classification.getVertex().isNew() || !this.exists(classification, parent))
    {
      EdgeObject edge = classification.getVertex().addParent(parent.getVertex(), classification.getType().getMdEdge());
      edge.apply();
    }
  }

  @Transaction
  @Override
  public void addChild(Classification classification, Classification child)
  {
    this.addParent(child, classification);
  }

  @Transaction
  @Override
  public void removeParent(Classification classification, Classification parent)
  {
    classification.getVertex().removeParent(parent.getVertex(), classification.getType().getMdEdge());
  }

  @Transaction
  @Override
  public void removeChild(Classification classification, Classification child)
  {
    this.removeParent(child, classification);
  }

  @Transaction
  @Override
  public void move(Classification classification, Classification newParent)
  {
    this.getParents(classification).forEach(parent -> {
      this.removeParent(classification, parent);
    });

    this.addParent(classification, newParent);
  }

  @Override
  public Page<Classification> getChildren(Classification classification)
  {
    return this.getChildren(classification, 20, 1);
  }

  @Override
  public Page<Classification> getChildren(Classification classification, Integer pageSize, Integer pageNumber)
  {
    StringBuilder cStatement = new StringBuilder();
    cStatement.append("SELECT out('" + classification.getType().getMdEdge().getDBClassName() + "').size()");
    cStatement.append(" FROM :rid");

    GraphQuery<Integer> cQuery = new GraphQuery<Integer>(cStatement.toString());
    cQuery.setParameter("rid", classification.getVertex().getRID());

    Integer count = cQuery.getSingleResult();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT EXPAND(out('" + classification.getType().getMdEdge().getDBClassName() + "')");
    statement.append(") FROM :rid");
    statement.append(" ORDER BY code");

    if (pageSize != null && pageNumber != null)
    {
      int first = pageSize * ( pageNumber - 1 );
      int rows = pageSize;

      statement.append(" SKIP " + first + " LIMIT " + rows);
    }

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", classification.getVertex().getRID());

    List<Classification> results = query.getResults().stream().map(vertex -> {
      return new Classification(classification.getType(), vertex);
    }).collect(Collectors.toList());

    return new Page<Classification>(count, pageNumber, pageSize, results);
  }

  @Override
  public List<Classification> getParents(Classification classification)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("SELECT EXPAND(in('" + classification.getType().getMdEdge().getDBClassName() + "')");
    statement.append(") FROM :rid");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", classification.getVertex().getRID());

    List<Classification> results = query.getResults().stream().map(vertex -> {
      return new Classification(classification.getType(), vertex);
    }).collect(Collectors.toList());

    return results;
  }

  @Override
  public List<Classification> getAncestors(Classification classification, String rootCode)
  {
    GraphQuery<VertexObject> query = null;

    if (rootCode != null && rootCode.length() > 0)
    {
      StringBuilder statement = new StringBuilder();
      statement.append("SELECT expand($res)");
      statement.append(" LET $a = (TRAVERSE in(\"" + classification.getType().getMdEdge().getDBClassName() + "\") FROM :rid WHILE (code != :code))");
      statement.append(", $b = (SELECT FROM " + classification.getType().getMdVertex().getDBClassName() + " WHERE code = :code)");
      statement.append(", $res = (UNIONALL($a,$b))");

      query = new GraphQuery<VertexObject>(statement.toString());
      query.setParameter("rid", classification.getVertex().getRID());
      query.setParameter("code", rootCode);
    }
    else
    {
      StringBuilder statement = new StringBuilder();
      statement.append("TRAVERSE in(\"" + classification.getType().getMdEdge().getDBClassName() + "\") FROM :rid");

      query = new GraphQuery<VertexObject>(statement.toString());
      query.setParameter("rid", classification.getVertex().getRID());
    }

    List<Classification> results = query.getResults().stream().map(vertex -> {
      return new Classification(classification.getType(), vertex);
    }).collect(Collectors.toList());

    return results;
  }

  private boolean exists(Classification classification, Classification parent)
  {
    EdgeObject edge = this.getEdge(classification, parent);

    return ( edge != null );
  }

  @Override
  public EdgeObject getEdge(Classification classification, Classification parent)
  {
    String statement = "SELECT FROM " + classification.getType().getMdEdge().getDBClassName();
    statement += " WHERE out = :parent";
    statement += " AND in = :child";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("parent", parent.getVertex().getRID());
    query.setParameter("child", classification.getVertex().getRID());

    return query.getSingleResult();
  }

  @Override
  public ClassificationNode getAncestorTree(Classification classification, String rootCode, Integer pageSize)
  {
    List<Classification> ancestors = this.getAncestors(classification, rootCode);

    ClassificationNode prev = null;

    for (Classification ancestor : ancestors)
    {
      Page<Classification> page = this.getChildren(ancestor, pageSize, 1);

      List<ClassificationNode> transform = page.getResults().stream().map(r -> {
        return new ClassificationNode(r);
      }).collect(Collectors.toList());

      if (prev != null)
      {
        int index = transform.indexOf(prev);

        if (index != -1)
        {
          transform.set(index, prev);
        }
        else
        {
          transform.add(prev);
        }
      }

      ClassificationNode node = new ClassificationNode();
      node.setClassification(ancestor);
      node.setChildren(new Page<ClassificationNode>(page.getCount(), page.getPageNumber(), page.getPageSize(), transform));

      prev = node;
    }

    return prev;
  }

  @Override
  public Classification get(ClassificationType type, String code)
  {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT FROM " + type.getMdVertex().getDBClassName());
    builder.append(" WHERE code = :code");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(builder.toString());
    query.setParameter("code", code);

    VertexObject result = query.getSingleResult();

    if (result != null)
    {
      return new Classification(type, result);
    }

    return null;
  }

  @Override
  public Classification getByOid(ClassificationType type, String oid)
  {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT FROM " + type.getMdVertex().getDBClassName());
    builder.append(" WHERE oid = :oid");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(builder.toString());
    query.setParameter("oid", oid);

    VertexObject result = query.getSingleResult();

    if (result != null)
    {
      return new Classification(type, result);
    }

    return null;
  }

  @Override
  public Classification newInstance(ClassificationType type)
  {
    VertexObjectDAO dao = VertexObjectDAO.newInstance(type.getMdVertex());

    return new Classification(type, VertexObject.instantiate(dao));
  }

  @Override
  public Classification construct(ClassificationType type, JsonObject object, boolean isNew)
  {
    if (isNew)
    {
      return this.newInstance(type);
    }

    String code = object.get(AbstractClassification.CODE).getAsString();

    return this.get(type, code);
  }

  @Override
  public List<Classification> search(ClassificationType type, String rootCode, String text)
  {
    StringBuilder builder = new StringBuilder();

    if (rootCode != null && rootCode.length() > 0)
    {
      builder.append("SELECT FROM (TRAVERSE out(\"" + type.getMdEdge().getDBClassName() + "\") FROM :rid) ");
    }
    else
    {
      builder.append("SELECT FROM " + type.getMdVertex().getDBClassName());
    }

    if (text != null)
    {
      builder.append(" WHERE (code.toUpperCase() LIKE :text");
      builder.append(" OR " + AbstractVertexRestriction.localize("displayLabel") + ".toUpperCase() LIKE :text)");
    }

    builder.append(" ORDER BY code");
    builder.append(" LIMIT 10");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(builder.toString());

    if (text != null)
    {
      query.setParameter("text", "%" + text.toUpperCase() + "%");
    }

    if (rootCode != null && rootCode.length() > 0)
    {
      Classification root = this.get(type, rootCode);
      query.setParameter("rid", root.getVertex().getRID());
    }

    List<Classification> results = query.getResults().stream().map(vertex -> {
      return new Classification(type, vertex);
    }).collect(Collectors.toList());

    return results;
  }

  @Override
  public Classification get(AttributeClassificationType attribute, String code)
  {
    String classificationTypeCode = attribute.getClassificationType();
    ClassificationType type = this.typeService.getByCode(classificationTypeCode);

    return this.get(type, code);
  }

  @Override
  public JsonObject exportToJson(String classificationTypeCode, String code)
  {
    ClassificationType type = this.typeService.getByCode(classificationTypeCode);
    Classification classification = this.get(type, code);

    return exportToJson(classification);
  }

  private JsonObject exportToJson(Classification classification)
  {
    JsonArray children = new JsonArray();

    Page<Classification> page = this.getChildren(classification);

    for (Classification child : page)
    {
      children.add(exportToJson(child));
    }

    JsonObject json = classification.toJSON();
    json.add("children", children);
    return json;
  }

  @Override
  public void importJsonTree(String classificationTypeCode, ApplicationResource resource) throws IOException
  {
    ClassificationType type = this.typeService.getByCode(classificationTypeCode);

    try (InputStream stream = resource.openNewStream())
    {
      JsonObject object = JsonParser.parseReader(new InputStreamReader(stream, "UTF-8")).getAsJsonObject();

      importJsonTree(type, null, object);
    }
  }

  @Override
  @Transaction
  public void importJsonTree(ClassificationType type, Classification parent, JsonObject object)
  {
    Classification classification = this.construct(type, object, true);
    this.populate(classification, object);
    this.apply(classification, parent);

    JsonArray children = object.get("children").getAsJsonArray();

    for (int i = 0; i < children.size(); i++)
    {
      JsonObject child = children.get(i).getAsJsonObject();

      importJsonTree(type, classification, child);
    }
  }

}
