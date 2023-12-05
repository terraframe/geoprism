package net.geoprism.registry.model;

import org.commongeoregistry.adapter.metadata.OrganizationDTO;

import com.google.gson.JsonElement;

import net.geoprism.registry.view.JsonSerializable;

public class OrganizationView implements JsonSerializable
{
  private OrganizationDTO dto;

  public OrganizationView(OrganizationDTO dto)
  {
    this.dto = dto;
  }

  @Override
  public JsonElement toJSON()
  {
    return this.dto.toJSON();
  }

}
