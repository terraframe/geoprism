package net.geoprism.graph;

public class LabeledPropertyGraphSynchronizationDisplayLabelDTO extends LabeledPropertyGraphSynchronizationDisplayLabelDTOBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1487923798;
  
  public LabeledPropertyGraphSynchronizationDisplayLabelDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given LocalStructDTO into a new DTO.
  * 
  * @param localStructDTO The LocalStructDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected LabeledPropertyGraphSynchronizationDisplayLabelDTO(com.runwaysdk.business.LocalStructDTO localStructDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(localStructDTO, clientRequest);
  }
  
}
