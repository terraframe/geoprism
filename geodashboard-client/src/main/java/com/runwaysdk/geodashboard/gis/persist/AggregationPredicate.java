package com.runwaysdk.geodashboard.gis.persist;

import java.util.ArrayList;
import java.util.List;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.util.Predicate;
import com.runwaysdk.system.metadata.MdAttributeConcreteDTO;
import com.runwaysdk.system.metadata.MdAttributeMomentDTO;

public class AggregationPredicate implements Predicate<AggregationTypeDTO>, Reloadable
{
  private List<AllAggregationTypeDTO> filters;

  public AggregationPredicate(MdAttributeConcreteDTO _mdAttribute)
  {
    this.filters = new ArrayList<AllAggregationTypeDTO>();

    if (_mdAttribute instanceof MdAttributeMomentDTO)
    {
      this.filters.add(AllAggregationTypeDTO.AVG);
      this.filters.add(AllAggregationTypeDTO.SUM);
    }
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
