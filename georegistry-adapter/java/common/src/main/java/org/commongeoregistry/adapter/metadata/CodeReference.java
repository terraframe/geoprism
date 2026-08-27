package org.commongeoregistry.adapter.metadata;

import com.google.gson.JsonObject;

public class CodeReference
{
  private String code;

  private String type;

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public JsonObject toJSON()
  {
    JsonObject object = new JsonObject();
    object.addProperty("code", this.code);
    object.addProperty("type", this.type);

    return object;
  }

  public CodeReference fromJSON(JsonObject object)
  {
    this.setCode(object.get("code").getAsString());
    this.setType(object.get("type").getAsString());

    return this;
  }

  public static CodeReference build(String code, String type)
  {
    CodeReference reference = new CodeReference();
    reference.setCode(code);
    reference.setType(type);

    return reference;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof CodeReference)
    {
      return this.type.equals( ( (CodeReference) obj ).type) && this.code.equals( ( (CodeReference) obj ).code);
    }

    return super.equals(obj);
  }

}
