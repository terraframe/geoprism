package com.runwaysdk.geodashboard.oda.driver;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.runwaysdk.geodashboard.report.ReportQueryViewDTO;
import com.runwaysdk.transport.metadata.AttributeBooleanMdDTO;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;
import com.runwaysdk.transport.metadata.AttributeDateMdDTO;
import com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO;
import com.runwaysdk.transport.metadata.AttributeDecMdDTO;
import com.runwaysdk.transport.metadata.AttributeDecimalMdDTO;
import com.runwaysdk.transport.metadata.AttributeDoubleMdDTO;
import com.runwaysdk.transport.metadata.AttributeFloatMdDTO;
import com.runwaysdk.transport.metadata.AttributeIntegerMdDTO;
import com.runwaysdk.transport.metadata.AttributeLongMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeTextMdDTO;
import com.runwaysdk.transport.metadata.AttributeTimeMdDTO;

public class ViewResultSetMetadaData implements IResultSetMetaData
{

  private ReportQueryViewDTO view;

  private List<String>       attributeNames;

  public ViewResultSetMetadaData(ReportQueryViewDTO view)
  {
    this.view = view;
    this.attributeNames = new ArrayList<String>(view.getAttributeNames());
  }

  private String getAttributeName(int index)
  {
    return this.attributeNames.get( ( index - 1 ));
  }

  private AttributeMdDTO getAttributeMdDTO(int index)
  {
    return this.view.getAttributeMd(this.getAttributeName(index));
  }

  @Override
  public int getColumnCount() throws OdaException
  {
    return attributeNames.size();
  }

  @Override
  public int getColumnDisplayLength(int index) throws OdaException
  {
    AttributeMdDTO attributeMdDTO = this.getAttributeMdDTO(index);

    if (attributeMdDTO instanceof AttributeCharacterMdDTO)
    {
      return ( (AttributeCharacterMdDTO) attributeMdDTO ).getSize();
    }

    return -1;
  }

  @Override
  public String getColumnLabel(int index) throws OdaException
  {
    AttributeMdDTO attributeMdDTO = this.getAttributeMdDTO(index);

    return attributeMdDTO.getDisplayLabel();
  }

  @Override
  public String getColumnName(int index) throws OdaException
  {
    return this.getAttributeMdDTO(index).getName();
  }

  @Override
  public int getColumnType(int index) throws OdaException
  {
    AttributeMdDTO attributeMdDTO = this.getAttributeMdDTO(index);

    if (attributeMdDTO instanceof AttributeCharacterMdDTO || attributeMdDTO instanceof AttributeTextMdDTO)
    {
      return MetaDataTypeInfo.STRING_PARAMETER;
    }
    else if (attributeMdDTO instanceof AttributeIntegerMdDTO || attributeMdDTO instanceof AttributeLongMdDTO)
    {
      return MetaDataTypeInfo.INTEGER_PARAMETER;
    }
    else if (attributeMdDTO instanceof AttributeFloatMdDTO || attributeMdDTO instanceof AttributeDoubleMdDTO)
    {
      return MetaDataTypeInfo.DOUBLE_PARAMETER;
    }
    else if (attributeMdDTO instanceof AttributeDecimalMdDTO)
    {
      return MetaDataTypeInfo.DECIMAL_PARAMETER;
    }
    else if (attributeMdDTO instanceof AttributeDateMdDTO)
    {
      return MetaDataTypeInfo.DATE_PARAMETER;
    }
    else if (attributeMdDTO instanceof AttributeTimeMdDTO)
    {
      return MetaDataTypeInfo.TIME_PARAMETER;
    }
    else if (attributeMdDTO instanceof AttributeDateTimeMdDTO)
    {
      return MetaDataTypeInfo.TIMESTAMP_PARAMETER;
    }
    else if (attributeMdDTO instanceof AttributeBooleanMdDTO)
    {
      return MetaDataTypeInfo.BOOLEAN_PARAMETER;
    }

    return 0;
  }

  @Override
  public String getColumnTypeName(int index) throws OdaException
  {
    AttributeMdDTO attributeMdDTO = this.getAttributeMdDTO(index);

    return attributeMdDTO.getClass().getSimpleName();
  }

  @Override
  public int getPrecision(int index) throws OdaException
  {
    AttributeMdDTO attributeMdDTO = this.getAttributeMdDTO(index);

    if (attributeMdDTO instanceof AttributeDecMdDTO)
    {
      return ( (AttributeDecMdDTO) attributeMdDTO ).getDecimalLength();
    }

    return -1;
  }

  @Override
  public int getScale(int index) throws OdaException
  {
    AttributeMdDTO attributeMdDTO = this.getAttributeMdDTO(index);

    if (attributeMdDTO instanceof AttributeDecMdDTO)
    {
      return ( (AttributeDecMdDTO) attributeMdDTO ).getTotalLength();
    }

    return -1;
  }

  @Override
  public int isNullable(int index) throws OdaException
  {
    AttributeMdDTO attributeMdDTO = this.getAttributeMdDTO(index);

    return ( attributeMdDTO.isRequired() ? IResultSetMetaData.columnNoNulls : IResultSetMetaData.columnNullable );
  }

}
