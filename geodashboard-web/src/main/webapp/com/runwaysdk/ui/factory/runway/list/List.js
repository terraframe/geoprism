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
/**
 * RunwaySDK Javascript UI library.
 * 
 * @author Terraframe
 */
//define(["../../../../ClassFramework", "../widget/Widget"], function(ClassFramework){
(function(){

  var ClassFramework = Mojo.Meta;
  
var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
var RUNWAY_UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");

var ListItem = ClassFramework.newClass(Mojo.RW_PACKAGE+'ListItem', {
  Implements: RUNWAY_UI.ListItemIF,
  
  Extends : RW.Widget,
  
  Instance : {
    initialize : function(data)
    {
      this.$initialize("li");
      
      this._data = data;
      
      if (data != null) {
        this.setInnerHTML(this._data.toString());
      }
    },
    getData : function()
    {
      return this._data;
    },
    hasLI : function(LI)
    {
      if (RUNWAY_UI.HTMLElementIF.getMetaClass().isInstance(LI)) {
        return this.equals(LI);
      }
      else {
        return this.getRawEl() === LI;
      }
    },
  }
});

var List = ClassFramework.newClass(Mojo.RW_PACKAGE+'List', {
  
  Extends : RW.Widget,
  
  Implements : RUNWAY_UI.ListIF,
  
  Instance: {
    initialize : function(attrs, styles, items) {
      this.$initialize("ul", attrs, styles);
      
      items = items || [];
      
      for (var i = 0; i < items.length; i++)
      {
        this.addItem(items[i]);
      }
    },
    _makeListItem : function(item) {
      if (item instanceof ListItem) {
      }
      else if (ClassFramework.extendsBase(item))
      {
        item = new ListItem(item);
      }
      else
      {
        throw new com.runwaysdk.Exception("Can only add ListItems or objects that extend Base to the list.");
      }
      // We can't put this dispatchEvent here because we need the event to be dispatched AFTER we've appended the item to the list. (DD requires this)
      //this.dispatchEvent(new RUNWAY_UI.AddItemEvent(item));
      
      return item;
    },
    addItem : function(item)
    {
      item = this._makeListItem(item);
      this.appendChild(item);
      this.dispatchEvent(new RUNWAY_UI.AddItemEvent(item));
      return item;
    },
    removeItem : function(item)
    {
      var items = this.getChildren();
      var oldItem = null;
      // Remove at the last index if no arguments are specified
      if (arguments.length === 0)
      {
        if(items.length > 0)
        {
          oldItem = items[items.length-1];
        }
        else
        {
          throw new com.runwaysdk.Exception("Cannot remove ListItem at index 0 because the list is empty.");
        }
      }
      else if (Mojo.Util.isNumber(item))
      {
        if (item < this.size())
        {
          oldItem = items[item];
        }
        else
        {
          throw new com.runwaysdk.Exception("Cannot remove ListItem at index "+item+" because it is out of bounds (size: "+size+").");
        }
      }
      else if (item instanceof ListItem)
      {
        if (this.hasItem(item))
        {
          oldItem = item;
        }
      }
      
      if (!(this.hasItem(oldItem)))
      {
        throw new com.runwaysdk.Exception("The item " + item + " is not present in the list.");
      }
      
      this.removeChild(oldItem);
      
      this.dispatchEvent(new RUNWAY_UI.RemoveItemEvent(oldItem));
      
      return oldItem;
    },
    clearItems : function()
    {
      while (this.size() > 0)
      {
        this.removeItem();
      }
    },
    hasItem : function(item)
    {
      var id = item instanceof ListItem ? item.getId() : item;
      return this.getChild(id) !== null;
    },
    getItem : function(item)
    {
      if (Mojo.Util.isNumber(item))
      {
        var items = this.getChildren();
        return this.getChild(items[item]);
      }
      else if (Mojo.Util.isString(item))
      {
        return this.getChild(item);
      }
      else
      {
        throw new com.runwaysdk.Exception("getItem requires an element ID or an index number.");
      }
    },
    getNextSiblingItem : function(item) {
      var listItems = this.getChildren();
      
      // Find the index of item
      var index = 0;
      for (var key in listItems) {
        if (item.equals( listItems[key] )) {
          break;
        }
        index++;
      }
      
      return listItems[index+1];
    },
    insertBefore : function(newItem, refItem) {
      newItem = this._makeListItem(newItem);
      
      this.$insertBefore(newItem, refItem);
      
      this.dispatchEvent(new RUNWAY_UI.AddItemEvent(newItem));
      
      return newItem;
    },
    replaceItem : function(newItem, oldItem)
    {
      newItem = this._makeListItem(newItem);
      
      if (!(this.hasItem(oldItem)))
      {
        throw new com.runwaysdk.Exception("The item " + item + " is not present in the list.");
      }
      
      this.replaceChild(newItem, oldItem);
      
      this.dispatchEvent(new RUNWAY_UI.RemoveItemEvent(oldItem));
      this.dispatchEvent(new RUNWAY_UI.AddItemEvent(newItem));
      
      return oldItem;
    },
    getAllItems : function()
    {
      return this.getChildren();
    },
    getItemByLI : function(LI)
    {
      var items = this.getChildren();
      
      for (var i = 0; i < items.length; i++)
      {
        var item = items[i];
        if (item.hasLI(LI))
        {
          return item;
        }
      }
      
      return null;
    },
    hasLI : function(LI) {
      if (this.getItemByLI(LI) == null) {
        return false;
      }
      else {
        return true;
      }
    },
    size : function()
    {
      return this.getChildren().length;
    },
  }
});

})();
