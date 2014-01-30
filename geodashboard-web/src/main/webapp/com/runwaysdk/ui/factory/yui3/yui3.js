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
 * RunwaySDK Javascript UI Adapter for YUI3.
 * 
 * @author Terraframe
 */

//define(["../yui2/yui2"], function(){

(function(){

// Set native parsing to false because the system has been tested using
// non-native parsing and we don't any surprises, even if native is faster.
// Mojo.ClientSession.setNativeParsingEnabled(false);

var UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");
var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
Mojo.YUI3_PACKAGE = Mojo.UI_PACKAGE+'YUI3.';
var YUI2 = Mojo.Meta.alias(Mojo.YUI2_PACKAGE+'*');
var STRUCT = Mojo.Meta.alias('com.runwaysdk.structure.*');
var Facade = Mojo.Meta.findClass('com.runwaysdk.Facade'); // FIXME have Will tweak Mojo.Meta.alias

var Y = YUI().use('*');
//Y.config.debug = true;
//Y.DD.DDM._debugShim = true;

var Factory = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'Factory', {
  
  IsSingleton : true,
  
  Implements : UI.AbstractComponentFactoryIF,
  
  Instance: {
    newElement: function(el, attributes, styles) {
      if (UI.Util.isElement(el)) {
        return el;
      }
      else {
//        return UI.Manager.getFactory("Runway").newElement(el, attributes, styles);
        return new HTMLElement(el, attributes, styles);
      }
    },
    newDocumentFragment : function(el) {
      return UI.Manager.getFactory("Runway").newDocumentFragment(el);
    },
    newDialog : function (title, config) {
      return YUI2.Factory.getInstance().newDialog(title, config);
    },
    newButton : function(label, handler, el) {
      //return YUI2.Factory.getInstance().newButton(label, handler, el);
      return UI.Manager.getFactory("Runway").newButton(label, handler, el);
    },
    newList : function (title, config, items) {
      return UI.Manager.getFactory("Runway").newList(title, config, items);
    },
    newListItem : function(data){
      return UI.Manager.getFactory("Runway").newListItem(data);
    },
    newForm : function(config){
      return UI.Manager.getFactory("Runway").newForm(config);
    },
    newFormControl : function(type, config) {
      return UI.Manager.getFactory("Runway").newFormControl(type, config);
    },
    newDataTable : function (type, config) {
      return new DataTable(type, config);
    },
    newColumn : function(config){
      return new Column(config);
    },
    newRecord : function(obj){
      return new Record(obj);
    },
    newLayout : function (type, config)
    {
      if (type == "vertical")
      {
        return new VerticalLayout(config);
      }
      else if (type == "horizontal")
      {
        return new HorizontalLayout(config);
      }
      else
      {
        throw new com.runwaysdk.Exception("Invalid layout manager type: ["+type+"].")
      }
    },
    makeDraggable : function(elProvider, config) {
      if (UI.ListIF.getMetaClass().isInstance(elProvider)) {
        return new DragList(elProvider, config);
      }
      else {
        throw new com.runwaysdk.Exception(elProvider + " does not have a default drag implementation.");
      }
    },
    makeDroppable : function(elProvider, config) {
      if (UI.ListIF.getMetaClass().isInstance(elProvider)) {
        return new DropList(elProvider, config);
      }
      else {
        throw new com.runwaysdk.Exception(elProvider + " does not have a default drop implementation.");
      }
    },
    newContextMenu : function(config) {
      return UI.Manager.getFactory("Runway").newContextMenu(config);
    }
  }
});
UI.Manager.addFactory("YUI3", Factory);

// YUI specific implementations and subclasses
// DataSource
// TODO can this be a plugin?
var RunwayDataSource = function(type, containingWidget) {
    RunwayDataSource.superclass.constructor.apply(this);
    this._containingWidget = containingWidget;
    
    this._type = type;
    this._taskQueue = new STRUCT.TaskQueue();
    this._metadataQueryDTO = null;
    this._resultsQueryDTO = null;
    this._requestEvent = null;
    this._attributeNames = [];
    
    // query attributes
    this._pageNumber = 1;
    this._pageSize = 20;
    this._sortAttribute = null;
    this._ascending = true;
};

Y.mix(RunwayDataSource, {
    NAME: "DataSourceRunwayQuery",
    ATTRS: {
        source: {
        }
    }
});
    
Y.extend(RunwayDataSource, Y.DataSource.Local, {

    resetQuery : function(){
      this._pageNumber = 1;
      this._pageSize = 20;
      this._sortAttribute = null;
      this._ascending = true;
      if(this._resultsQueryDTO !== null)
      {
        this._resultsQueryDTO.clearOrderByList();
      }
    },

    setPageNumber : function(pageNumber){
      this._pageNumber = pageNumber;
    },
    
    setPageSize : function(pageSize){
      this._pageSize = pageSize;
    },
    getSortAttribute : function(){
      return this._sortAttribute;
    },
    setSortAttribute : function(sortAttribute){
      // TODO accept multiple attributes for priority sorting
      this._sortAttribute = sortAttribute;
    },
    
    toggleAscending : function(){
      this.setAscending(!this.isAscending());
    },
    
    setAscending : function(ascending){
      this._ascending = ascending;
    },
    
    isAscending : function(){
      return this._ascending;
    },
    
    metadataLoaded : function(){
      return this._metadataLoaded;
    },

    _getQueryDTO : function(){
      var thisDS = this;
      
      var clientRequest = new Mojo.ClientRequest({
        onSuccess : function(queryDTO){
          
          // The initial value of the results QueryDTO is the
          // metadata object because we have to start with something
          // and the metadata QueryDTO works as a template.
          thisDS._metadataQueryDTO = queryDTO;
          thisDS._resultsQueryDTO = queryDTO;
          
          // notify any listeners that we have some metadata available
          var attrs = [];
          thisDS._attributeNames = [];
          var names = queryDTO.getAttributeNames();
          var RefDTO = com.runwaysdk.transport.attributes.AttributeReferenceDTO;
          var StructDTO = com.runwaysdk.transport.attributes.AttributeStructDTO;
          for(var i=0; i<names.length; i++)
          {
            var name = names[i];
            var attrDTO = queryDTO.getAttributeDTO(name);
            
            // TODO Custom filter needed. Should only allow primitives based on IF
            if(!(attrDTO instanceof RefDTO) && !(attrDTO instanceof StructDTO)
              && !attrDTO.getAttributeMdDTO().isSystem() && name !== 'keyName'
              && attrDTO.isReadable()){
                thisDS._attributeNames.push(name);
                attrs.push(attrDTO);
            }
          }
          
          // TODO publish this event more formally
          var metadata = {
            attributes : attrs
          };
          thisDS.fire('runwaysdk:MetaDataLoaded', metadata);
          thisDS._metadataLoaded = true;
          
          thisDS._taskQueue.next(); // invokes _getResultSet
        },
        onFailure : function(e){
          thisDS._requestEvent.error = e;
          thisDS.fire("data", thisDS._requestEvent);
        }
      });
      
      Facade.getQuery(clientRequest, this._type);
    },
    _getResultSet : function(){
    
      var thisDS = this;
      
      var clientRequest = new Mojo.ClientRequest({
        onSuccess : function(queryDTO){
          thisDS._resultsQueryDTO = queryDTO;
          thisDS._finalizeRequest();
        },
        onFailure : function(e){
          thisDS._requestEvent.error = e;
          thisDS.fire("data", thisDS._requestEvent);
        }
      });
      
      // setup the query parameters
      this._resultsQueryDTO.setCountEnabled(true);
      this._resultsQueryDTO.setPageSize(this._pageSize);
      this._resultsQueryDTO.setPageNumber(this._pageNumber);
      
      
      if(Mojo.Util.isString(this._sortAttribute)){
        this._resultsQueryDTO.clearOrderByList();
        var order = this._ascending ? 'asc' : 'desc';
        this._resultsQueryDTO.addOrderBy(this._sortAttribute, order);
      }
      
      this._containingWidget.dispatchEvent(new PreLoadEvent(this._resultsQueryDTO));      
      
      Facade.queryEntities(clientRequest, this._resultsQueryDTO);
    },
    
    _finalizeRequest : function(){
      
      // convert each DTO into an object literal
      var json = [];
      var resultSet = this._resultsQueryDTO.getResultSet();   
      for(var i=0; i<resultSet.length; i++)
      {
        var result = resultSet[i];
         // always include the id and type for dereferencing
        var obj = {
          id:result.getId(),
          type:result.getType()
        };
        for(var j=0; j<this._attributeNames.length; j++){
          var name = this._attributeNames[j];
          var value = result.getAttributeDTO(name).getValue();
          value = value !== null ? value : '';
          obj[name] = value;
        }
        
        json.push(obj);
      }

      this.fire("data", Y.mix({data:json}, this._requestEvent));
      
      // FIXME pass in as extra params instead of mixed in
      var pagination = {
        pageNumber : this._pageNumber,
        pageSize : this._pageSize,
        count : this._resultsQueryDTO.getCount()
      }
      this.fire('runwaysdk:pagination', pagination);
    },
    
    _loadClass : function(){
      
      var thisDS = this;
      var clientRequest = new Mojo.ClientRequest({
        onSuccess : function(){
          thisDS._taskQueue.next();
        },
        onFailure : function(e){
          thisDS._requestEvent.error = e;
          thisDS.fire("data", thisDS._requestEvent);        
        }
      });
      
      Facade.importTypes(clientRequest, [this._type], {autoEval : true});
    },

    _defRequestFn: function(evt) {
      
      this._requestEvent = evt;
      var thisDS = this;
      
      // 1. Fetch the class if it's not loaded
      // FIXME needs to be able to load subclasses
      if(!Mojo.Meta.classExists(this._type)){
        throw new com.runwaysdk.Exception('Operation to lazy load DataSource type ['+this._type+'] is not supported.');
        /*
        this._taskQueue.addTask(new STRUCT.TaskIF({
          start : function(){
            thisDS._loadClass();      
          }
        }));
        */        
      }
      
      // 2. Fetch the QueryDTO with metadata for the DataSchema
      if(!this.metadataLoaded()){
        this._taskQueue.addTask(new STRUCT.TaskIF({
          start : function(){
            thisDS._getQueryDTO();      
          }
        }));
      }
      
      // 3. Perform the query and get a result set
      this._taskQueue.addTask(new STRUCT.TaskIF({
        start : function(){
          thisDS._getResultSet();
        }
      }));
      
      this._taskQueue.start();
      
      return this._requestEvent.tId; // transaction id
    }
});

/**
 * Base class of YUI3 wrapped widgets that contains YUI boilerplate code and
 * lifecycle management.
 */
var YUI3WidgetBase = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'YUI3WidgetBase',{
  Extends : UI.WidgetBase,
  IsAbstract : true,
  Instance : {
    initialize : function(id){
      this.$initialize(id);
    },
    destroy : function(){
      this.$destroy();
      
      var impl = this.getImpl();
      if(impl instanceof Y.Base){
        impl.destroy();
      }
    }
  }
});

/**
 * Fired before the underlying DataSource of a widget (e.g., DataTable) loads
 * a result set.
 */
var PreLoadEvent = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'PreLoadEvent', {
  Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
  Instance : {
    initialize : function(queryObject)
    {
      this.$initialize();
      this._queryObject = queryObject;
    },
    getQueryObject : function() { return this._queryObject; }
  }
});

// --------------------------------

var DataTable = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'DataTable', {
  Implements : UI.DataTableIF,
  Extends : YUI3WidgetBase,
  Instance : {
    initialize : function(type, addedColumns){
    
      this.$initialize();
      
      if(Mojo.Util.isString(type))
      {
        this._dataSource = new RunwayDataSource(type, this);
      }
      else
      {
        throw new com.runwaysdk.Exception('Cannot create a DataTable of type ['+type+'].');
      }
      
      this._paginationDiv = null;
      this._pages = new com.runwaysdk.structure.HashMap(); // mapping between DOM id and page number
      this._dataSource.on('runwaysdk:MetaDataLoaded', this._mdLoaded, this);
      this._dataSource.on('runwaysdk:pagination', this._insertPagination, this);
      
      
      this._dataTable = new Y.DataTable.Base({
        columnset:[]
      });

      this._dataTable.on('render', this._renderHandler, this); 
      
      this._dataTable.plug(Y.Plugin.DataTableDataSource, {
        datasource : this._dataSource
      });

      this._columnSet = new ColumnSet(this._dataTable.get('columnset'));
      this._recordSet = new RecordSet(this._dataTable.get('recordset'));
      
      this._dataTable.delegate('click', this._thClickHandler, 'th', this);

      // extra columns to add to the end of the table
      this._addedColumns = addedColumns || [];
      
      this._typeFormatters = {};
    },
    _renderHandler : function(e){
      this._setRendered(true);
    },
    resetDataTable : function(){
      this._dataSource.resetQuery();
      this.load();
    },
    _selectPage : function(e){
      var page = this._pages.get(e.target.get('id'));
      if(page){
        this._dataSource.setPageNumber(page.getPageNumber());
        this.load();
      }
    },
    _insertPagination: function(e) {
      var pageNumber = e.pageNumber;
      var pageSize = e.pageSize;
      var count = e.count;

      var pagination = new UI.Pagination(pageNumber, pageSize, count);
      var pages = pagination.getPages();
      
      // clear the old pagination
      if(this._paginationDiv){
        this._paginationDiv.setInnerHTML('');
        this._pages.clear();
      }
      else {
        this._paginationDiv = this.getFactory().newElement('div');
        this._paginationDiv.getImpl().on('click', this._selectPage, this);
      }

      for(var i=0; i<pages.length; i++)
      {
        var page = pages[i];
  
        var span = this.getFactory().newElement('span');
				span.addClassName("form-gen-available-pages");
        this._pages.put(span, page);
  
        if(page.isLeft() || page.isRight())
        {
          span.setInnerHTML('...');
        }
        else if(page.isCurrentPage())
        {
          span.setInnerHTML(page.getPageNumber());
        }
        else
        {
          span.setInnerHTML(page.getPageNumber());
        }
  
        this._paginationDiv.appendChild(span);
      }
      var countSpan = this.getFactory().newElement('span');
			countSpan.addClassName("form-gen-total-pages");
      var max = (pageNumber * pageSize);
      if(max > count) max = count;
      countSpan.setInnerHTML(" " + (((pageNumber-1) * pageSize)+1)+ "-" +max+" / "+ count);
      this._paginationDiv.appendChild(countSpan);
      
      var boundingBox = this._dataTable.get("contentBox");
      boundingBox.appendChild(this._paginationDiv.getImpl());
    },
    _thClickHandler : function(e){
      var th = e.currentTarget;
      var id = th.get('id');
      e.halt();
      
      // Dereference the column by id instead of key.
      // This returns a YUI Column object, which is okay
      // because we're operating on the implementation instead
      // of a ColumnIF
      var column = this._columnSet.getImplColumnById(id);
      var attribute = column.get('key'); // TODO needs namespacing w/ type
      
      var lastSortAttribute = this._dataSource.getSortAttribute();
      
      if(lastSortAttribute === attribute){
        this._dataSource.toggleAscending();
      }
      else {
        this._dataSource.setSortAttribute(attribute);
        this._dataSource.setAscending(true);
      }
      
      this._dataSource.setPageNumber(1);
      
      this.load();
    },
    
    _mdLoaded : function(e){
      
      if(!this._dataSource.metadataLoaded())
      {
        var attrs = e.attributes;
        var cols = [];
        
        for(var i=0; i<attrs.length; i++)
        {
          var attr = attrs[i];
          
          var label = attr.getAttributeMdDTO().getDisplayLabel();
          var key = attr.getName();
          
          cols.push(new Column({
            key : key,
            label : label,
            sortable : true,
            formatter : (this._typeFormatters[attr.getType()] || null)
          }));
        }
        
        cols = cols.concat(this._addedColumns);
      
        this._columnSet.addColumn(cols);
      }
      
      if(!this.isRendered())
      {
        var parent = this.getParent().getRawEl();
        this._dataTable.render(parent);
      }
    },
    setTypeFormatter : function(type, formatter){
      this._typeFormatters[type] = formatter;
    },
    getColumnSet : function(){
      return this._columnSet;
    },
    getRecordSet : function(){
      return this._recordSet;
    },
    getImpl : function(){
      return this._dataTable;
    },
    getEl : function(){
      return this._dataTable;
    },
    getContentEl : function() {
      return this.getEl(); // FIXME : Change this to the real content el (I don't know your datatable layout)
    },
    render : function(parentNode){
      if(this.isRendered())
      {
        this._dataTable.render(parentNode);
      }
      else
      {
        var parent = parentNode ? this.getFactory().newElement(parentNode) : UI.DOMFacade.getBody();
        this.setParent(parent);
        this.load();
      }
    },
    load : function(){
      this._dataTable.datasource.load();
    }
  }
});

var ColumnSet = Mojo.Meta.newClass(Mojo.UI_PACKAGE+"ColumnSet", {
  Implements : UI.ColumnSetIF,
  Instance : {
    initialize : function(columnSet){
      this._set = columnSet;
    },
    _getDefinitions : function(){
      return this._set.get('definitions');
    },
    _setDefinitions : function(defs, append){
      if(append){
        defs = this._getDefinitions().concat(defs);  
      }

      this._set.set('definitions', defs);
    },
    /**
     * Returns the column mapped to the given key.
     */
    getColumnByKey : function(key){
      this._getDefinitions('definitions').keySet
    },
    getColumnByIndex : function(ind){
      
    },
    /**
     * Returns the Column object with the given DOM id.
     */
    getImplColumnById : function(id){
      return this._set.idHash[id];
    },
    /**
     * @param columnIF a ColumnIF or ColumnIF[] object.
     */
    addColumn : function(columnIF){
      
      var cols = Mojo.Util.isArray(columnIF) ? columnIF : [columnIF];
      var defs = Mojo.Iter.map(cols, function(col) {
        return {
          key : col.getKey(),
          label : col.getLabel(),
          sortable : col.isSortable(),
          formatter : col.getFormatter()
        };
      });
      
      this._setDefinitions(defs, true);
      this._set.initializer();
    },
    
    /**
     * @param columnIF a ColumnIF or ColumnIF[] object.
     */    
    removeColumn : function(columnIF){
      var keys = new com.runwaysdk.structure.HashSet();
      if(Mojo.Util.isArray(columnIF)){
        keys.addAll(Mojo.Iter.map(columnIF, function(col){
          return col.getKey();
        }));
      }
      else {
        keys.add(columnIF.getKey());
      }
      
      var cols = [];
      var defs = this._getDefinitions();
      
      for(var i=0, len=defs.length; i<len; i++){
        var col = defs[i];
        if(keys.contains(col.getKey())){
          cols.push(col);
        }
      }

      this._setDefinitions(cols);
      this._set.initializer();
    }
  }
});

var Column = Mojo.Meta.newClass(Mojo.UI_PACKAGE+"Column", {
  Implements : UI.ColumnIF,
  Instance : {
    initialize : function(config){
      
      this._col; // impl
      if(config instanceof Y.Column)
      {
        this._col = config;
      }
      else
      {
        this._col = new Y.Column({
          key : config.key || Mojo.Util.generateId(),
          label : config.label || '',
          sortable : config.sortable || false,
          formatter : config.formatter || null
        });
      }
    },
    getImpl : function(){
      return this._col;
    },
    isSortable : function() {
      return this._col.get('sortable');
    },
    getKey : function(){
      return this._col.get('key');
    },
    getLabel : function(){
      return this._col.get('label');
    },
    getFormatter : function(){
      return this._col.get('formatter');
    },
    setFormatter : function(formatter){
      this._col.set('formatter', formatter);
    }
  }
});

var RecordSet = Mojo.Meta.newClass(Mojo.UI_PACKAGE+"RecordSet", {
  Implements : UI.RecordSetIF,
  Instance : {
    initialize : function(recordSet){
      this._set = recordSet;
    },
    /**
     * @param recordIF a RecordIF or RecordIF[] object.
     */
    addRecord : function(recordIF){
    },
    
    /**
     * @param RecordIF a ColumnIF or RecordIF[] object.
     */    
    removeRecord : function(recordIF){
    }
  }
});

var Record = Mojo.Meta.newClass(Mojo.UI_PACKAGE+"Record", {
  Implements : UI.RecordIF,
  Instance : {
    initialize : function(record){
      this.record = record;
    },
    getValue : function(key){} // String
  }
});

var HTMLElement = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'HTMLElement', {
    
    Implements : UI.HTMLElementIF,
  
    Extends : UI.HTMLElementBase,
    
    Instance : {
      initialize : function(el, attributes, styles)
      {
        if (el instanceof Y.Node) {
          this._impl = el;
        }
        else {
          el = UI.Util.stringToRawElement(el);
          this._impl = Y.one(el);
        }
        
        this.$initialize(el, attributes, styles);
      },
      /**
       * Override to generate the id based on the DOM element.
       */
      _generateId : function(){
        var id = UI.DOMFacade.getAttribute(this.getRawEl(), 'id');
        return Mojo.Util.isString(id) && id.length > 0 ? id : this.$_generateId();
      },
      addClassName : function(name)
      {
        this.getImpl().addClass(name);
      },
      hasClassName : function(name)
      {
        return this.getImpl().hasClass(name);
      },
      removeClassName : function(name)
      {
        this.getImpl().removeClass(name);
      },
      getElementsByClassName : function(className, tag)
      {
        return this.getRawEl().getElementsByClassName(className, tag);
      },
      getRawEl : function()
      {
        return Y.Node.getDOMNode(this.getImpl());
      },
      normalize : function()
      {
        this.getRawEl().normalize();
      },
      setInnerHTML : function (html)
      {
        this.getImpl().setContent(html);
      },
      getInnerHTML : function ()
      {
        return this.getImpl().getContent();
      },
      getChildren : function() {
        var children = this.getImpl().get('children');
        
        var len = children.length;
        var elementIFs = [len];
        var f = this.getFactory();
        for(var i=0; i<len; i++)
        {
          elementIFs[i] = f.newElement(children[i]);
        }
        return elementIFs;
      }
    }
});

/************************
 ****** Drag Drop *******
 ************************/
com.runwaysdk.ui.DD_CURRENT_DRAG = null
var BaseDrag = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'Drag', {
  IsAbstract: true,
  Implements : UI.DragIF,
  Instance : {
    initialize : function()
    {
      var delegate = this.getDragDelegate();
      this._data = this.getDragData();
      var config = this.getDragConfig();
      
      if (delegate == null) {
        throw new com.runwaysdk.Exception("You must specify a drag delegate.");
      }
      if (config == null || config.selector == null) {
        throw new com.runwaysdk.Exception("You must specify a selector for your drag delegation.");
      }
      
      var that = this;
      this.addEventListener(DragStartEvent, this.onDragStart);
      this.addEventListener(DragEndEvent, this.onDragEnd);
      this.addEventListener(DragEvent, this.onDrag);
      delegate.addEventListener(com.runwaysdk.event.DestroyEvent, function() {that.destroy()});
      
      delegate = delegate.getEl().getRawEl();
      
      this._delegate = new Y.DD.Delegate({
        container: delegate,
        nodes: /* "#" + delegate.id + " " + */ config.selector,
        dragConfig : {data: {data: this._data, dragObject: that}}
      });
      
      if (config.constrain2node != null) {
        this._delegate.dd.plug(Y.Plugin.DDConstrained, {
          constrain2node: config.constrain2node
        });
      }
      
      // FIXME : allow a way to change this via config
      this._delegate.dd.plug(Y.Plugin.DDProxy, {
        moveOnEnd: false
      });
      
      // FIXME : allow a way to change this via config
      this._delegate.dd.plug(Y.Plugin.DDNodeScroll, {
        node: delegate
      });
      
      this._delegate.on("drag:start", function(e) { that.dispatchEvent(new DragStartEvent(e, that)) } );
      this._delegate.on("drag:end", function(e) { that.dispatchEvent(new DragEndEvent(e, that)) } );
      this._delegate.on("drag:drag", function(e) { that.dispatchEvent(new DragEvent(e, that)) } );
    },
    
    destroy : function() {
      this._delegate.destroy();
      this.$destroy();
    },
    
    syncTargets : function() {
      this._delegate.syncTargets();
      
      // Update the data on our all new children so that they can reference us in DropEvents
      /*
      var children = UI.DOMFacade.getChildren(this.getDragDelegate());
      for (var k in children) {
        var child = children[k];
        if (child.tagName.toUpperCase() === this.getDragConfig().selector.toUpperCase()) {
          var drag = Y.DD.DDM.getDrag(child);
          
          if (drag) {
            var data = drag.get("data");
            if (data == null) {
              data = {
                data: {}
              };
            }
            
            data.dragObject = this;
            drag.set("data", data);
          }
        }
      }
      */
    },
    
    // Extending classes are not required to implement these methods from DragIF:
    getDragData : function() {},
    getDragTarget : function(HTMLElementIF) { return HTMLElementIF; },
    onDragStart : function(dragStartEvent) {},
    onDrag : function(dragEvent) {},
    onDragEnd : function(dragEndEvent) {}
  }
});

// Drag Events :
var BaseDragEvent = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'BaseDragEvent', {
  IsAbstract: true,
  Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
  Instance : {
    initialize : function(e, drag)
    {
      this.$initialize();
      this._e = e;
      this._dragEl = e.target.get("node");
      this._ghost = e.target.get("dragNode");
      this._drag = drag;
      this._data = e.target.get("data").data;
    },
    getEventImpl : function() { return this._e; },
    getDrag : function() { return this._drag; }, // Our drag object
    getDragImpl : function() { return this._e.target; }, // YUI3's drag object
    getDragTarget : function() { return this.getDrag().getDragTarget(UI.Manager.getFactory().newElement(this._dragEl)); }, // the HTMLElement being dragged
    getDragData : function() { return this._data; },
    getGhost : function() { return UI.Manager.getFactory().newElement(this._ghost); }
  }
});
var DragStartEvent = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'DragStartEvent', {
  Extends : BaseDragEvent,
  Instance : {
    initialize : function(e, drag)
    {
      this.$initialize(e, drag);
    },
    defaultAction : function() {
      this._dragEl.setStyle("opacity", ".25")
      
      this._ghost.set('innerHTML', this._dragEl.get('innerHTML'));
      this._ghost.setStyles({
        opacity: '.5',
        borderColor: this._dragEl.getStyle('borderColor'),
        backgroundColor: this._dragEl.getStyle('backgroundColor')
      });
      
      com.runwaysdk.ui.DD_CURRENT_DRAG = this.getDrag();
    }
  }
});
var DragEvent = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'DragEvent', {
  Extends : BaseDragEvent,
  Instance : {
    initialize : function(e, drag)
    {
      this.$initialize(e, drag);
    },
    defaultAction : function() {
      
    }
  }
});
var DragEndEvent = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'DragEndEvent', {
  Extends : BaseDragEvent,
  Instance : {
    initialize : function(e, drag)
    {
      this.$initialize(e, drag);
    },
    defaultAction : function() {
      this._dragEl.setStyle("opacity", null);
    }
  }
});
var BaseDrop  = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'Drop', {
  IsAbstract: true,
  Implements : UI.DropIF,
  Instance : {
    initialize : function()
    {
      var delegate = this.getDropDelegate();
      var config = this.getDropConfig();
      
      if (delegate == null) {
        throw new com.runwaysdk.Exception("You must specify a drop delegate.");
      }
      if (config == null || config.selector == null) {
        throw new com.runwaysdk.Exception("You must specify a selector for your drop delegation.");
      }
      
      var that = this;
      this.addEventListener(DropEnterEvent, this.onDropEnter);
      this.addEventListener(DropExitEvent, this.onDropExit);
      this.addEventListener(DropHitEvent, this.onDropHit);
      this.addEventListener(CanDropEvent, this.onCanDrop);
      this.addEventListener(DropOverEvent, this.onDropOver);
      delegate.addEventListener(com.runwaysdk.event.DestroyEvent, function() {that.destroy()});
      
      delegate = delegate.getEl().getRawEl();
      
      this._delegate = new Y.DD.Delegate({
        container: delegate,
        nodes: config.selector,
        invalid: config.selector,
        target:true // allow this delegate to be a drop target
      });
      
      this._delegate.on("drop:enter", function(e) { that.dispatchEvent(new DropEnterEvent(e, that)) } );
      this._delegate.on("drop:exit", function(e) { that.dispatchEvent(new DropExitEvent(e, that)) } );
      this._delegate.on("drop:hit", this.dispatchDropHitEvent, this );
      this._delegate.on("drop:over", function(e) { that.dispatchEvent(new DropOverEvent(e, that)) } );
    },
    
    dispatchDropHitEvent : function(e) {
      if (this.dispatchEvent(new CanDropEvent(e, this))) {
        this.dispatchEvent(new DropHitEvent(e, this));
      }
    },
    
    getYUIDelegate : function() {
      return this._delegate;
    },
    
    syncTargets : function() {
      this._delegate.syncTargets();
    },
    
    // Extending classes are not required to implement these methods from DropIF:
    onDropEnter : function() {},
    onDropExit : function() {},
    onDropHit : function() {},
    onDropOver : function() {},
    onCanDrop : function() {},
    getDragData : function() {},
    getDropTarget : function(HTMLElementIF) { return HTMLElementIF; }
  }
});

// Drop Events :
var BaseDropEvent = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'BaseDropEvent', {
  IsAbstract: true,
  Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
  Instance : {
    initialize : function(e, drop)
    {
      this.$initialize();
      this._e = e;
      this._dropEl = e.target.get("node");
      this._drop = drop;
      this._data = e.target.get("data");
    },
    getEventImpl : function() { return this._e; },
    getDrop : function() { return this._drop; }, // Our drop class
    getDropImpl : function() { return this._e.target; }, // YUI3's drop class
    getDropTarget : function() { return this.getDrop().getDropTarget(UI.Manager.getFactory().newElement(this._dropEl)); }
  }
});
var DropEnterEvent = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'DropEnterEvent', {
  Extends : BaseDropEvent,
  Instance : {
    initialize : function(e, drop)
    {
      this.$initialize(e, drop);
      this._dragEl = this._e.drag.get("node");
    },
    getDrag : function() {
      return this._e.drag.get("data").dragObject;
      //return com.runwaysdk.ui.DD_CURRENT_DRAG;
    },
    getDragImpl : function() {
      return this._e.drag;
    },
    getDragTarget : function() {
      return this.getDrag().getDragTarget(UI.Manager.getFactory().newElement(this._dragEl));
    },
    getDragData : function() {
      return this._e.drag.get('data').data;
    },
    defaultAction : function() {
      
    }
  }
});
var DropExitEvent = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'DropExitEvent', {
  Extends : BaseDropEvent,
  Instance : {
    initialize : function(e, drop)
    {
      this.$initialize(e, drop);
    },
    defaultAction : function() {
      
    }
  }
});
var CanDropEvent = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'CanDropEvent', {
  Extends : BaseDropEvent,
  Instance : {
    initialize : function(e, drop)
    {
      this.$initialize(e, drop);
      this._dragEl = this._e.drag.get("node");
    },
    getDrag : function() {
      return this._e.drag.get("data").dragObject;
      //return com.runwaysdk.ui.DD_CURRENT_DRAG;
    },
    getDragImpl : function() {
      return this._e.drag;
    },
    getDragTarget : function() {
      return this.getDrag().getDragTarget(UI.Manager.getFactory().newElement(this._dragEl));
    },
    getDragData : function() {
      return this._e.drag.get('data');
    },
    defaultAction : function() {
      
    }
  }
});
var DropHitEvent = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'DropHitEvent', {
  Extends : BaseDropEvent,
  Instance : {
    initialize : function(e, drop)
    {
      this.$initialize(e, drop);
      this._dragEl = this._e.drag.get("node");
    },
    getDrag : function() {
      return this._e.drag.get("data").dragObject;
      //return com.runwaysdk.ui.DD_CURRENT_DRAG;
    },
    getDragImpl : function() {
      return this._e.drag;
    },
    getDragTarget : function() {
      return this.getDrag().getDragTarget(UI.Manager.getFactory().newElement(this._dragEl));
    },
    getDragData : function() {
      return this._e.drag.get('data');
    },
    defaultAction : function() {
      if (this._e.drag.nodescroll != null) {
        this._e.drag.nodescroll.set('parentScroll', this._e.drop.get('node').get("parentNode"));
      }
      this._e.drop.sizeShim();
      Y.DD.DDM.syncActiveShims(true);
      
      //this._e.drag.stopDrag();
      //this
    }
  }
});
var DropOverEvent = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'DropOverEvent', {
  Extends : BaseDropEvent,
  Instance : {
    initialize : function(e, drop)
    {
      this.$initialize(e, drop);
      this._dragEl = this._e.drag.get("node");
    },
    getDrag : function() {
      return this._e.drag.get("data").dragObject;
      //return com.runwaysdk.ui.DD_CURRENT_DRAG;
    },
    getDragImpl : function() {
      return this._e.drag;
    },
    getDragTarget : function() {
      return this.getDrag().getDragTarget(UI.Manager.getFactory().newElement(this._dragEl));
    },
    getDragData : function() {
      return this._e.drag.get('data');
    },
    defaultAction : function() {
      
    }
  }
});

// Widget Specific Drag/Drops

var DragList = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'DragList', {
  Extends : BaseDrag,
  Instance : {
    initialize : function(list, config)
    {
      this._list = list;
      
      this._config = config || {};
      if (this._config.selector == null) {
        this._config.selector = "li";
      }
      
      this._list.addEventListener(UI.AddItemEvent, this.onAddItem, null, this);
      this._list.addEventListener(UI.RemoveItemEvent, this.onRemoveItem, null, this);
      
      this.$initialize();
    },
    onAddItem : function(addItemEvent) {
      this.syncTargets();
      
      // At this point the li is changing ownership from 1 list to another
      // If we don't update the reference to the draglist then the drag still thinks it belongs to the other list
      // Normally this would get fixed by the drag object being recreated, but if the drag is still in progress then it never does.
      // Update the reference to the draglist
      var drag = Y.DD.DDM.getDrag(addItemEvent.getListItem().getEl().getRawEl());
      
      if (drag) {
        var data = drag.get("data");
        if (data == null) {
          data = {
            data: {}
          };
        }
        
        data.dragObject = this;
        drag.set("data", data);
      }
    },
    onRemoveItem : function(removeItemEvent) {
      this.syncTargets();
    },
    destroy : function() {
      this._list.removeEventListener(UI.AddItemEvent, this.onAddItem);
      this._list.removeEventListener(UI.RemoveItemEvent, this.onRemoveItem);
    },
    
    // From DragIF
    onDragStart : function(dragStartEvent) {
      // This code is only required if the fix in onAddItem is also used
      var drag = Y.DD.DDM.getDrag(dragStartEvent.getDragTarget().getEl().getImpl());
      
      var data = drag.get("data");
      if (data == null) {
        data = {
          data: {}
        };
      }
      
      data.dragObject = this;
      drag.set("data", data);
    },
    getDragDelegate : function() {
      return this._list;
    },
    getDragConfig : function() {
      return this._config;
    },
    getDragTarget : function(HTMLElementIF) {
      // Attempt to convert it to a ListItem
      return this.getDragDelegate().getItemByLI(HTMLElementIF);
    }
  }
});
var DropList = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'DropList', {
  Extends : BaseDrop,
  Instance : {
    initialize : function(list, config)
    {
      this._list = list;
      
      this._config = config || {};
      if (this._config.selector == null) {
        this._config.selector = "li";
      }
      
      this._list.addEventListener(UI.AddItemEvent, this.onAddItem, null, this);
      this._list.addEventListener(UI.RemoveItemEvent, this.onRemoveItem, null, this);
      
      this.$initialize();
    },
    onAddItem : function(addItemEvent) {
      this.syncTargets();
    },
    onRemoveItem : function(removeItemEvent) {
      // Refresh the drop object
      removeItemEvent.getListItem().getEl().getImpl().unplug(Y.Plugin.Drop);
      this.syncTargets();
    },
    destroy : function() {
      this._list.removeEventListener(UI.AddItemEvent, this.onAddItem);
      this._list.removeEventListener(UI.RemoveItemEvent, this.onRemoveItem);
    },
    
    // From DropIF
    getDropDelegate : function() {
      return this._list;
    },
    getDropConfig : function() {
      return this._config;
    },
    getDropTarget : function(HTMLElementIF) {
      // Attempt to convert it to either a ListItem or a List
      
      // FIXME : add getTagName to HTMLElementIF?
      var dropTargetTagname = HTMLElementIF.getRawEl().tagName.toUpperCase();
      
      if (dropTargetTagname === "LI") {
        return this.getDropDelegate().getItemByLI(HTMLElementIF);
      }
      else if (dropTargetTagname === "UL") {
        return this.getDropDelegate();
      }
      
      return HTMLElementIF;
    },
    onCanDrop : function(canDropEvent) {
      var dragDel = canDropEvent.getDrag().getDragDelegate();
      var dropDel = canDropEvent.getDrop().getDropDelegate();
      
      if (!UI.ListIF.getMetaClass().isInstance(dragDel)) {
        canDropEvent.preventDefault();
      }
      
      var dragTarget = canDropEvent.getDragTarget();
      var dropTarget = canDropEvent.getDropTarget();
      
      if ( !UI.ListItemIF.getMetaClass().isInstance(dragTarget) ) {
        canDropEvent.preventDefault();
      }
      
      var isdropTargetListIF = UI.ListIF.getMetaClass().isInstance(dropTarget);
      
      if ( !(isdropTargetListIF || UI.ListItemIF.getMetaClass().isInstance(dropTarget)) ) {
        canDropEvent.preventDefault();
      }
      
      if (isdropTargetListIF && dropTarget.hasItem(dragTarget)) {
        canDropEvent.preventDefault();
      }
    },
    onDropOver : function(dropOverEvent) {
      // Forward this event to onDropHit
      this.dispatchDropHitEvent(dropOverEvent.getEventImpl());
    },
    onDropHit : function(dropHitEvent, dragTarget, dropTarget, dragList) {
      // both of these are ListItemIFs as guaranteed by our onCanDrop conditions
      var dragDel = dropHitEvent.getDrag().getDragDelegate();
      var dropDel = dropHitEvent.getDrop().getDropDelegate();
      
      // dragTarget is guaranteed to be a ListItemIF thanks to onCanDrop
      dragTarget = dragTarget || dropHitEvent.getDragTarget();
      // dropTarget can be either a ListItemIF or a ListIF
      dropTarget = dropTarget || dropHitEvent.getDropTarget();
      
      if (UI.ListIF.getMetaClass().isInstance(dropTarget)) {
          dragDel.removeItem(dragTarget);
          dropTarget.addItem(dragTarget);
      }
      else if (UI.ListItemIF.getMetaClass().isInstance(dropTarget)) {
        if (dragDel.getNextSiblingItem(dragTarget) === dropTarget) {
          dropTarget = dropDel.getNextSiblingItem(dropTarget);
          
          // There is no nextSibling, add it to the end of the list
          if (dropTarget == null) { return this.onDropHit(dropHitEvent, dragTarget, dropDel, dragDel) }
        }
        
        dragDel.removeItem(dragTarget);
        dropDel.insertBefore(dragTarget, dropTarget);
      }
    }
  }
});

// Layout Managers
var LayoutBase = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'LayoutBase', {
  Implements : UI.LayoutIF,
  Extends : UI.Composite,
  IsAbstract : true,
  Instance : {
    initialize : function(config)
    {
      this.$initialize();
      this._div = this.getFactory().newElement("div");
      config = config || {};
    },
    getImpl : function()
    {
      return this;
    },
    getEl : function()
    {
      return this._div;
    },
    getContentEl : function()
    {
      return this.getEl();
    },
    getRawEl : function()
    {
      return this.getEl();
    },
    addClassName : function(name)
    {
      this.getEl().addClassName(name);
    },
    appendChild : function(child)
    {
      //this.getContentEl().appendChild(child);
      this.$appendChild(child);
    }
  }
});

var VerticalLayout = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'VerticalLayout', {
  Extends: LayoutBase,
  Instance : {
    initialize : function(config)
    {
      this.$initialize(config);
    },
    appendChild : function(item)
    {
      this.$appendChild(item);
      
      var row = this.getFactory().newElement("div");
      row.addClassName("yui3-g");
      
      var col = this.getFactory().newElement("div");
      col.addClassName("yui3-u");
      
      col.appendChild(item);
      row.appendChild(col);
      this._div.appendChild(row);
    },
    getContentDivs : function()
    {
      return this._components.values();
    },
    getEl : function()
    {
      return this._div;
    }
  }
});

var HorizontalLayout = Mojo.Meta.newClass(Mojo.YUI3_PACKAGE+'HorizontalLayout', {
  Extends: LayoutBase,
  Instance : {
    initialize : function(config)
    {
      this.$initialize(config);
      this._row = this.getFactory().newElement("div");
      this._row.addClassName("yui3-g");
      this._div.appendChild(this._row);
    },
    appendChild : function(item)
    {
      this.$appendChild(item);
      var col = this.getFactory().newElement("div");
      col.addClassName("yui3-u");
      
      col.appendChild(item);
      this._row.appendChild(col);
    },
    getEl : function()
    {
      return this._div;
    }
  }
});

})();
