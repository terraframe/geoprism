package net.geoprism.data.etl.excel;

@com.runwaysdk.business.ClassSignature(hash = 2139472794)
public abstract class ExcelImportHistoryDTOBase extends com.runwaysdk.system.scheduler.JobHistoryDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "net.geoprism.data.etl.excel.ExcelImportHistory";
  private static final long serialVersionUID = 2139472794;
  
  protected ExcelImportHistoryDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected ExcelImportHistoryDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ERRORFILE = "errorFile";
  public static java.lang.String FILENAME = "fileName";
  public static java.lang.String HASERROR = "hasError";
  public static java.lang.String IMPORTCOUNT = "importCount";
  public static java.lang.String NUMBERUNKNOWNGEOS = "numberUnknownGeos";
  public static java.lang.String NUMBERUNKNOWNTERMS = "numberUnknownTerms";
  public static java.lang.String RECONSTRUCTIONJSON = "reconstructionJSON";
  public static java.lang.String SERIALIZEDUNKNOWNGEOS = "serializedUnknownGeos";
  public static java.lang.String SERIALIZEDUNKNOWNTERMS = "serializedUnknownTerms";
  public static java.lang.String TOTALRECORDS = "totalRecords";
  public com.runwaysdk.system.VaultFileDTO getErrorFile()
  {
    if(getValue(ERRORFILE) == null || getValue(ERRORFILE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.VaultFileDTO.get(getRequest(), getValue(ERRORFILE));
    }
  }
  
  public String getErrorFileId()
  {
    return getValue(ERRORFILE);
  }
  
  public void setErrorFile(com.runwaysdk.system.VaultFileDTO value)
  {
    if(value == null)
    {
      setValue(ERRORFILE, "");
    }
    else
    {
      setValue(ERRORFILE, value.getId());
    }
  }
  
  public boolean isErrorFileWritable()
  {
    return isWritable(ERRORFILE);
  }
  
  public boolean isErrorFileReadable()
  {
    return isReadable(ERRORFILE);
  }
  
  public boolean isErrorFileModified()
  {
    return isModified(ERRORFILE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getErrorFileMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(ERRORFILE).getAttributeMdDTO();
  }
  
  public String getFileName()
  {
    return getValue(FILENAME);
  }
  
  public void setFileName(String value)
  {
    if(value == null)
    {
      setValue(FILENAME, "");
    }
    else
    {
      setValue(FILENAME, value);
    }
  }
  
  public boolean isFileNameWritable()
  {
    return isWritable(FILENAME);
  }
  
  public boolean isFileNameReadable()
  {
    return isReadable(FILENAME);
  }
  
  public boolean isFileNameModified()
  {
    return isModified(FILENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getFileNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(FILENAME).getAttributeMdDTO();
  }
  
  public Boolean getHasError()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(HASERROR));
  }
  
  public void setHasError(Boolean value)
  {
    if(value == null)
    {
      setValue(HASERROR, "");
    }
    else
    {
      setValue(HASERROR, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isHasErrorWritable()
  {
    return isWritable(HASERROR);
  }
  
  public boolean isHasErrorReadable()
  {
    return isReadable(HASERROR);
  }
  
  public boolean isHasErrorModified()
  {
    return isModified(HASERROR);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getHasErrorMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(HASERROR).getAttributeMdDTO();
  }
  
  public Integer getImportCount()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(IMPORTCOUNT));
  }
  
  public void setImportCount(Integer value)
  {
    if(value == null)
    {
      setValue(IMPORTCOUNT, "");
    }
    else
    {
      setValue(IMPORTCOUNT, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isImportCountWritable()
  {
    return isWritable(IMPORTCOUNT);
  }
  
  public boolean isImportCountReadable()
  {
    return isReadable(IMPORTCOUNT);
  }
  
  public boolean isImportCountModified()
  {
    return isModified(IMPORTCOUNT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getImportCountMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(IMPORTCOUNT).getAttributeMdDTO();
  }
  
  public Integer getNumberUnknownGeos()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(NUMBERUNKNOWNGEOS));
  }
  
  public void setNumberUnknownGeos(Integer value)
  {
    if(value == null)
    {
      setValue(NUMBERUNKNOWNGEOS, "");
    }
    else
    {
      setValue(NUMBERUNKNOWNGEOS, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isNumberUnknownGeosWritable()
  {
    return isWritable(NUMBERUNKNOWNGEOS);
  }
  
  public boolean isNumberUnknownGeosReadable()
  {
    return isReadable(NUMBERUNKNOWNGEOS);
  }
  
  public boolean isNumberUnknownGeosModified()
  {
    return isModified(NUMBERUNKNOWNGEOS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getNumberUnknownGeosMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(NUMBERUNKNOWNGEOS).getAttributeMdDTO();
  }
  
  public Integer getNumberUnknownTerms()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(NUMBERUNKNOWNTERMS));
  }
  
  public void setNumberUnknownTerms(Integer value)
  {
    if(value == null)
    {
      setValue(NUMBERUNKNOWNTERMS, "");
    }
    else
    {
      setValue(NUMBERUNKNOWNTERMS, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isNumberUnknownTermsWritable()
  {
    return isWritable(NUMBERUNKNOWNTERMS);
  }
  
  public boolean isNumberUnknownTermsReadable()
  {
    return isReadable(NUMBERUNKNOWNTERMS);
  }
  
  public boolean isNumberUnknownTermsModified()
  {
    return isModified(NUMBERUNKNOWNTERMS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getNumberUnknownTermsMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(NUMBERUNKNOWNTERMS).getAttributeMdDTO();
  }
  
  public String getReconstructionJSON()
  {
    return getValue(RECONSTRUCTIONJSON);
  }
  
  public void setReconstructionJSON(String value)
  {
    if(value == null)
    {
      setValue(RECONSTRUCTIONJSON, "");
    }
    else
    {
      setValue(RECONSTRUCTIONJSON, value);
    }
  }
  
  public boolean isReconstructionJSONWritable()
  {
    return isWritable(RECONSTRUCTIONJSON);
  }
  
  public boolean isReconstructionJSONReadable()
  {
    return isReadable(RECONSTRUCTIONJSON);
  }
  
  public boolean isReconstructionJSONModified()
  {
    return isModified(RECONSTRUCTIONJSON);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getReconstructionJSONMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(RECONSTRUCTIONJSON).getAttributeMdDTO();
  }
  
  public String getSerializedUnknownGeos()
  {
    return getValue(SERIALIZEDUNKNOWNGEOS);
  }
  
  public void setSerializedUnknownGeos(String value)
  {
    if(value == null)
    {
      setValue(SERIALIZEDUNKNOWNGEOS, "");
    }
    else
    {
      setValue(SERIALIZEDUNKNOWNGEOS, value);
    }
  }
  
  public boolean isSerializedUnknownGeosWritable()
  {
    return isWritable(SERIALIZEDUNKNOWNGEOS);
  }
  
  public boolean isSerializedUnknownGeosReadable()
  {
    return isReadable(SERIALIZEDUNKNOWNGEOS);
  }
  
  public boolean isSerializedUnknownGeosModified()
  {
    return isModified(SERIALIZEDUNKNOWNGEOS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getSerializedUnknownGeosMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(SERIALIZEDUNKNOWNGEOS).getAttributeMdDTO();
  }
  
  public String getSerializedUnknownTerms()
  {
    return getValue(SERIALIZEDUNKNOWNTERMS);
  }
  
  public void setSerializedUnknownTerms(String value)
  {
    if(value == null)
    {
      setValue(SERIALIZEDUNKNOWNTERMS, "");
    }
    else
    {
      setValue(SERIALIZEDUNKNOWNTERMS, value);
    }
  }
  
  public boolean isSerializedUnknownTermsWritable()
  {
    return isWritable(SERIALIZEDUNKNOWNTERMS);
  }
  
  public boolean isSerializedUnknownTermsReadable()
  {
    return isReadable(SERIALIZEDUNKNOWNTERMS);
  }
  
  public boolean isSerializedUnknownTermsModified()
  {
    return isModified(SERIALIZEDUNKNOWNTERMS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getSerializedUnknownTermsMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(SERIALIZEDUNKNOWNTERMS).getAttributeMdDTO();
  }
  
  public Integer getTotalRecords()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(TOTALRECORDS));
  }
  
  public void setTotalRecords(Integer value)
  {
    if(value == null)
    {
      setValue(TOTALRECORDS, "");
    }
    else
    {
      setValue(TOTALRECORDS, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isTotalRecordsWritable()
  {
    return isWritable(TOTALRECORDS);
  }
  
  public boolean isTotalRecordsReadable()
  {
    return isReadable(TOTALRECORDS);
  }
  
  public boolean isTotalRecordsModified()
  {
    return isModified(TOTALRECORDS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getTotalRecordsMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(TOTALRECORDS).getAttributeMdDTO();
  }
  
  public static final void deleteAllHistory(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.excel.ExcelImportHistoryDTO.CLASS, "deleteAllHistory", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void downloadErrorSpreadsheet(java.io.OutputStream outputStream)
  {
    String[] _declaredTypes = new String[]{"java.io.OutputStream"};
    Object[] _parameters = new Object[]{outputStream};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.excel.ExcelImportHistoryDTO.CLASS, "downloadErrorSpreadsheet", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void downloadErrorSpreadsheet(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id, java.io.OutputStream outputStream)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.io.OutputStream"};
    Object[] _parameters = new Object[]{id, outputStream};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.excel.ExcelImportHistoryDTO.CLASS, "downloadErrorSpreadsheet", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final net.geoprism.data.etl.excel.ExcelImportHistoryDTO[] getAllHistory(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.excel.ExcelImportHistoryDTO.CLASS, "getAllHistory", _declaredTypes);
    return (net.geoprism.data.etl.excel.ExcelImportHistoryDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static net.geoprism.data.etl.excel.ExcelImportHistoryDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (net.geoprism.data.etl.excel.ExcelImportHistoryDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createBusiness(this);
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
  
  public static net.geoprism.data.etl.excel.ExcelImportHistoryQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (net.geoprism.data.etl.excel.ExcelImportHistoryQueryDTO) clientRequest.getAllInstances(net.geoprism.data.etl.excel.ExcelImportHistoryDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static net.geoprism.data.etl.excel.ExcelImportHistoryDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.excel.ExcelImportHistoryDTO.CLASS, "lock", _declaredTypes);
    return (net.geoprism.data.etl.excel.ExcelImportHistoryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static net.geoprism.data.etl.excel.ExcelImportHistoryDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(net.geoprism.data.etl.excel.ExcelImportHistoryDTO.CLASS, "unlock", _declaredTypes);
    return (net.geoprism.data.etl.excel.ExcelImportHistoryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
