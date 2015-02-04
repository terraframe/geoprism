package com.runwaysdk.geodashboard.gis.persist;

import java.util.List;

public class DashboardThematicStyleDTO extends DashboardThematicStyleDTOBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1553979534;

  public DashboardThematicStyleDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }

  /**
   * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
   * 
   * @param businessDTO
   *          The BusinessDTO to duplicate
   * @param clientRequest
   *          The clientRequest this DTO should use to communicate with the server.
   */
  protected DashboardThematicStyleDTO(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }

  public AggregationTypeDTO getActiveAggregation(List<AggregationTypeDTO> _aggregations)
  {
    List<AllAggregationTypeDTO> activeAggregations = this.getAggregationType();

    if (activeAggregations.size() > 0)
    {
      AllAggregationTypeDTO activeAggregation = activeAggregations.get(0);
      AggregationTypeDTO item = activeAggregation.item(this.getRequest());

      return item;
    }
    else if (_aggregations.size() > 0)
    {
      return _aggregations.get(0);
    }

    return null;
  }

  public String getActiveAggregationLabel(List<AggregationTypeDTO> _aggregations)
  {
    AggregationTypeDTO item = this.getActiveAggregation(_aggregations);

    if (item != null)
    {
      return item.getDisplayLabel().getValue();
    }

    return "";
  }

}
