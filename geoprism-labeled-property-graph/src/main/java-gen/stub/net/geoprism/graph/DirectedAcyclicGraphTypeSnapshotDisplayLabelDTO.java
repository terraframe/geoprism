package net.geoprism.graph;

public class DirectedAcyclicGraphTypeSnapshotDisplayLabelDTO extends DirectedAcyclicGraphTypeSnapshotDisplayLabelDTOBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1727123424;
  
  public DirectedAcyclicGraphTypeSnapshotDisplayLabelDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given LocalStructDTO into a new DTO.
  * 
  * @param localStructDTO The LocalStructDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected DirectedAcyclicGraphTypeSnapshotDisplayLabelDTO(com.runwaysdk.business.LocalStructDTO localStructDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(localStructDTO, clientRequest);
  }
  
}
