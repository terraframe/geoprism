package org.ideorg.iq;

@com.runwaysdk.business.ClassSignature(hash = -2083039601)
public abstract class SalesTransactionViewDTOBase extends com.runwaysdk.business.ViewDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "org.ideorg.iq.SalesTransactionView";
  private static final long serialVersionUID = -2083039601;
  
  protected SalesTransactionViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DELIVERYDATE = "deliveryDate";
  public static java.lang.String ID = "id";
  public static java.lang.String NUMBEROFUNITS = "numberOfUnits";
  public java.util.Date getDeliveryDate()
  {
    return com.runwaysdk.constants.MdAttributeDateUtil.getTypeSafeValue(getValue(DELIVERYDATE));
  }
  
  public void setDeliveryDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(DELIVERYDATE, "");
    }
    else
    {
      setValue(DELIVERYDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATE_FORMAT).format(value));
    }
  }
  
  public boolean isDeliveryDateWritable()
  {
    return isWritable(DELIVERYDATE);
  }
  
  public boolean isDeliveryDateReadable()
  {
    return isReadable(DELIVERYDATE);
  }
  
  public boolean isDeliveryDateModified()
  {
    return isModified(DELIVERYDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateMdDTO getDeliveryDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateMdDTO) getAttributeDTO(DELIVERYDATE).getAttributeMdDTO();
  }
  
  public Double getNumberOfUnits()
  {
    return com.runwaysdk.constants.MdAttributeDoubleUtil.getTypeSafeValue(getValue(NUMBEROFUNITS));
  }
  
  public void setNumberOfUnits(Double value)
  {
    if(value == null)
    {
      setValue(NUMBEROFUNITS, "");
    }
    else
    {
      setValue(NUMBEROFUNITS, java.lang.Double.toString(value));
    }
  }
  
  public boolean isNumberOfUnitsWritable()
  {
    return isWritable(NUMBEROFUNITS);
  }
  
  public boolean isNumberOfUnitsReadable()
  {
    return isReadable(NUMBEROFUNITS);
  }
  
  public boolean isNumberOfUnitsModified()
  {
    return isModified(NUMBEROFUNITS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDecMdDTO getNumberOfUnitsMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDecMdDTO) getAttributeDTO(NUMBEROFUNITS).getAttributeMdDTO();
  }
  
  public static SalesTransactionViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.ViewDTO dto = (com.runwaysdk.business.ViewDTO)clientRequest.get(id);
    
    return (SalesTransactionViewDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createSessionComponent(this);
    }
    else
    {
      getRequest().update(this);
    }
  }
  public void delete()
  {
    getRequest().delete(this.getId());
  }
  
}
