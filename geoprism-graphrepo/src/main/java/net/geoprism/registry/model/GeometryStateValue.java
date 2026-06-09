package net.geoprism.registry.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.locationtech.jts.geom.Geometry;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;

import net.geoprism.registry.model.graph.VertexServerGeoObject;

public class GeometryStateValue extends PrimitiveStateValue
{
  private ServerGeoObjectType type;

  private Geometry            geometry;

  public GeometryStateValue(ServerGeoObjectType type, VertexObject node, String nodeAttribute)
  {
    super(node, nodeAttribute);

    this.type = type;
  }

  @Override
  public void setValue(Object value)
  {
    if (value instanceof Geometry)
    {
      this.geometry = (Geometry) value;
    }
    else
    {
      super.setValue(value);
    }
  }

  public Geometry getGeometryValue()
  {
    if (geometry == null)
    {
      this.geometry = getGeometryValue((String) this.getValue());
    }

    return geometry;
  }

  public BusinessDAOIF getGeometryInstance()
  {
    return getGeometryInstance((String) this.getValue());
  }

  @Override
  public void apply(VertexServerGeoObject object)
  {
    if (this.hasGeometryObject() && this.geometry == null)
    {
      return;
    }

    MdBusinessDAOIF geometryTable = this.type.getGeometryTable();
    LocalizedValue localizedValue = object.getDisplayLabel(getStartDate());
    String label = localizedValue != null ? localizedValue.getValue() : object.getCode();

    // Create or update the entry in the geometry table
    BusinessDAO geometryInstance = !this.hasGeometryObject() ? //
        BusinessDAO.newInstance(geometryTable.definesType()) : //
        getGeometryInstance().getBusinessDAO();

    geometryInstance.setValue(DefaultAttribute.CODE.getName(), object.getCode());
    // geometryInstance.setValue(DefaultAttribute.DISPLAY_LABEL.getName(),
    // object.getCode());
    geometryInstance.setValue(DefaultAttribute.DISPLAY_LABEL.getName(), label);
    geometryInstance.setValue(EdgeType.START_DATE, this.getStartDate());
    geometryInstance.setValue(EdgeType.END_DATE, this.getEndDate());
    geometryInstance.setValue(DefaultAttribute.GEOMETRY.getName(), this.geometry);
    geometryInstance.apply();

    // Set the value of this node
    this.setValue(geometryInstance.getOid());

    super.apply(object);
  }

  @Override
  public void delete()
  {
    // Ensure the geometry row is deleted
    if (hasGeometryObject())
    {
      getGeometryInstance().getBusinessDAO().delete();
    }

    super.delete();
  }

  public boolean hasGeometryObject()
  {
    return !StringUtils.isBlank((String) this.getValue());
  }

  public ValueOverTime toValueOverTime()
  {
    Date startDate = this.getStartDate();
    Date endDate = this.getEndDate();

    return new ValueOverTime(this.getOid(), startDate, endDate, this.getGeometryValue());
  }

  public static BusinessDAOIF getGeometryInstance(String id)
  {
    return BusinessDAO.get(id);
  }

  public static Geometry getGeometryValue(String oid)
  {
    if (!StringUtils.isBlank(oid))
    {
      return (Geometry) getGeometryInstance(oid).getObjectValue(DefaultAttribute.GEOMETRY.getName());
    }

    return null;
  }

}
