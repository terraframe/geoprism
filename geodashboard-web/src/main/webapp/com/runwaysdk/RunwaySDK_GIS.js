/*
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
//define(["RunwaySDK_DTO"], function(){
(function(){

Mojo.GIS_PACKAGE = Mojo.ROOT_PACKAGE+'gis.transport.attributes.';
Mojo.GIS_MD_PACKAGE = Mojo.ROOT_PACKAGE+'gis.transport.metadata.';

// geometry
var attrGeo = Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributeGeometryDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',

  IsAbstract : true,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});


var geoMd = Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributeGeometryMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',

  IsAbstract : true,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

// lineString
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributeLineStringDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributeLineStringMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

// point
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributePointDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributePointMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

// polygon
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributePolygonDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributePolygonMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});


// multiLine
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributeMultiLineStringDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributeMultiLineStringMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

// multiPoint
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributeMultiPointDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributeMultiPointMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

// multiPolygon
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributeMultiPolygonDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributeMultiPolygonMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

})();