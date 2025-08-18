<!--

    Copyright (c) 2023 TerraFrame, Inc. All rights reserved.

    This file is part of Geoprism(tm).

    Geoprism(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Geoprism(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.

-->
<version xsi:noNamespaceSchemaLocation="classpath:com/runwaysdk/resources/xsd/version.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <doIt>
    <create>
      
      <mdVertex name="net.geoprism.registry.graph.AttributeDataSourceType" extends="net.geoprism.registry.graph.AttributeType" label="Attribute Source Type">
        <attributes>
        </attributes>
      </mdVertex>  
        
      <mdVertex name="net.geoprism.registry.graph.AttributeDataSourceValue" extends="net.geoprism.registry.graph.AttributeBasicValue" label="Attribute Source Value">
        <attributes>
          <graphReference name="value" label="Value" required="false" type="net.geoprism.registry.graph.DataSource"  />          
        </attributes>
      </mdVertex>    
    </create>
    
  </doIt>
  <undoIt>
    <delete>
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeDataSourceValue" />
      <object type="com.runwaysdk.system.metadata.MdVertex" key="net.geoprism.registry.graph.AttributeDataSourceType" />
    </delete>
  </undoIt>
</version>