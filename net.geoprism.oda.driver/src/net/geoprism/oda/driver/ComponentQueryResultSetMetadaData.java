/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.oda.driver;

import java.util.ArrayList;
import java.util.List;

import net.geoprism.report.AttributeCharacterMetadataIF;
import net.geoprism.report.AttributeDecMetadataIF;
import net.geoprism.report.AttributeMetadataIF;
import net.geoprism.report.RemoteQueryIF;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class ComponentQueryResultSetMetadaData implements IResultSetMetaData
{
  private RemoteQueryIF query;

  private List<String>  attributeNames;

  public ComponentQueryResultSetMetadaData(RemoteQueryIF query)
  {
    this.query = query;
    this.attributeNames = new ArrayList<String>(this.query.getAttributeNames());
  }

  private String getAttributeName(int index)
  {
    return this.attributeNames.get( ( index - 1 ));
  }

  private AttributeMetadataIF getAttributeMetadata(int index)
  {
    AttributeMetadataIF attribute = this.query.getAttributeMetadata(this.getAttributeName(index));
    return attribute;
  }

  @Override
  public int getColumnCount() throws OdaException
  {
    return attributeNames.size();
  }

  @Override
  public int getColumnDisplayLength(int index) throws OdaException
  {
    AttributeMetadataIF attributeMetadata = this.getAttributeMetadata(index);

    if (attributeMetadata instanceof AttributeCharacterMetadataIF)
    {
      return ( (AttributeCharacterMetadataIF) attributeMetadata ).getSize();
    }

    return -1;
  }

  @Override
  public String getColumnLabel(int index) throws OdaException
  {
    AttributeMetadataIF attributeMetadata = this.getAttributeMetadata(index);

    return attributeMetadata.getDisplayLabel();
  }

  @Override
  public String getColumnName(int index) throws OdaException
  {
    return this.getAttributeMetadata(index).getName();
  }

  @Override
  public int getColumnType(int index) throws OdaException
  {
    AttributeMetadataIF attributeMetadata = this.getAttributeMetadata(index);

    return attributeMetadata.getColumnType();

    // if (attributeMetadata instanceof AttributeCharacterMetadataIF)
    // {
    // return MetaDataTypeInfo.STRING_PARAMETER;
    // }
    // else if (attributeMetadata instanceof AttributeLongMetadataIF)
    // {
    // return MetaDataTypeInfo.INTEGER_PARAMETER;
    // }
    // else if (attributeMetadata instanceof AttributeDoubleMetadataIF)
    // {
    // return MetaDataTypeInfo.DOUBLE_PARAMETER;
    // }
    // else if (attributeMetadata instanceof AttributeDecimalMetadataIF)
    // {
    // return MetaDataTypeInfo.DECIMAL_PARAMETER;
    // }
    // else if (attributeMetadata instanceof AttributeDateMetadataIF)
    // {
    // return MetaDataTypeInfo.DATE_PARAMETER;
    // }
    // else if (attributeMetadata instanceof AttributeTimeMetadataIF)
    // {
    // return MetaDataTypeInfo.TIME_PARAMETER;
    // }
    // else if (attributeMetadata instanceof AttributeDateTimeMetadataIF)
    // {
    // return MetaDataTypeInfo.TIMESTAMP_PARAMETER;
    // }
    // else if (attributeMetadata instanceof AttributeBooleanMetadataIF)
    // {
    // return MetaDataTypeInfo.BOOLEAN_PARAMETER;
    // }
    //
    // return 0;
  }

  @Override
  public String getColumnTypeName(int index) throws OdaException
  {
    AttributeMetadataIF attributeMetadata = this.getAttributeMetadata(index);

    return attributeMetadata.getClass().getSimpleName();
  }

  @Override
  public int getPrecision(int index) throws OdaException
  {
    AttributeMetadataIF attributeMetadata = this.getAttributeMetadata(index);

    if (attributeMetadata instanceof AttributeDecMetadataIF)
    {
      return ( (AttributeDecMetadataIF) attributeMetadata ).getPrecision();
    }

    return -1;
  }

  @Override
  public int getScale(int index) throws OdaException
  {
    AttributeMetadataIF attributeMetadata = this.getAttributeMetadata(index);

    if (attributeMetadata instanceof AttributeDecMetadataIF)
    {
      return ( (AttributeDecMetadataIF) attributeMetadata ).getScale();
    }

    return -1;
  }

  @Override
  public int isNullable(int index) throws OdaException
  {
    AttributeMetadataIF attributeMetadata = this.getAttributeMetadata(index);

    return ( attributeMetadata.isRequired() ? IResultSetMetaData.columnNoNulls : IResultSetMetaData.columnNullable );
  }

}
