package net.geoprism.registry.graph;

import java.util.Optional;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.registry.conversion.LocalizedValueConverter;

public class SourceAuthority extends SourceAuthorityBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 241315036;

  public SourceAuthority()
  {
    super();
  }

  public LocalizedValue getLabel()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(SourceAuthority.DISPLAYLABEL));
  }

  public LocalizedValue getDescriptionLV()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(SourceAuthority.DESCRIPTION));
  }

  public static Optional<SourceAuthority> getByCode(String code)
  {
    MdVertexDAOIF metadata = MdVertexDAO.getMdVertexDAO(SourceAuthority.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + metadata.getDBClassName());
    statement.append(" WHERE " + metadata.definesAttribute(SourceAuthority.CODE).getColumnName() + " = :code");

    GraphQuery<SourceAuthority> query = new GraphQuery<SourceAuthority>(statement.toString());
    query.setParameter("code", code);

    return Optional.ofNullable(query.getSingleResult());
  }

  public static Optional<SourceAuthority> getByRid(String rid)
  {
    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + rid);

    GraphQuery<SourceAuthority> query = new GraphQuery<SourceAuthority>(statement.toString());

    return Optional.ofNullable(query.getSingleResult());
  }

}
