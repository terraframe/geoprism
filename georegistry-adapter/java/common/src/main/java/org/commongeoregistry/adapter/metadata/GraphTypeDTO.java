/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Common Geo Registry Adapter(tm). If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.serialization.LocalizedValueDeserializer;
import org.commongeoregistry.adapter.serialization.LocalizedValueSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GraphTypeDTO implements Serializable
{
  private static final long  serialVersionUID            = -1947163248569170534L;

  public static final String JSON_TYPE_CODE              = "typeCode";

  public static final String JSON_CODE                   = "code";

  public static final String JSON_LOCALIZED_LABEL        = "label";

  public static final String JSON_LOCALIZED_DESCRIPTION  = "description";

  public static final String DIRECTED_ACYCLIC_GRAPH_TYPE = "DirectedAcyclicGraphType";

  public static final String UNDIRECTED_GRAPH_TYPE       = "UndirectedGraphType";

  public static final String HIERARCHY_TYPE              = "HierarchyType";

  protected String           typeCode;

  /**
   * Unique identifier but also human readable.
   */
  protected String           code;

  /**
   * The localized label of the hierarchy type for the presentation tier.
   */
  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  protected LocalizedValue   label;

  /**
   * The localized description of the hierarchy type for the presentation tier.
   */
  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  protected LocalizedValue   description;

  protected Long             seq;

  protected String           parentType;

  protected String           childType;

  public GraphTypeDTO()
  {
    this.seq = 0L;
  }

  public GraphTypeDTO(String typeCode, String code, LocalizedValue label, LocalizedValue description)
  {
    this();

    this.typeCode = typeCode;
    this.code = code;
    this.label = label;
    this.description = description;
  }

  public String getTypeCode()
  {
    return typeCode;
  }

  public void setTypeCode(String typeCode)
  {
    this.typeCode = typeCode;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public LocalizedValue getLabel()
  {
    return label;
  }

  public void setLabel(LocalizedValue label)
  {
    this.label = label;
  }

  public LocalizedValue getDescription()
  {
    return description;
  }

  public void setDescription(LocalizedValue description)
  {
    this.description = description;
  }

  public Long getSeq()
  {
    return seq;
  }

  public void setSeq(Long seq)
  {
    this.seq = seq;
  }

  public String getParentType()
  {
    return parentType;
  }

  public void setParentType(String parentType)
  {
    this.parentType = parentType;
  }

  public String getChildType()
  {
    return childType;
  }

  public void setChildType(String childType)
  {
    this.childType = childType;
  }

  public JsonObject toJSON()
  {
    return toJSON(new DefaultSerializer());
  }

  public JsonObject toJSON(CustomSerializer serializer)
  {
    JsonObject jsonObj = new JsonObject();

    jsonObj.addProperty(JSON_TYPE_CODE, this.getTypeCode());

    jsonObj.addProperty(JSON_CODE, this.getCode());

    jsonObj.add(JSON_LOCALIZED_LABEL, this.getLabel().toJSON(serializer));

    jsonObj.add(JSON_LOCALIZED_DESCRIPTION, this.getDescription().toJSON(serializer));

    return jsonObj;
  }

  /**
   * Constructs a {@link GraphTypeDTO} from the given JSON.
   * 
   * @param _sJson
   * @param _registry
   * @return
   */
  public static GraphTypeDTO fromJSON(String _sJson, RegistryAdapter _registry)
  {
    JsonObject oJson = JsonParser.parseString(_sJson).getAsJsonObject();

    String typeCode = oJson.get(JSON_TYPE_CODE).getAsString();
    String code = oJson.get(JSON_CODE).getAsString();
    LocalizedValue label = LocalizedValue.fromJSON(oJson.get(JSON_LOCALIZED_LABEL).getAsJsonObject());
    LocalizedValue description = LocalizedValue.fromJSON(oJson.get(JSON_LOCALIZED_DESCRIPTION).getAsJsonObject());

    GraphTypeDTO dto = new GraphTypeDTO(typeCode, code, label, description);

    return dto;
  }

  public static GraphTypeDTO[] fromJSONArray(String saJson, RegistryAdapter adapter)
  {
    JsonArray jaHts = JsonParser.parseString(saJson).getAsJsonArray();
    GraphTypeDTO[] hts = new GraphTypeDTO[jaHts.size()];
    for (int i = 0; i < jaHts.size(); ++i)
    {
      GraphTypeDTO ht = GraphTypeDTO.fromJSON(jaHts.get(i).toString(), adapter);
      hts[i] = ht;
    }

    return hts;
  }
}
