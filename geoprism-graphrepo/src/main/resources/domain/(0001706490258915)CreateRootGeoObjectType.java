/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
<version xsi:noNamespaceSchemaLocation="classpath:com/runwaysdk/resources/xsd/version.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <doIt>
    <create>
      <mdVertex name="net.geoprism.registry.graph.BaseGeoObjectType" label="Base Geo Object Type">
        <attributes>
          <char name="code" label="Code" required="true" size="255" indexType="unique"  />          
        </attributes>
      </mdVertex>    
      
      <mdVertex name="net.geoprism.registry.graph.GeoObjectType" extends="net.geoprism.registry.graph.BaseGeoObjectType" label="GeoObjectType">
        <attributes>
          <char name="geometryType" label="Geometry Type" required="true" size="255"  />          
          <localCharEmbedded name="label" label="Label" required="true" />        
          <localCharEmbedded name="description" label="Description" />        
          <boolean name="isGeometryEditable" label="Is Geometry Editable" />
          <boolean name="isAbstract" label="Is Abstract" />
          <boolean name="isPrivate" label="Is Private" />         
          <graphReference name="superType" label="Super Type" type="net.geoprism.registry.graph.GeoObjectType" />
          <graphReference name="organization" label="Organization" type="net.geoprism.registry.graph.GraphOrganization" />
          <reference name="mdVertex" label="MdVertex" type="com.runwaysdk.system.metadata.MdVertex" />
          <reference name="rootTerm" label="Root Term" required="false" type="net.geoprism.ontology.Classifier"  />                
        </attributes>
      </mdVertex>    
      
      <mdVertex name="net.geoprism.registry.graph.HierarchicalRelationshipType" label="Hierarchical Relationship" generateController="false">
        <attributes>
          <char name="code" size="255" required="true" indexType="unique index" label="Code" />
          <localCharEmbedded name="label" label="Label" required="true" />        
          <localCharEmbedded name="description" label="Description" />        
          <graphReference name="organization" label="Organization" type="net.geoprism.registry.graph.GraphOrganization" />
          <reference name="definitionEdge" type="com.runwaysdk.system.metadata.MdEdge" required="true" label="DefinitionEdge" />          
          <reference name="objectEdge" type="com.runwaysdk.system.metadata.MdEdge" required="true" label="ObjectEdge" />          
          <text name="abstractDescription" required="false" label="Abstract" />
          <text name="progress" required="false" label="Progress" />
          <text name="acknowledgement" required="false" label="Acknowledgement and disclaimer" />
          <text name="contact" required="false" label="Contact information" />
          <text name="accessConstraints" label="Access Constraints" />
          <text name="useConstraints" label="Use Constraints" />         
          <text name="disclaimer" label="Disclaimer" />
          <text name="phoneNumber" label="Telephone number" />
          <text name="email" label="Email address" />             
        </attributes>
      </mdVertex>    
      
      <mdVertex name="net.geoprism.registry.graph.AttributeType" isAbstract="true" label="AttributeType">
        <attributes>
          <char name="code" label="Code" required="true" size="255" />
          <localCharEmbedded name="label" label="Label" required="true" />        
          <localCharEmbedded name="description" label="Description" />        
          <boolean name="required" label="Required" />
          <boolean name="unique" label="Unique" />
          <boolean name="isDefault" label="Is Default" />
          <boolean name="isChangeOverTime" label="Is Change Over Time" />
          <graphReference name="geoObjectType" label="Geo Object Type" required="true" type="net.geoprism.registry.graph.GeoObjectType"/>
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeBooleanType" extends="net.geoprism.registry.graph.AttributeType" label="Attribute Boolean Type">
        <attributes>
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeUUIDType" extends="net.geoprism.registry.graph.AttributeType" label="Attribute Character Type">
        <attributes>
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeCharacterType" extends="net.geoprism.registry.graph.AttributeType" label="Attribute Character Type">
        <attributes>
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeDateType" extends="net.geoprism.registry.graph.AttributeType" label="Attribute Date Type">
        <attributes>
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeLongType" extends="net.geoprism.registry.graph.AttributeType" label="Attribute Long Type">
        <attributes>
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeDoubleType" extends="net.geoprism.registry.graph.AttributeType" label="Attribute Double Type">
        <attributes>
          <integer name="precision" label="Precision" required="true"  />          
          <integer name="scale" label="Scale" required="true"  />          
          <reference name="valueVertex" label="Value Vertex" required="true" type="com.runwaysdk.system.metadata.MdVertex"/>          
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeLocalType" extends="net.geoprism.registry.graph.AttributeType" label="Attribute Local Type">
        <attributes>
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeTermType" extends="net.geoprism.registry.graph.AttributeType" label="Attribute Term Type">
        <attributes>
          <reference name="rootTerm" label="Root Term" required="false" type="net.geoprism.ontology.Classifier"  />     
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeClassificationType" extends="net.geoprism.registry.graph.AttributeType" label="Attribute Classification Type">
        <attributes>
          <reference name="mdClassification" type="com.runwaysdk.system.metadata.MdClassification" label="MdClassification" required="true"/>          
          <text name="rootTerm" label="Root Term" required="false"/>  
          <reference name="valueVertex" label="Value Vertex" required="true" type="com.runwaysdk.system.metadata.MdVertex"/>                            
        </attributes>
      </mdVertex>    
      
      
      <mdVertex name="net.geoprism.registry.graph.AttributeGeometryType" extends="net.geoprism.registry.graph.AttributeType" label="Attribute Geometry Type">
        <attributes>
          <char name="geometryType" label="Geometry Type" required="true" size="255"  />          
        </attributes>
      </mdVertex>    


      <mdVertex name="net.geoprism.registry.graph.AttributeValue" isAbstract="true" label="AttributeValue">
        <attributes>
          <char name="attributeName" label="Attribute Name" required="true" size="255" />
          <dateTime name="startDate" label="Start Date" required="true"  />
          <dateTime name="endDate" label="End Date" required="true"  />          
        </attributes>
      </mdVertex>    
      
      <mdVertex name="net.geoprism.registry.graph.AttributeBasicValue" isAbstract="true" extends="net.geoprism.registry.graph.AttributeValue" label="Attribute Basic Value">
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeBooleanValue" extends="net.geoprism.registry.graph.AttributeBasicValue" label="Attribute Boolean Value">
        <attributes>
          <boolean name="value" label="Value" required="true"  />          
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeCharacterValue" extends="net.geoprism.registry.graph.AttributeBasicValue" label="Attribute Character Value">
        <attributes>
          <text name="value" label="Value" required="true"  />          
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeDateValue" extends="net.geoprism.registry.graph.AttributeBasicValue" label="Attribute Date Value">
        <attributes>
          <dateTime name="value" label="Value" required="true"  />          
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeLongValue" extends="net.geoprism.registry.graph.AttributeBasicValue" label="Attribute Long Value">
        <attributes>
          <long name="value" label="Value" required="true"  />          
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeLocalValue" extends="net.geoprism.registry.graph.AttributeBasicValue" label="Attribute Local Value">
        <attributes>
          <text name="defaultLocale" label="Default Locale" required="true"  />          
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeTermValue" extends="net.geoprism.registry.graph.AttributeBasicValue" label="Attribute Term Value">
        <attributes>
          <reference name="value" label="Value" required="true" type="net.geoprism.ontology.Classifier"  />          
        </attributes>
      </mdVertex>    
<!--       
      <mdVertex name="net.geoprism.registry.graph.AttributeClassificationValue" isAbstract="true" label="AttributeValue">
        <attributes>
          <graphReference name="value" type="net.geoprism.registry.graph.ClassificationNode" label="Value" required="true"/>          
        </attributes>
      </mdVertex>    
 -->      
      
      
      <mdVertex name="net.geoprism.registry.graph.AttributeGeometryValue"  extends="net.geoprism.registry.graph.AttributeValue" isAbstract="true" label="Attribute Geometry Value">
        <attributes>
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributePointValue" extends="net.geoprism.registry.graph.AttributeGeometryValue" label="Attribute Point Value">
        <attributes>
          <multipoint name="value" label="Value" required="true" dimension="2" srid="4326"  />          
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributePolygonValue" extends="net.geoprism.registry.graph.AttributeGeometryValue" label="Attribute Polygon Value">
        <attributes>
          <multipolygon name="value" label="Value" required="true" dimension="2" srid="4326" />          
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeLineValue" extends="net.geoprism.registry.graph.AttributeGeometryValue" label="Attribute Line Value">
        <attributes>
          <multilinestring name="value" label="Value" required="true" dimension="2" srid="4326"  />          
        </attributes>
      </mdVertex>    
      <mdVertex name="net.geoprism.registry.graph.AttributeShapeValue" extends="net.geoprism.registry.graph.AttributeGeometryValue" label="Attribute Shape Value">
        <attributes>
          <shape name="value" label="Value" required="true" dimension="2" srid="4326"  />          
        </attributes>
      </mdVertex>    
      
      <mdEdge name="net.geoprism.registry.graph.HasValue" label="Has Value" parent="net.geoprism.registry.graph.GeoVertex" child="net.geoprism.registry.graph.AttributeBasicValue" generateSource="false">
      </mdEdge>

      <mdEdge name="net.geoprism.registry.graph.HasGeometry" label="Has Geometry" parent="net.geoprism.registry.graph.GeoVertex" child="net.geoprism.registry.graph.AttributeGeometryValue" generateSource="false">
      </mdEdge>  

<!-- 
      <object key="ROOT" type="net.geoprism.registry.graph.BaseGeoObjectType">
        <attribute name="code" value="ROOT" />
      </object>                
 -->      
    </create>
  </doIt>
  <undoIt>
    <delete>
      <object type="com.runwaysdk.system.metadata.MdEdge" key="net.geoprism.registry.graph.HasGeometry" />
      <object type="com.runwaysdk.system.metadata.MdEdge" key="net.geoprism.registry.graph.HasValue" />
    
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeShapeValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeLineValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributePolygonValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributePointValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeGeometryValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeTermValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeLocalValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeLongValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeDateValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeCharacterValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeBooleanValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeBasicValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeValue" />    
      
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeGeometryType" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeClassificationType" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeTermType" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeLocalType" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeDoubleType" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeLongType" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeDateType" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeCharacterType" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeUUIDType" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeBooleanType" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeType" />
      
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.HierarchicalRelationshipType" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.GeoObjectType" />      
      
      
    </delete>
  </undoIt>
</version>