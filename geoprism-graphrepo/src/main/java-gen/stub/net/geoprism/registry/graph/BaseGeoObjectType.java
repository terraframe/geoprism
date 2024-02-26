package net.geoprism.registry.graph;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

public class BaseGeoObjectType extends BaseGeoObjectTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 57875307;

  public BaseGeoObjectType()
  {
    super();
  }

  public static BaseGeoObjectType getByCode(String code)
  {
    MdVertexDAOIF metadata = MdVertexDAO.getMdVertexDAO(BaseGeoObjectType.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + metadata.getDBClassName());
    statement.append(" WHERE " + metadata.definesAttribute(GeoObjectType.CODE).getColumnName() + " = :code");

    GraphQuery<GeoObjectType> query = new GraphQuery<GeoObjectType>(statement.toString());
    query.setParameter("code", code);

    return query.getSingleResult();
  }

}
