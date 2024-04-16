package net.geoprism.graph;

public class UndirectedGraphTypeSnapshotDisplayLabelDTO extends UndirectedGraphTypeSnapshotDisplayLabelDTOBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1702739877;
  
  public UndirectedGraphTypeSnapshotDisplayLabelDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given LocalStructDTO into a new DTO.
  * 
  * @param localStructDTO The LocalStructDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected UndirectedGraphTypeSnapshotDisplayLabelDTO(com.runwaysdk.business.LocalStructDTO localStructDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(localStructDTO, clientRequest);
  }
  
}
