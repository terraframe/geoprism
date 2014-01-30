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

//define(["./ClassFramework", "./Util"], function(ClassFramework, Util) {
(function(){

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  
  var Iterable = Mojo.Meta.newInterface(Mojo.STRUCTURE_PACKAGE+'Iterable', {
    Instance : {
      iterator : function(){}
    }
  });
  
  var Iterator = Mojo.Meta.newInterface(Mojo.STRUCTURE_PACKAGE+"Iterator", {
    Instance : {
      next:function(){},
      hasNext:function(){}
    }
  });
  
  var AbstractCollection = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'AbstractCollection', {
    IsAbstract: true,
    Instance : {
    }
  });
  
  var AbstractMap = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'AbstractMap', {
    Extends : AbstractCollection,
    IsAbstract : true,
    Instance : {
      initialize : function()
      {
        this.$initialize();
      }    
    }
  });
  
  // FIXME use hasOwnProperty() versus key in obj? Cross-browser/speed concerns?
  var HashMap = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'HashMap', {
    Extends: AbstractMap,
    Instance : {
      initialize : function(map)
      {
        this.$initialize();
        this._map = {};
        this._size = 0;
        
        if(map)
        {
          this.putAll(map);
        }
      },
      
      _getKey : function(key)
      {
        return ClassFramework.extendsBase(key) ? 
          key.getHashCode() : key.toString();
      },
      
      put : function(key, value)
      {
        var mapKey = this._getKey(key);
        
        var oldValue = null;
        var contains = this._containsKey(mapKey);
        if(contains)
        {
          oldValue = this._get(mapKey);
        }
        
        this._map[mapKey] = value;
        
        // We can only increase the size if we are not overwriting
        // and this check must be explicit because this._get(key) can
        // return null in the case where a key actually maps to a null
        // value. So incrementing the size if this._get(mapKey) === null
        // is not sufficient.
        if(!contains)
        {
          this._size++;
        }
        
        return oldValue;
      },
      
      // FIXME return boolean to match Java API?
      remove : function(key)
      {
        var mapKey = this._getKey(key);
        
        var oldValue = null;
        var contains = this._containsKey(mapKey);
        if(contains)
        {
          oldValue = this._get(mapKey);
        }
        
        delete this._map[mapKey];
        
        // We can only decrease the size if the key does not exist
        // and this check must be explicit because this._get(key) can
        // return null in the case where a key actually maps to a null
        // value. So decrementing the size if this._get(mapKey) === null
        // is not sufficient.
        if(contains)
        {
          this._size--;
        }
        
        return oldValue;
      },
      
      _get : function(mapKey)
      {
        return this._map.hasOwnProperty(mapKey) ? this._map[mapKey] : null;
      },
      
      get : function(key)
      {
        var mapKey = this._getKey(key);
        return this._get(mapKey);
      },
      
      clear : function()
      {
        var keys = Mojo.Util.getKeys(this._map, true);
        for (var i=0, len=keys.length; i<len; i++)
        {
          this.remove(keys[i]);
        }
      },
      
      _containsKey : function(mapKey)
      {
        return this._map.hasOwnProperty(mapKey);
      },
      
      containsKey : function(key)
      {
        var mapKey = this._getKey(key);
        return this._containsKey(mapKey);
      },
      
      containsValue : function(value)
      {
        for (var k in this._map)
        {
          if(this._map.hasOwnProperty(k) && Mojo.Util.equals(this._map[k], value))
          {
            return true;
          }
        }
        
        return false;
      },
      
      isEmpty : function()
      {
        return this._size === 0;
      },
      
      keySet : function()
      {
        return Mojo.Util.getKeys(this._map, true);
      },
      
      putAll : function(obj)
      {
        if(obj instanceof AbstractMap)
        {
          var keys = obj.keySet();
          for(var i=0; len=keys.length; i++)
          {
            var key = keys[i];
            var value = obj.get(key);
            this.put(key, value);
          }
        }
        else if(Util.isArray(obj))
        {
          for(var i=0; i<obj.length; i++)
          {
            var o = obj[i];
            this.put(o, o);
          }
        }
        else if(Util.isObject(obj))
        {
          for (var k in obj)
          {
            if(obj.hasOwnProperty(k))
            {
              this.put(k, obj[k]);
            }
          }
        }
      },
      
      size : function()
      {
        return this._size;
      },
      
      values : function()
      {
        return Mojo.Util.getValues(this._map, true);
      },
      /**
       * Serializes this Map into a basic JSON object.
       */
      toJSON : function(key){
        // only serialize the underlying map to avoid infinite recursion or other
        // circular issues.
        return new com.runwaysdk.StandardSerializer(this._map).toJSON(key);
      }
    }
  });
  
  // FIXME use common looping method with function callback
  var LinkedHashMap = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'LinkedHashMap', {
    Extends : HashMap,
    Instance : {
      initialize : function(map){
        this._head = null;
        this._tail = null;
        this.$initialize(map);
      },
      keySet : function(){
        var keys = [];
        var current = this._head;
        while(current !== null){
          keys.push(this.get(current.key));
          current = current._next;
        }
        return keys;
      },
      values : function(){
        var values = [];
        var current = this._head;
        while(current !== null){
          values.push(this.get(current.key));
          current = current._next;
        }
        return values;
      },
      replace : function(key, value, oldKey){
        var keyStr = this._getKey(key);
        var oldKeyStr = this._getKey(oldKey);
      
        if(!this.containsKey(oldKeyStr)){
          throw new com.runwaysdk.Exception('Cannot replace the non-existent key ['+bKey+'].');
        }
        else if(this.containsKey(keyStr)){
          throw new com.runwaysdk.Exception('Cannot replace with the key ['+key+'] because it already exists in the map.');
        }
        
        var current = this._head;
        while(current !== null){
          if(current.key === oldKeyStr){
            // found the old key so simply reset the key but retain all pointers
            this.$remove(current.key);
            this.$put(keyStr, value);
            current.key = keyStr;
            
            return;
          }
          
          current = current._next;
        }      
      },
      insert : function(key, value, bKey){
        var keyStr = this._getKey(key);
        var bKeyStr = this._getKey(bKey);
      
        if(!this.containsKey(bKey)){
          throw new com.runwaysdk.Exception('Cannot insert before the non-existent key ['+bKey+'].');
        }
        else if(this.containsKey(keyStr)){
          throw new com.runwaysdk.Exception('Cannot insert the key ['+key+'] because it already exists in the map.');
        }
        
        var current = this._head;
        while(current !== null){
          if(current.key === bKeyStr){
            // found the old key so insert the new one before it
            var node = {key: keyStr, prev: current.prev, _next: current};
            
            if(this._head === current){
              // reset the head reference as the new node
              this._head = node;
            }
            else {
              current.prev._next = node;
            }
            current.prev = node;
  
            this.$put(keyStr, value);
            
            return;
          }
          
          current = current._next;
        }
      },
      put : function(key, value){
  
        key = this._getKey(key);
        
        if(!this.containsKey(key)){
          if(this._head === null){
            this._head = {key:key, prev: null, _next: null};
            this._tail = this._head;
          }
          else {
            var node = {key:key, prev: this._tail, _next: null};
            this._tail._next = node;
            this._tail = node;
          }
        }
        
        return this.$put(key, value);
      },
      clear : function(){
        this.$clear();
        this._head = null;
        this._tail = null;
      },
      remove : function(key){
        key = this._getKey(key);
        
        if(this.containsKey(key)){
          var current = this._head;
          while(current !== null){
            if(key === current.key){
              
              if(current === this._head){
                // removing the first item
                this._head = current._next;
                if(this._head){
                  this._head.prev = null;
                }
                else {
                  this._tail = null; // no items left (head is already null)
                }
              }
              else if(current === this._tail){
                // removing the last item            
                this._tail = current.prev;
                this._tail._next = null;
              }
              else {
                // all other items
                current.prev._next = current._next;
                current._next.prev = current.prev;
              }
              
              break;
            }
            else {
              current = current._next;
            }
          }        
        }
        
        return this.$remove(key);
      }
    }
  });
  
  var AbstractSet = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'AbstractSet', {
    Extends: AbstractCollection,
    Implements : Iterable,
    IsAbstract : true,
    Instance : {
      initialize : function()
      {
        this.$initialize();
      }
    }
  });
  
  var ArrayIterator = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'ArrayIterator', {
    Implements : Iterator,
    Instance : {
      initialize : function(arr)
      {
        this._array = arr;
        this._ind = 0;
      },
      next:function(){
        return this._array[this._ind++];
      },
      hasNext:function(){
        return this._ind < this._array.length;
      }
    }
  });
  
  var HashSet = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'HashSet', {
    Extends: AbstractSet,
    Instance : {
      initialize : function(collection)
      {
        this.$initialize();
        this._map = new HashMap();
  
        if(collection)
        {
          this.addAll(collection);
        }
      },
      iterator : function()
      {
        return new ArrayIterator(this.toArray());
      },
      
      add : function(obj)
      {
        this._map.put(obj, obj);
      },
      
      addAll : function(obj)
      {
        if(obj instanceof AbstractCollection)
        {
          var iter = obj.iterator();
          while(iter.hasNext())
          {
            this.add(iter.next());
          }
        }
        else if(Util.isArray(obj))
        {
          for(var i=0; i<obj.length; i++)
          {
            this.add(obj[i]);
          }
        }
        else if(Util.isObject(obj))
        {
          for (var k in obj)
          {
            if(obj.hasOwnProperty(k))
            {
              this.add(obj[k]);
            }
          }
        }
        else
        {
          throw new Exception('Object type ['+typeof obj+'] is not a recognized ' +
              'parameter for ['+this.getMetaClass().getQualifiedName()+'.addAll].');
        }
      },
      
      clear : function()
      {
        this._map.clear();
      },
      
      contains : function(obj)
      {
        return this._map.containsKey(obj);
      },
      
      _toCollection : function(collection)
      {
        if(collection instanceof AbstractCollection)
        {
          return collection;
        }
        else
        {
          return new HashSet(collection);
        }
      },
      
      containsAll : function(collection)
      {
        var compareTo = this._toCollection(collection);
        
        var iter = compareTo.iterator();
        while(iter.next())
        {
          if(!this.contains(iter.next))
          {
            return false;
          }
        }
        
        return true;
      },
      
      containsExactly : function(obj)
      {
        var collection = this._toCollection(obj);
        return this.size() === collection.size() && this.containsAll(collection);
      },
      
      isEmpty : function()
      {
        return this._map.isEmpty();
      },
      
      remove : function(obj)
      {
        this._map.remove(obj);
      },
      
      removeAll : function()
      {
        this._map.clear();
      },
      
      retainAll : function(obj)
      {
        var modified = false;
        var collection = this._toCollection(obj);
        var values = this._map.values();
        for(var i=0, len=values.length; i<len; i++)
        {
          var value = values[i];
          if(!collection.contains(value))
          {
            this.remove(value);
            modified = true;
          }
        }
        
        return modified;
      },
      
      size : function()
      {
        return this._map.size();
      },
      
      toArray : function()
      {
        return this._map.values();
      }
    }
  });
  
  return Mojo.Meta.alias(Mojo.STRUCTURE_PACKAGE + "*");
  
})();
