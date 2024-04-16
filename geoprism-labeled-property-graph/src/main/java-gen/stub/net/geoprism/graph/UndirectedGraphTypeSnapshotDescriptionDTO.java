package net.geoprism.graph;

public class UndirectedGraphTypeSnapshotDescriptionDTO extends UndirectedGraphTypeSnapshotDescriptionDTOBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 680278607;
  
  public UndirectedGraphTypeSnapshotDescriptionDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given LocalStructDTO into a new DTO.
  * 
  * @param localStructDTO The LocalStructDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected UndirectedGraphTypeSnapshotDescriptionDTO(com.runwaysdk.business.LocalStructDTO localStructDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(localStructDTO, clientRequest);
  }
  
}
