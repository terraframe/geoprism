/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.graph;

@com.runwaysdk.business.ClassSignature(hash = 1794115256)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GeoVertex.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class GeoVertexBase extends com.runwaysdk.business.graph.VertexObject
{
  public final static String CLASS = "net.geoprism.registry.graph.GeoVertex";
  public static final java.lang.String CREATEDATE = "createDate";
  public static final java.lang.String GEOLINE = "geoLine";
  public static final java.lang.String GEOMULTILINE = "geoMultiLine";
  public static final java.lang.String GEOMULTIPOINT = "geoMultiPoint";
  public static final java.lang.String GEOMULTIPOLYGON = "geoMultiPolygon";
  public static final java.lang.String GEOPOINT = "geoPoint";
  public static final java.lang.String GEOPOLYGON = "geoPolygon";
  public static final java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static final java.lang.String OID = "oid";
  public static final java.lang.String SEQ = "seq";
  public static final java.lang.String SHAPE = "shape";
  private static final long serialVersionUID = 1794115256;
  
  public GeoVertexBase()
  {
    super();
  }
  
  public java.util.Date getCreateDate()
  {
    return (java.util.Date) this.getObjectValue(CREATEDATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF getCreateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertex.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF)mdClassIF.definesAttribute(CREATEDATE);
  }
  
  public void setCreateDate(java.util.Date value)
  {
    this.setValue(CREATEDATE, value);
  }
  
  public org.locationtech.jts.geom.LineString getGeoLine()
  {
    return (org.locationtech.jts.geom.LineString) this.getObjectValue(GEOLINE);
  }
  
  public static com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF getGeoLineMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertex.CLASS);
    return (com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF)mdClassIF.definesAttribute(GEOLINE);
  }
  
  public void setGeoLine(org.locationtech.jts.geom.LineString value)
  {
    this.setValue(GEOLINE, value);
  }
  
  public org.locationtech.jts.geom.MultiLineString getGeoMultiLine()
  {
    return (org.locationtech.jts.geom.MultiLineString) this.getObjectValue(GEOMULTILINE);
  }
  
  public static com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF getGeoMultiLineMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertex.CLASS);
    return (com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF)mdClassIF.definesAttribute(GEOMULTILINE);
  }
  
  public void setGeoMultiLine(org.locationtech.jts.geom.MultiLineString value)
  {
    this.setValue(GEOMULTILINE, value);
  }
  
  public org.locationtech.jts.geom.MultiPoint getGeoMultiPoint()
  {
    return (org.locationtech.jts.geom.MultiPoint) this.getObjectValue(GEOMULTIPOINT);
  }
  
  public static com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF getGeoMultiPointMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertex.CLASS);
    return (com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF)mdClassIF.definesAttribute(GEOMULTIPOINT);
  }
  
  public void setGeoMultiPoint(org.locationtech.jts.geom.MultiPoint value)
  {
    this.setValue(GEOMULTIPOINT, value);
  }
  
  public org.locationtech.jts.geom.MultiPolygon getGeoMultiPolygon()
  {
    return (org.locationtech.jts.geom.MultiPolygon) this.getObjectValue(GEOMULTIPOLYGON);
  }
  
  public static com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF getGeoMultiPolygonMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertex.CLASS);
    return (com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF)mdClassIF.definesAttribute(GEOMULTIPOLYGON);
  }
  
  public void setGeoMultiPolygon(org.locationtech.jts.geom.MultiPolygon value)
  {
    this.setValue(GEOMULTIPOLYGON, value);
  }
  
  public org.locationtech.jts.geom.Point getGeoPoint()
  {
    return (org.locationtech.jts.geom.Point) this.getObjectValue(GEOPOINT);
  }
  
  public static com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF getGeoPointMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertex.CLASS);
    return (com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF)mdClassIF.definesAttribute(GEOPOINT);
  }
  
  public void setGeoPoint(org.locationtech.jts.geom.Point value)
  {
    this.setValue(GEOPOINT, value);
  }
  
  public org.locationtech.jts.geom.Polygon getGeoPolygon()
  {
    return (org.locationtech.jts.geom.Polygon) this.getObjectValue(GEOPOLYGON);
  }
  
  public static com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF getGeoPolygonMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertex.CLASS);
    return (com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF)mdClassIF.definesAttribute(GEOPOLYGON);
  }
  
  public void setGeoPolygon(org.locationtech.jts.geom.Polygon value)
  {
    this.setValue(GEOPOLYGON, value);
  }
  
  public java.util.Date getLastUpdateDate()
  {
    return (java.util.Date) this.getObjectValue(LASTUPDATEDATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF getLastUpdateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertex.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF)mdClassIF.definesAttribute(LASTUPDATEDATE);
  }
  
  public void setLastUpdateDate(java.util.Date value)
  {
    this.setValue(LASTUPDATEDATE, value);
  }
  
  public String getOid()
  {
    return (String) this.getObjectValue(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertex.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public Long getSeq()
  {
    return (Long) this.getObjectValue(SEQ);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLongDAOIF getSeqMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertex.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLongDAOIF)mdClassIF.definesAttribute(SEQ);
  }
  
  public void setSeq(Long value)
  {
    this.setValue(SEQ, value);
  }
  
  public org.locationtech.jts.geom.Geometry getShape()
  {
    return (org.locationtech.jts.geom.Geometry) this.getObjectValue(SHAPE);
  }
  
  public static com.runwaysdk.gis.dataaccess.MdAttributeShapeDAOIF getShapeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertex.CLASS);
    return (com.runwaysdk.gis.dataaccess.MdAttributeShapeDAOIF)mdClassIF.definesAttribute(SHAPE);
  }
  
  public void setShape(org.locationtech.jts.geom.Geometry value)
  {
    this.setValue(SHAPE, value);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.business.graph.EdgeObject addGeoVertexHasSynonymChild(net.geoprism.registry.graph.GeoVertexSynonym geoVertexSynonym)
  {
    return super.addChild(geoVertexSynonym, "net.geoprism.registry.graph.GeoVertexHasSynonym");
  }
  
  public void removeGeoVertexHasSynonymChild(net.geoprism.registry.graph.GeoVertexSynonym geoVertexSynonym)
  {
    super.removeChild(geoVertexSynonym, "net.geoprism.registry.graph.GeoVertexHasSynonym");
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<net.geoprism.registry.graph.GeoVertexSynonym> getGeoVertexHasSynonymChildGeoVertexSynonyms()
  {
    return super.getChildren("net.geoprism.registry.graph.GeoVertexHasSynonym",net.geoprism.registry.graph.GeoVertexSynonym.class);
  }
  
  public com.runwaysdk.business.graph.EdgeObject addLocatedInChild(net.geoprism.registry.graph.GeoVertex geoVertex)
  {
    return super.addChild(geoVertex, "net.geoprism.registry.graph.LocatedIn");
  }
  
  public void removeLocatedInChild(net.geoprism.registry.graph.GeoVertex geoVertex)
  {
    super.removeChild(geoVertex, "net.geoprism.registry.graph.LocatedIn");
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<net.geoprism.registry.graph.GeoVertex> getLocatedInChildGeoVertexs()
  {
    return super.getChildren("net.geoprism.registry.graph.LocatedIn",net.geoprism.registry.graph.GeoVertex.class);
  }
  
  public com.runwaysdk.business.graph.EdgeObject addExternalIDParent(net.geoprism.registry.graph.ExternalSystem externalSystem)
  {
    return super.addParent(externalSystem, "net.geoprism.registry.graph.ExternalID");
  }
  
  public void removeExternalIDParent(net.geoprism.registry.graph.ExternalSystem externalSystem)
  {
    super.removeParent(externalSystem, "net.geoprism.registry.graph.ExternalID");
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<net.geoprism.registry.graph.ExternalSystem> getExternalIDParentExternalSystems()
  {
    return super.getParents("net.geoprism.registry.graph.ExternalID", net.geoprism.registry.graph.ExternalSystem.class);
  }
  
  public com.runwaysdk.business.graph.EdgeObject addLocatedInParent(net.geoprism.registry.graph.GeoVertex geoVertex)
  {
    return super.addParent(geoVertex, "net.geoprism.registry.graph.LocatedIn");
  }
  
  public void removeLocatedInParent(net.geoprism.registry.graph.GeoVertex geoVertex)
  {
    super.removeParent(geoVertex, "net.geoprism.registry.graph.LocatedIn");
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<net.geoprism.registry.graph.GeoVertex> getLocatedInParentGeoVertexs()
  {
    return super.getParents("net.geoprism.registry.graph.LocatedIn", net.geoprism.registry.graph.GeoVertex.class);
  }
  
  public com.runwaysdk.business.graph.EdgeObject addSearchLinkDefaultParent(com.runwaysdk.business.graph.VertexObject searchDefault)
  {
    return super.addParent(searchDefault, "net.geoprism.registry.search.SearchLinkDefault");
  }
  
  public void removeSearchLinkDefaultParent(com.runwaysdk.business.graph.VertexObject searchDefault)
  {
    super.removeParent(searchDefault, "net.geoprism.registry.search.SearchLinkDefault");
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.business.graph.VertexObject> getSearchLinkDefaultParentSearchDefaults()
  {
    return super.getParents("net.geoprism.registry.search.SearchLinkDefault", com.runwaysdk.business.graph.VertexObject.class);
  }
  
  public static GeoVertex get(String oid)
  {
    return (GeoVertex) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}
