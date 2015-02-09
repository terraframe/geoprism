package com.runwaysdk.geodashboard.gis.persist;

import java.util.ArrayList;
import java.util.List;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.util.Predicate;
import com.runwaysdk.system.metadata.MdAttributeConcreteDTO;
import com.runwaysdk.system.metadata.MdAttributeDTO;
import com.runwaysdk.system.metadata.MdAttributeMomentDTO;
import com.runwaysdk.system.metadata.MdAttributeVirtualDTO;

public class AggregationPredicate implements Predicate<AggregationTypeDTO>, Reloadable
{
  private List<AllAggregationTypeDTO> filters;

  public AggregationPredicate(MdAttributeDTO _mdAttribute)
  {
    this.filters = new ArrayList<AllAggregationTypeDTO>();

    MdAttributeConcreteDTO mdAttributeConcrete = getMdAttributeConcrete(_mdAttribute);

    if (mdAttributeConcrete instanceof MdAttributeMomentDTO)
    {
      this.filters.add(AllAggregationTypeDTO.AVG);
      this.filters.add(AllAggregationTypeDTO.SUM);
    }
  }

  private MdAttributeConcreteDTO getMdAttributeConcrete(MdAttributeDTO _mdAttribute)
  {
    if (_mdAttribute instanceof MdAttributeVirtualDTO)
    {
      return ( (MdAttributeVirtualDTO) _mdAttribute ).getMdAttributeConcrete();
    }

    return (MdAttributeConcreteDTO) _mdAttribute;
  }

  @Override
  public boolean evaulate(AggregationTypeDTO _dto)
  {
    for (AllAggregationTypeDTO filter : filters)
    {
      if (_dto.getEnumName().equals(filter.getName()))
      {
        return true;
      }
    }

    return false;
  }
}
