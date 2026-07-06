package net.geoprism.registry.view;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, // use logical type name
    include = JsonTypeInfo.As.PROPERTY, property = "type")

@JsonSubTypes({ //
    @JsonSubTypes.Type(value = SingleValueDTO.class, name = SingleValueDTO.TYPE), //
    @JsonSubTypes.Type(value = MultiValueDTO.class, name = MultiValueDTO.TYPE), //
})
public abstract class AttributeValueDTO
{
}
