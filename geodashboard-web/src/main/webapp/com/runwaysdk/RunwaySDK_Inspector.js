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
// refactor so that it's not dependent on the dom having loaded (I reference the body element directly)
// add record/stop auto-record+buffer for Tracer (and logger?)
// add settings tab for above
// Fix bug where method with same name for static/instance shows only instance (I think it executes this way too)
// viewing initialize's (and probably any overriden method) source does infinite recursion

//define(["./ClassFramework"], function(){
(function(){

Mojo.Meta.newClass('com.runwaysdk.inspector.Inspector', {

  IsSingleton : true,

  Instance : {
  
    initialize : function()
    {
      var prefix = this.getMetaClass().getQualifiedName()+'_';
      
      this._mainWindowId = prefix+'mainWindow';
      this._mainMinId = prefix+'mainMin';
      this._mainExitId = prefix+'mainExit';
      this._mainDrag = prefix+'mainDrag';
      
      this._secWindowId = prefix+'secWindow';
      this._secMaxId = prefix+'secMax';
      this._secExitId = prefix+'secExit';
      this._secDrag = prefix+'secDrag';
      
      this._explorerTab = prefix+'explorerTab';
      this._loggerTab = prefix+'loggerTab';
      this._tracerTab = prefix+'tracerTab';
      
      this._explorerContent = prefix+'explorerContent';
      this._loggerContent = prefix+'loggerContent';
      this._tracerContent = prefix+'tracerContent';
      
      this._mainWindow = null;
      this._secWindow = null;
      
      this._dragEvent = null;
      this._clientX = null;
      this._clientY = null;
      
      this._beaconId = null;
      
      // regex to define allowed and disallowed weaving paths (mostly to avoid infinite recursion
      // FIXME don't completely disallow classes, just the ones that cause infinite recursion
      this._classRE = /^(?:(?!(Mojo.log\..*)|(Mojo.aspect)|(Mojo.Iter)|(Mojo.Util)|(Mojo.Meta)|(com\.terraframe\.mojo\.((MetaClass)|(Method)|(Constant)|(Base)|(inspector\.)))).)*$/;
      this._methodRE = /^(?!toString).*$/;
      
      this._explorer = new com.runwaysdk.inspector.Explorer(this, this._explorerTab, this._explorerContent);
      this._logger = new com.runwaysdk.inspector.Logger(this, this._loggerTab, this._loggerContent);
      this._tracer = new com.runwaysdk.inspector.Tracer(this, this._tracerTab, this._tracerContent, this._logger);
      
      this._currentContent = this._explorer;
      
      this._rendered = false;
      
      // log window onError
      var oldOnError = window.onerror;
      var that = this;
      window.onerror = function(msg, url, line) {
        oldOnError.apply(window, arguments);
        that._logger.logError({ fileName: url, lineNumber: line, message: msg });
      };
    },
    
    getLogger : function() {
      return this._logger;
    },
    
    getTracer : function() {
      return this._tracer;
    },
    
    isRendered : function()
    {
      return this._rendered;
    },
    
    addNewClass : function(inspector, args, returnObj, klass, method)
    {
      var className = returnObj.getMetaClass().getQualifiedName();
      var r = '^'+className.replace('.', '\\.')+'$';
      var re = new RegExp(r);
      var methodRE = /^(?!toString).*$/;
      
      inspector.addAroundAdvice(re);
      inspector._explorer.render();
    },
    
    addAroundAdvice : function(classRE)
    {
            
      var aroundWrapper = Mojo.Util.curry(this.aroundWrapper, this);
      var around = new Mojo.aspect.AroundAdvice(classRE, this._methodRE, aroundWrapper);
      around.weave();
    },
    
    aroundWrapper : function(mainWin, args, proceed, klass, method)
    {
    
      var id = mainWin._tracer.beforeTrace(this, args, klass, method);
    
      try
      {
        var obj = proceed.apply(this, (args));
      }
      catch(e)
      {
        mainWin._logger.logError(e, id);
        
        throw e;
      }
      
      mainWin._tracer.afterTrace(this, obj, klass, method);
      
      return obj;
    
    },
    
    render : function()
    {
      this._buildHTML();
      this._hookEvents();
      
      this._explorer.render();
      this._logger.render();
      this._tracer.render();
      
      /*
      this.addAroundAdvice(this._classRE);

      // Capture new classes
      var addNew = Mojo.Util.curry(this.addNewClass, this);
      var capture = new Mojo.aspect.AfterAdvice(/Mojo\.Meta/, /newClass/, addNew, Mojo.aspect.Advice.MATCH_STATIC);
      capture.weave();      
      */
      
      this._rendered = true;
    },
    
    _buildHTML : function()
    {
      var tStyle = 'display: inline; list-style-type: none; width: 50px; height: 30px; cursor: pointer; border: 1px solid black; background-color: #eee; padding: 4px;';
      var cStyle = 'padding: 5px 0px 5px 5px;';
    
      var html = '';
      html += '<div style="border: 1px solid black; position: absolute; width: 800px; height: 600px; background-color: white">';
      html += '  <div id="'+this._mainDrag+'" style="cursor: move; height: 30px; width: 100%; border-bottom: 1px solid black;">';
      html += '    <div id="'+this._mainDrag+'_label" style="float: left; margin: 3px 3px 3px 5px">JS Inspector</div>';
      html += '    <div id="'+this._mainExitId+'" style="cursor: pointer; float: right; width: 20px; height: 20px; border: 1px solid black; text-align: center; margin: 3px 3px">x</div>';
      html += '    <div id="'+this._mainMinId+'" style="cursor: pointer; float: right; width: 20px; height: 20px; border: 1px solid black; text-align: center; margin: 3px 3px">-</div>';
      html += '  </div>';
      html += '  <div stle="height: 100%; width: 100%">';
      html += '    <ul style="margin: 15px 0px 4px 0px; padding-left: 5px">';
      html += '      <li id="'+this._explorerTab+'" style="'+tStyle+' background-color: #00ffff;">Explorer</li>';
      html += '      <li id="'+this._loggerTab+'" style="'+tStyle+'">Logger</li>';
      html += '      <li id="'+this._tracerTab+'" style="'+tStyle+'">Tracer</li>';
      html += '    </ul>';
      html += '    <div style="border-top: 1px solid black">';
      html += '      <div id="'+this._explorerContent+'" style="display: block; '+cStyle+'">';
      html += '      </div>';
      html += '      <div id="'+this._loggerContent+'" style="display: none; '+cStyle+'">';
      html += '      </div>';
      html += '      <div id="'+this._tracerContent+'" style="display: none; '+cStyle+'">';
      html += '      </div>';
      html += '    </div>';
      html += '  </div>';
      html += '</div>';
      
      this._mainWindow = document.createElement('div');
      this._mainWindow.style.position = 'absolute';
      this._mainWindow.style.display = 'none';
      this._mainWindow.style.left = '10px';
      this._mainWindow.style.top = '10px';
      this._mainWindow.style.zIndex = '999999999';
      this._mainWindow.style.fontFamily = 'Arial';
      this._mainWindow.style.fontSize = '10pt';
      this._mainWindow.id = this._mainWindowId;
      this._mainWindow.innerHTML = html;
      
      html = '';
      html += '<div id="'+this._secDrag+'" style="cursor: move; height: 30px; width: 150px; border: 1px solid black; background-color: white">';
      html += '  <div id="'+this._secDrag+'_label" style="float: left; margin: 3px 3px 3px 5px">JS Inspector</div>';
      html += '  <div id="'+this._secExitId+'" style="cursor: pointer; float: right; width: 20px; height: 20px; border: 1px solid black; text-align: center; margin: 3px 3px">x</div>';
      html += '  <div id="'+this._secMaxId+'" style="cursor: pointer; float: right; width: 20px; height: 20px; border: 1px solid black; text-align: center; margin: 3px 3px">+</div>';
      html += '</div>';
      
      this._secWindow = document.createElement('div');
      this._secWindow.style.position = 'absolute';
      this._secWindow.style.display = 'block';
      this._secWindow.style.left = '10px';
      this._secWindow.style.top = '10px';
      this._secWindow.id = this._secWindowId;
      this._secWindow.style.zIndex = '999999999';
      this._secWindow.innerHTML = html;
      
      var body = document.getElementsByTagName('body')[0];
      body.appendChild(this._mainWindow);
      body.appendChild(this._secWindow);
    },
    
    startBeacon : function()
    {
      if(this._beaconId == null)
      {
        this._beaconId = setInterval((function(mainWin, secWin) {

          var mainWinEl = document.getElementById(mainWin);
          if (mainWinEl == null || mainWinEl == undefined) { return; }
          
          var on = false;

          return function(){
         
            var secWin = document.getElementById(secWin);
            if (secWin == null) { return; }
            
            if(on)
            {
              mainWinEl.style.backgroundColor = 'white';
              secWin.style.backgroundColor = 'white';
            }
            else
            {
              mainWinEl.style.backgroundColor = 'red';
              secWin.style.backgroundColor = 'red';
            }

            on = !on;          
          };          
          
        })(this._mainDrag, this._secDrag), 500);
      }
    },
    
    stopBeacon : function()
    {
      if(this._beaconId != null)
      {
        clearInterval(this._beaconId);
        document.getElementById(this._mainDrag).style.backgroundColor = 'white';
        document.getElementById(this._secDrag).style.backgroundColor = 'white';
        
        this._beaconId = null;
      }
    },
    
    _hookEvents : function()
    {
      function alertClose()
      {
        //alert("This cannot be closed, only minimized or maximized.\n This is why it's super ultra mega alpha edition.");
        this.destroy();
      }
    
      var manager = com.runwaysdk.inspector.EventManager.getInstance();
      var IEvent = com.runwaysdk.inspector.IEvent;

      manager.addEvent(new IEvent(this._mainWindowId, 'click', this._delegateClick, this));
      
      // main window exit/minimize
      manager.addEvent(new IEvent(this._mainMinId, 'click', this.hide, this));
      manager.addEvent(new IEvent(this._mainExitId, 'click', alertClose, this));
      
      // secondary window exit/maximize
      manager.addEvent(new IEvent(this._secMaxId, 'click', this.show, this));
      manager.addEvent(new IEvent(this._secExitId, 'click', alertClose, this));
      
      // dragging
      manager.addEvent(new IEvent(this._mainDrag, 'mousedown', this.startDrag, this));
      manager.addEvent(new IEvent(this._secDrag, 'mousedown', this.startDrag, this));
      manager.addEvent(new IEvent(this._mainDrag, 'mouseup', this.endDrag, this));
      manager.addEvent(new IEvent(this._secDrag, 'mouseup', this.endDrag, this));
      
      // tabbing
      manager.addEvent(new IEvent(this._explorerTab, 'click', this.doTab, this, this._explorer));
      manager.addEvent(new IEvent(this._loggerTab, 'click', this.doTab, this, this._logger));
      manager.addEvent(new IEvent(this._tracerTab, 'click', this.doTab, this, this._tracer));
      
      // beacon stop
      manager.addEvent(new IEvent(this._mainWindow, 'click', this.stopBeacon, this));
      manager.addEvent(new IEvent(this._secWindow, 'click', this.stopBeacon, this));
    },
    
    doTab : function(e, content)
    {
      if(this._currentContent === content)
      {
        return;
      }
      
      this._currentContent.hide();
      content.show();

      if(!content.rendered())
      {
        content.render();
      }
      
      document.getElementById(this._explorerTab).style.backgroundColor = '#eee';
      document.getElementById(this._loggerTab).style.backgroundColor = '#eee';
      document.getElementById(this._tracerTab).style.backgroundColor = '#eee';
      
      content.getTab().style.backgroundColor = '#00ffff';
      
      this._currentContent = content;
    },
    
    isDragging : function()
    {
      return this._dragEvent != null;
    },
    
    startDrag : function(e)
    {
      var el = e.target;
      
      var win;
      if(el.id === this._mainDrag || el.id === this._mainDrag+'_label')
      {
        win = this._mainWindow;
      }
      else if(el.id === this._secDrag || el.id === this._secDrag+'_label')
      {
        win = this._secWindow;
      }
      else
      {
        return;
      }
      
      this._dragEvent = 
        new com.runwaysdk.inspector.IEvent(window, 'mousemove', this.doDrag, this, win);

      var manager = com.runwaysdk.inspector.EventManager.getInstance();
      manager.addEvent(this._dragEvent);
    },
    
    endDrag : function(e)
    {
      if(this._dragEvent != null)
      {
        var manager = com.runwaysdk.inspector.EventManager.getInstance();
        manager.removeEvent(this._dragEvent);
      
        this._dragEvent = null;
        this._clientX = null;
        this._clientY = null;
      }
    },
    
    doDrag : function(e, win)
    {
      if(this._clientX != null && this._clientY != null)
      {
        var dX = this._clientX - e.clientX;
        var dY = this._clientY - e.clientY;
        
        var wX = parseInt(win.style.left);
        var wY = parseInt(win.style.top);
        
        var nX = (wX - dX) + 'px';
        var nY = (wY - dY) + 'px';
        
        this._secWindow.style.left = nX;
        this._secWindow.style.top = nY;

        this._mainWindow.style.left = nX;
        this._mainWindow.style.top = nY;
      }
      
      this._clientX = e.clientX;
      this._clientY = e.clientY;
    },
    
    show : function()
    {
      this._secWindow.style.display = 'none';
      this._mainWindow.style.display = 'block';
    },
    
    hide : function()
    {
      this._secWindow.style.display = 'block';
      this._mainWindow.style.display = 'none';
    },
    
    destroy : function()
    {
      var manager = com.runwaysdk.inspector.EventManager.getInstance();
      manager.removeAll();
      
      this._mainWindow.parentNode.removeChild(this._mainWindow);
      this._secWindow.parentNode.removeChild(this._secWindow);
      
      this._mainWindow = null;
      this._secWindow = null;
      
      this._logger.stopRowBuffer();
      this._tracer.stopRowBuffer();
      
      //this._explorer = null;
      //this._logger = null;
      //this._tracer = null;
      
      this._rendered = false;
    },
    
    _delegateClick : function(e)
    {
      var el = e.target;
      if(el.nodeName === 'A' && el.name && el.name.match(/\w+:/))
      {
        var pieces = el.name.split(':');
        var action = pieces[0];
        var data = pieces[1];
        
        if(action === 'viewClass')
        {
          this.doTab(null, this._explorer);
          this._explorer.getClassDefinition(data);
        }
        else if(action === 'viewMethod')
        {
          this.doTab(null, this._explorer);
          this._explorer.getMethodDefinition(data);
        }
        else if(action === 'viewTracer')
        {
          this.doTab(null, this._tracer);
        }
        else if(action === 'viewHierarchy')
        {
          this.doTab(null, this._explorer);
          this._explorer.getHierarchy();
        }
      }
    }
  
  },
  
  Static : {
  
    launch : function()
    {
      var inspector = Mojo.$.com.runwaysdk.inspector.Inspector.getInstance();
      inspector.isRendered() ? inspector.show() : inspector.render();
    },
    
    getLogger : function() {
      var inspector = Mojo.$.com.runwaysdk.inspector.Inspector.getInstance();
      return inspector.getLogger();
    },
    
    getTracer : function() {
      var inspector = Mojo.$.com.runwaysdk.inspector.Inspector.getInstance();
      return inspector.getTracer();
    }
    
  }

});


Mojo.Meta.newClass('com.runwaysdk.inspector.Content', {

  IsAbstract : true,

  Instance : {
  
    initialize : function(mainWin, tabId, contentId)
    {
      this._mainWin = mainWin;
      this._tabId = tabId;
      this._contentId = contentId;
      this._rendered = false;
    },
    
    getTab : function()
    {
      return document.getElementById(this._tabId);
    },
    
    getEl : function()
    {
      return document.getElementById(this._contentId);
    },
    
    newEl : function(type)
    {
      return document.createElement(type);
    },
    
    rendered : function()
    {
      return this._rendered;
    },
    
    show : function()
    {
      this.getEl().style.display = 'block';
    },
    
    hide : function()
    {
      this.getEl().style.display = 'none';
    },
    
    // FIXME make abstract
    render : function()
    {
      this._render();
      
      this._rendered = true;
    }
  },
    
  Static : {
  
    makeA : function(content, name, useDefault, href)
    {
      var p = useDefault ? 'default' : 'pointer';
      var h = 'onmouseover="this.style.color=\'#0000ff\'" onmouseout="this.style.color=\'#000000\'"';
      return '<a href="'+(href != null ? href : '#')+'" name="'+name+'" style="color: black; text-decoration: none; cursor: '+
        p+';" '+(useDefault ? '' : h)+'>'+content+'</a>'; 
    },
    
    viewClassAction : function(klass, content)
    {
      var className = klass.getMetaClass().getQualifiedName();
      return com.runwaysdk.inspector.Content.makeA((content ? content : className), 'viewClass:'+className);
    },
  
    viewMethodAction : function(klass, method)
    {
      var m = method.getName();
      return com.runwaysdk.inspector.Content.makeA(m, 'viewMethod:'+klass.getMetaClass().getQualifiedName() + '.' + m); 
    },
    
    viewTracerAction : function(id)
    {
      return com.runwaysdk.inspector.Content.makeA('GOTO', 'viewTracer:'+id, false, '#'+id);
    },
    
    viewHierarchyAction : function()
    {
      return com.runwaysdk.inspector.Content.makeA('Hierarchy', 'viewHierarchy:');
    }
    
  }
  
});

Mojo.Meta.newClass('com.runwaysdk.inspector.Explorer', {

  Extends : com.runwaysdk.inspector.Content,
  
  Instance : {
  
    initialize : function(mainWin, tabId, contentId)
    {
      this.$initialize(mainWin, tabId, contentId);
      
      var prefix = this.getMetaClass().getQualifiedName()+'_';
      this._classList = prefix+'classList';
      this._definition = prefix+'definition';
      this._method = prefix+'method';
    },
    
    _render : function()
    {
      var el = this.getEl();
    
      // display all classes in scrollable div
      var classes = Mojo.Meta.getClasses();
      classes.sort(function(c1, c2){
        if(c1 === c2)
        {
          return 0;
        }
        else if(c1 < c2)
        {
          return -1;
        }
        else
        {
          return 1;
        }
      });
      
      var classLis = '';
      var pcks = {};
      
      for(var i=0; i<classes.length; i++)
      {
        var className = classes[i];
        var klass = Mojo.Meta.findClass(className);
        
        var pckName = klass.getMetaClass().getPackage();
        if(pckName !== '' && !pcks[pckName])
        {
          var a = this.constructor.makeA(pckName, pckName, true);
          classLis += '<li style="list-style-type: none; font-weight: bold">'+a+'</li>';
        
          pcks[pckName] = {};
        }
        
        var a = this.constructor.viewClassAction(klass);
        classLis += '<li style="list-style-type: none;">'+a+'</li>';
      }
      
      var pckNames = Mojo.Util.getKeys(pcks);
      var pckLis = '';
      for(var i=0; i<pckNames.length; i++)
      {
        var pckName = pckNames[i];
      
        var a = this.constructor.makeA(pckName, '', false, '#'+pckName);
        pckLis += '<li style="list-style-type: none;">'+a+'</li>';
      }
      
      var html = '';
      html += '<div style="float: left; width: 250px; height: 527px; overflow: scroll;">';
      html += '  '+this.constructor.viewHierarchyAction()+'<hr />';
      html += '  Packages';
      html += '  <ul style="padding: 5px; margin-top: 0px;">';
      html += pckLis;
      html += '  </ul><hr />';
      html += '  Classes';
      html += '  <ul id="'+this._classList+'" style="padding: 5px; margin-top: 0px">';
      html += classLis;
      html += '  </ul>';
      html += '</div>';
      html += '<div id="'+this._definition+'" style="float: left; width: 545px; height: 527px; overflow: scroll;">';
      html += '</div>';
      html += '<div id="'+this._method+'" style="float: left; width: 540px; height: 527px; overflow: scroll; display: none">';
      html += '</div>';
      
      el.innerHTML = html;
      
      this.getHierarchy();
    },
    
    getHierarchy : function()
    {
      var root = Mojo.$.com.runwaysdk.Base;
      that = this;

      function getChildren(parent)
      {
        
      
        var node = '<li style="list-style-type: none">';
        node += '|--'+that.constructor.viewClassAction(parent);
        node += '<ul style="margin:0px; padding-left:1em">';
  
        var subs = parent.getMetaClass().getSubClasses();
        subs.sort(function(c1, c2){
          var n1 = c1.getMetaClass().getQualifiedName(), n2 = c2.getMetaClass().getQualifiedName();
          if(n1 === n2)
          {
            return 0;
          }
          else if(n1 < n2)
          {
            return -1;
          }
          else
          {
            return 1;
          }
        });
    
        Mojo.Iter.forEach(subs, function(sub){
          node += getChildren(sub);
        });
        
        node += '</ul>';
        
        return node;
      }

      var nodeLi = getChildren(root);
      
      var html = '';
      html += '<div style="margin: 10px">';
      html += '<div style="font-weight: bold;">Hierarchy ['+Mojo.Meta.classCount()+' classes]</div><hr />';
      html += '<ul style="margin:0px; padding:0px">'+nodeLi+'</ul>';
      html += '</div>';

      var el = document.getElementById(this._definition);
      el.innerHTML = html;
    },
    
    getMethodDefinition : function(qualifiedMethod)
    {
      var ind = qualifiedMethod.lastIndexOf('.');
      var className = qualifiedMethod.substring(0, ind);
      var methodName = qualifiedMethod.substring(ind+1);
      
      var klass = Mojo.Meta.findClass(className);
      var method = klass.getMetaClass().getMethod(methodName);
      
      var back = this.constructor.viewClassAction(klass, '&#8656;');
      
      var body = method.getMethod().toString();
      body = body.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;");
      
      var highlighter = new Mojo.$.com.runwaysdk.inspector.SyntaxHighlighter();
      var src = highlighter.parse(body);
      
      var html = '';
      html += '<div style="margin: 10px">';
      html += '<div style="font-weight: bold;">'+back + ' ' + qualifiedMethod+'</div><hr />';
      html += '<pre>';
      html += src;
      html += '</pre>';
      
      var defPane = document.getElementById(this._definition);
      var methPane = document.getElementById(this._method);

      methPane.innerHTML = html;
      
      defPane.style.display = 'none';
      methPane.style.display = 'block';
    },
    
    getClassDefinition : function(qualifiedName)
    {
    
      var klass = Mojo.Meta.findClass(qualifiedName);
      var meta = klass.getMetaClass();
    
      var html = '';
      html += '<div style="margin: 10px">';
      html += '<div style="font-weight: bold;">'+meta.toString()+'</div><hr />';
      
      var extendsName;
      if(meta.getSuperClass() !== Object)
      {
        extendsName = this.constructor.viewClassAction(meta.getSuperClass());
      }
      else
      {
        extendsName = 'Object [JS Native]';
      }
      
      var tAttrs = 'cellpadding="3" cellspacing="0" border="1" style="font-size: 10pt; margin-bottom: 15px; white-space: nowrap; border-collapse: collapse"';
      var cellStyle = 'style="padding: 3px;"';
      
      var pckName = meta.getPackage();
      if(pckName === '')
      {
        pckName = '&nbsp;';
      }
      
      var subclasses = meta.getSubClasses();
      var sublinks = [];
      for(var i=0; i<subclasses.length; i++)
      {
        var a = this.constructor.viewClassAction(subclasses[i]);
        sublinks.push(a);
      }
      
      var isIF = meta.isInterface();
      
      // definition
      table = new com.runwaysdk.inspector.Table();
      table.setHeaders('Property', 'Value');
      table.addRow(['Package', pckName]);
      table.addRow([(isIF ? 'Interface Name' : 'Class Name'), meta.getName()]);
      table.addRow(['Abstract', meta.isAbstract()]);
      table.addRow(['Singleton', meta.isSingleton()]);

      if(!isIF)
      {
        var IFs = meta.getInterfaces();
        var IFlinks = [];
        for(var i=0; i<IFs.length; i++)
        {
          var a = this.constructor.viewClassAction(IFs[i]);
          IFlinks.push(a);
        }
        
        table.addRow(['Implements', (IFlinks.length > 0 ? IFlinks.join('<br />') : '&nbsp;')]);
      }

      table.addRow(['Extends', extendsName]);
      table.addRow([(isIF ? 'Sub-Interfaces' : 'Sub-Classes'), 
                    (sublinks.length > 0 ? sublinks.join('<br />') : '&nbsp;')]);
      
      html += 'Class Definition:<br />';
      html += table.getHTML();
      
      // constants
      table = new com.runwaysdk.inspector.Table();
      table.setHeaders('Name', 'Value', 'Defined On');
      
      
      var constants = meta.getConstants();
      constants.sort(function(c1, c2){
        var n1 = c1.getName(), n2 = c2.getName();
        if(n1 === n2)
        {
          return 0;
        }
        else if(n1 < n2)
        {
          return -1;
        }
        else
        {
          return 1;
        }
      });
      
      for(var i=0; i<constants.length; i++)
      {
        var constObj = constants[i];
        var cClass = constObj.getDefiningClass();
        
        var defining;
        if(cClass === klass)
        {
          defining = '[this class]';
        }
        else
        {
          defining = this.constructor.viewClassAction(cClass);
        }
        
        var value = constObj.getValue();
        var cValue = null;
        if(Mojo.Util.isFunction(value) || Mojo.Util.isObject(value) || Mojo.Util.isArray(value))
        {
          // FIXME link to source if a function
          cValue = typeof value;
        }
        else
        {
          cValue = value;
        }
        
        table.addRow(constObj.getName(), cValue, defining);
      }
      
      html += 'Constants:<br />';
      html += table.getHTML();
      
      // converts an array of methods into ordered rows
      function methodsToRows(table, methods, that, isStatic)
      {
        methods.sort(function(m1, m2){
          var n1 = m1.getName(), n2 = m2.getName();
          if(n1 === n2)
          {
            return 0;
          }
          else if(n1 < n2)
          {
            return -1;
          }
          else
          {
            return 1;
          }
        });
      
        Mojo.Iter.forEach(methods, function(method){
        
          var mClass = method.getDefiningClass();
          var defining = mClass === klass ?
            '[this class]' : this.constructor.viewClassAction(mClass);
          
          var nameA = this.constructor.viewMethodAction(klass, method);
          
          if(method.isConstructor())
          {
            nameA = '<span style="text-decoration: underline">'+nameA+'</span>';
          }
          
          var override = method.isStatic() ? method.isHiding() : method.isOverride();
          
          if(isStatic)
          {
            table.addRow(nameA, override, method.getArity(), defining);
          }
          else
          {
            table.addRow(nameA, method.isAbstract(), override, method.getArity(), defining);
          }
        }, that);
      }
      
      // instance methods
      table = new com.runwaysdk.inspector.Table();
      table.setHeaders('Name', 'Abstract', 'Override', 'Arity', 'Defined On');
      
      methodsToRows(table, meta.getInstanceMethods(), this, false);
      
      html += 'Instance Methods:<br />';
      html += table.getHTML();
            
      
      // static methods
      table = new com.runwaysdk.inspector.Table();
      table.setHeaders('Name', 'Hiding', 'Arity', 'Defined On');
      
      methodsToRows(table, meta.getStaticMethods(), this, true);
      
      html += 'Static Methods:<br />';
      html += table.getHTML();
      
      html += '</div>';
      
      var defPane = document.getElementById(this._definition);
      defPane.innerHTML = html;
      
      document.getElementById(this._method).style.display = 'none';
      defPane.style.display = 'block';
    }
  }
});

Mojo.Meta.newClass('com.runwaysdk.inspector.LoggerImpl', {

  Instance : {
  
    initialize : function(loggerContent)
    {
      this._content = loggerContent;
    },
    
    writeInfo : function(msg)
    {
     // FIXME make abstract
    },
    
    writeWarning : function(msg)
    {
     // FIXME make abstract
    },
    
    writeError : function(msg, error)
    {
      this._content.logError(error);
    },
  
  }

});

Mojo.Meta.newClass('com.runwaysdk.inspector.Logger', {

  Extends : com.runwaysdk.inspector.Content,
  
  Constants : {
  
    MAX_ROWS : 100,
  },
  
  Instance : {
  
    initialize : function(mainWin, tabId, contentId)
    {
      this.$initialize(mainWin, tabId, contentId);
      
      //var logger = new com.runwaysdk.inspector.LoggerImpl(this);
      //Mojo.log.LogManager.addLogger(logger);
      
      this._selectId = this.getMetaClass().getQualifiedName()+'_select';
      this._logTable = this.getMetaClass().getQualifiedName()+'_logTable';
      this._table = null;
      this._errorBuffer = [];
      this._loggedInfos = [];
      this._loggedWarnings = [];
      this._loggedErrors = [];
      this._logLevel = 0; // 0 = trace, 5 = fatal
      
      this._table = new com.runwaysdk.inspector.Table(this._logTable, this.constructor.MAX_ROWS);
      this._table.setHeaders('#', 'Type', 'Time', 'Tracer', 'Message', 'Stack', 'File', 'Line');
    },
    
    stopRowBuffer : function()
    {
      this._table.stopRowBuffer();
    },
    
    _render : function()
    {
      var el = this.getEl();
      
      var html = '';
      
      html += 'Log Display Level: ';
      html += '<select id="' + this._selectId + '">' +
        "<option>trace</option>" +
        "<option>debug</option>" +
        "<option>info</option>" +
        "<option>warning</option>" +
        "<option>error</option>" +
        "<option>fatal</option>" +
        "</select>";
      
      html += '<div style="overflow: scroll; height: 527px;">';
      
      // definition
      html += 'Last ' + this.constructor.MAX_ROWS + ' Log Entries:<br />';
      html  += '<div>';
      html += this._table.getHTML();
      html += '</div>'; 
      html += '</div>';
      
      el.innerHTML = html;
      
      this._table.startRowBuffer();
      
      var manager = com.runwaysdk.inspector.EventManager.getInstance();
      var IEvent = com.runwaysdk.inspector.IEvent;
      manager.addEvent(new IEvent(this._selectId, 'change', this.onSelectChange, this));
    },
    
    onSelectChange : function(e) {
      var select = document.getElementById(this._selectId);
      this._logLevel = select.selectedIndex;
      
      this._table.clearAllRows();
      
      if (this._logLevel <= 2) {
        for (var key in this._loggedInfos) {
          this._table.insertRow(this._loggedInfos[key].slice(0), null);
        }
      }
      if (this._logLevel <= 3) {
        for (var key in this._loggedWarnings) {
          this._table.insertRow(this._loggedWarnings[key].slice(0), null);
        } 
      }
      if (this._logLevel <= 4) {
        for (var key in this._loggedErrors) {
          this._table.insertRow(this._loggedErrors[key].slice(0), null);
        }
      }
      
      this._table.clearBuffer(true);
    },
    
    logInfo : function(msg)
    {
      var rowData = ['<span style="color: black; font-weight: bold">Info</span>',
        Mojo.Util.toISO8601(new Date()), '', '<pre>' + msg + '</pre>', '', '', ''];
      this._loggedInfos.push(rowData);
      
      if (this._logLevel <= 2) {
        this._table.insertRow(rowData.slice(0), null);
      }
    },
    
    logWarning : function(msg)
    {
      var rowData = ['<span style="color: yellow; font-weight: bold">Warning</span>',
        Mojo.Util.toISO8601(new Date()), '', '<pre>' + msg + '</pre>', '', '', ''];
      this._loggedWarnings.push(rowData);
      
      if (this._logLevel <= 3) {
        this._table.insertRow(rowData.slice(0), null);
      }
    },

    logError : function(error, id)
    {
      // FIXME output IE compatible message
      var traceA;
      if(id)
      {
        var traceA = this.constructor.viewTracerAction(id);
        this._errorBuffer.push(id);
      }
      else
      {
        traceA = '';
      }
    
      var rowData = ['<span style="color: red; font-weight: bold">Error</span>',
        Mojo.Util.toISO8601(new Date()), traceA, '<pre>'+ error.message + '</pre>', '<pre>'+error.stack+'</pre>', error.fileName, error.lineNumber];
      this._loggedErrors.push(rowData);
      
      if (this._logLevel <= 4) {
        this._table.insertRow(rowData.slice(0), id);
        this._mainWin.startBeacon();
      }
    },
    
    purgeErrorBuffer : function()
    {
      var refCopy = this._errorBuffer;
      this._errorBuffer = [];
      return refCopy;
    }
  }
  
});

Mojo.Meta.newClass('com.runwaysdk.inspector.Tracer', {

  Extends : com.runwaysdk.inspector.Content,

  Constants : {
  
    MAX_ROWS : 500,
  },

  Instance : {
  
    initialize : function(mainWin, tabId, contentId, logger)
    {
      this.$initialize(mainWin, tabId, contentId);
      
      this._logger = logger;
      this._tracerTable = this.getMetaClass().getQualifiedName()+'_tracerTable';
      this._table = null;
    },
    
    stopRowBuffer : function()
    {
      this._table.stopRowBuffer();
    },
    
    _render : function()
    {
      var el = this.getEl();
      
      var html = '';
      html += '<div style="overflow: scroll; height: 527px;">';
      
      
      this._table = new com.runwaysdk.inspector.Table(this._tracerTable, this.constructor.MAX_ROWS);
      this._table.setHeaders('#', 'Time', 'Call', 'Class Name', 'Method', 'Context', 'Parameters', 'Return');
      
      // definition
      html += 'Last 500 Traces:<br />';
      html += '<div>';
      html += this._table.getHTML();
      html += '</div>'
      html += '</div>';
      
      el.innerHTML = html;
      
      this._table.startRowBuffer();
    },
    
    beforeTrace : function(context, args, klass, method)
    {
      var id = new String(Math.random()).substring(2);
            
      // don't call toString on *this* if the method is the
      // initialize constructor because it may not have been fully constructed.
      var toStr = !method.isStatic() && method.isConstructor() ?
        Mojo.$.com.runwaysdk.Base.prototype.toString.call(context) : context.toString();
    
      var traceA = this.constructor.makeA('BEFORE', id, true);
    
      var argCell = '';
      if(args.length === 0)
      {
        argCell = '&nbsp;'
      }
      else
      {
        
        for(var i=0; i<args.length; i++)
        {
          argCell += '<span style="font-weight: bold">'+i.toString()+':</span> ';
          
          var arg = args[i];
          if(arg == null)
          {
            argCell += '[null] null<br />';
          }
          else if(arg instanceof Mojo.$.com.runwaysdk.Base)
          {
            argCell += '['+this.constructor.viewClassAction(arg.constructor) + '] '+arg.toString()+'<br />';
          }
          else if(Mojo.Util.isObject(arg) || Mojo.Util.isArray(arg) || Mojo.Util.isFunction(arg))
          {
            argCell += '['+(typeof arg)+'] '+(typeof arg)+'<br />';
          }
          else
          {
            var value;
            if(Mojo.Util.isString(arg) && arg.length > 50)
            {
              value = arg.substring(0, 50) + ' ...';
            }
            else
            {
              value = arg;
            }
            
            argCell += '['+(typeof arg)+'] '+value+'<br />';
          }
        }
      }
      
      this._table.insertRow([Mojo.Util.toISO8601(new Date()), traceA, 
        this.constructor.viewClassAction(klass), this.constructor.viewMethodAction(klass, method), toStr,
        argCell, 'n/a'], id);
        
      return id;
    },
    
    afterTrace : function(context, retObj, klass, method)
    {
      // don't call toString on *this* if the method is the
      // initialize constructor because may not have been fully constructed.
      var toStr = !method.isStatic() && method.getName() === 'initialize' ?
        Mojo.$.com.runwaysdk.Base.prototype.toString.call(context) : context.toString();
    
      var retCell = '';
      if(typeof retObj === 'undefined')
      {
        retCell += '&nbsp;';
      }
      else if(retObj === null)
      {
          retCell += '[null] null<br />';
      }      
      else if(retObj instanceof Mojo.$.com.runwaysdk.Base)
      {
        retCell += '['+this.constructor.viewClassAction(retObj.constructor) + '] '+retObj.toString()+'<br />';
      }
      else if(Mojo.Util.isObject(retObj) || Mojo.Util.isArray(retObj) || Mojo.Util.isFunction(retObj))
      {
        retCell += '['+(typeof retObj)+'] '+(typeof retObj)+'<br />';
      }
      else
      {
        var value;
        if(Mojo.Util.isString(retObj) && retObj.length > 50)
        {
          value = retObj.substring(0, 50) + ' ...';
        }
        else
        {
          value = retObj;
        }
        
        retCell += '['+(typeof retObj)+'] '+value+'<br />';
      }
      
      this._table.insertRow([Mojo.Util.toISO8601(new Date()), 'AFTER', 
        this.constructor.viewClassAction(klass), this.constructor.viewMethodAction(klass, method), toStr,
        'n/a', retCell], null);
    }
    
  }
  
});

Mojo.Meta.newClass('com.runwaysdk.inspector.Table', {

  Constants : {
    
    TABLE_STYLE : 'cellpadding="3" cellspacing="0" border="1" style="margin-left: 2px; font-size: 10pt; margin-bottom: 15px; white-space: nowrap; border-collapse: collapse;"',
    CELL_STYLE : 'padding: 3px;',
    BUFFER_TIMEOUT : 1000, // milliseconds until the table is updated
  },

  Instance : {
    
    initialize : function(id, maxRows)
    {
      this._id = id || new String(Math.random()).substring(2);
      this._maxRows = maxRows || null;
      this._headers = null;
      this._rows = [];
      this._table = null;
      this._rowCount = 0;
      
      this._rowBuffer = [];
      this._intervalId = null; 
    },
    
    stopRowBuffer : function()
    {
      clearInterval(this._intervalId);
    },
    
    startRowBuffer : function()
    {
      var bound = Mojo.Util.bind(this, this.clearBuffer);
      setInterval(bound, 1000);
    },
    
    clearBuffer : function(forceHtmlFlush)
    {
      if(this._rowBuffer.length === 0 && forceHtmlFlush == null)
      {
        return;
      }
      
      // insert the last buffered rows
      // this._rowBuffer.length
      [].splice.apply(this._rows, [0,0].concat(this._rowBuffer)); // this appends to the front
      //[].push.apply(this._rows, this._rowBuffer); // this appends to the back
      this._rowBuffer = [];
      
      if(this._rows.length > this._maxRows)
      {
        this._rows = this._rows.slice(0, this._maxRows);
      }
      
      if(this._table == null)
      {
        this._table = document.getElementById(this._id);
      }
      
      var html = this.getHTML(false);
      
      if (this._table != null) {
        this._table.parentNode.innerHTML = html;
        this._table = null;
      }
    },
    
    setHeaders : function(headers)
    {
      var data = Mojo.Util.isArray(headers) ? headers : [].splice.call(arguments, 0);
      var id = id || new String(Math.random()).substring(2);
    
      this._headers = {data: data, id: id};
    },
    
    // Uses the buffer
    insertRow : function(data, id)
    {
      if(this._table == null)
      {
        this._table = document.getElementById(this._id);
      }
      
      data = Mojo.Util.isArray(data) ? data : [].splice.call(arguments, 0);
      id = id || new String(Math.random()).substring(2);
      
      // add row count since we're using the buffer
      data.splice(0, 0, this._rowCount++);
    
      var row = {data: data, id: id};
      
      this._rowBuffer.splice(0,0,row); // add the row to the beginning of the rowBuffer array
      //this._rowBuffer.push(row); // add it to the end
    },
    
    // Does not use buffer
    addRow : function(data, id)
    {
      if(this._table == null)
      {
        this._table = document.getElementById(this._id);
      }

      data = Mojo.Util.isArray(data) ? data : [].splice.call(arguments, 0);
      id = id || new String(Math.random()).substring(2);
      
      var row = {data: data, id: id};
      
      this._rows.splice(0, 0, row);
    },

    _addRow : function(isHeader, row)
    {
      var html = '';
      html += '<tr id="'+row.id+'">';
      var data = row.data;
      Mojo.Iter.forEach(data, function(cell){
        if(isHeader)
        {
          html += '<th style="'+this.constructor.CELL_STYLE+' font-weight: bold;">'+cell+'</th>';
        }
        else
        {
          html += '<td style="'+this.constructor.CELL_STYLE+'">'+cell+'</td>';
        }
      }, this);
      html += '</tr>';
      return html;
    },
    
    clearAllRows : function() {
      this._rows = [];
      this._rowBuffer = [];
    },
    
    getHTML : function(rowsOnly)
    {
      var html = '';
      
      if(!rowsOnly)
      {
        html += '<table '+this.constructor.TABLE_STYLE+' id="'+this._id+'">';
      }
      html += this._addRow(true, this._headers);
      var rowsHTML = Mojo.Iter.map(this._rows, Mojo.Util.curry(this._addRow, false), this);
      html += rowsHTML.join('');
      
      if(!rowsOnly)
      {
        html += '</table>';
      }
      
      return html;
    }
  }
  
});


Mojo.Meta.newClass('com.runwaysdk.inspector.IEvent', {

  Instance : {
  
    initialize : function(element, type, handler, context, obj)
    {
      // unique id for each Event
      this._id = new String(Math.random()).substring(2);

      this._element = Mojo.Util.isString(element)
        ? document.getElementById(element) : element;
      this._type = type;
      this._handler = handler;
      this._context = context;
      this._obj = obj || {};
      
      
      this._wrapper = Mojo.Util.bind(this, function(e){
        
        // FIXME make sure e works in IE
        this._handler.call(this._context, e, this._obj);
      });
    },
    
    getId : function()
    {
      return this._id;
    },
    
    getElement : function()
    {
      return this._element;
    },
    
    getType : function()
    {
      return this._type;
    },
    
    getWrapper : function()
    {
      return this._wrapper;
    }
  }

});

Mojo.Meta.newClass('com.runwaysdk.inspector.EventManager', {

  IsSingleton : true,

  Instance : {
  
    initialize : function()
    {
      this._eventMap = {};
    },
    
    _addListener : function(event)
    {
      var wrapper = event.getWrapper();
      var type = event.getType();
      
      var el = event.getElement();
      if (el.addEventListener)
      {
        el.addEventListener(type, wrapper, false); 
      }
      else
      {
        el.attachEvent(type, wrapper);
      }
    },
    
    _removeListener : function(event)
    {
      var wrapper = event.getWrapper();
      var type = event.getType();
      
      var el = event.getElement();
      if(el.removeEventListener)
      {
        el.removeEventListener(type, wrapper, false);
      }
      else
      {
        el.detachEvent(type, wrapper);
      }
    },
    
    addEvent : function(event)
    {
      this._addListener(event);
    
      this._eventMap[event.getId()] = event;
    },
    
    removeEvent : function(event)
    {
      this._removeListener(event);
      
      delete this._eventMap[event.getId()];
    },
    
    removeAll : function()
    {
      var ids = Mojo.Util.getKeys(this._eventMap);
      for(var i=0; i<ids.length; i++)
      {
        this._removeListener(this._eventMap[ids[i]]);
      }
      
      this._eventMap = {};
    }
  }
});


/* ====================================================================
 * License for com.runwaysdk.inspector.SyntaxHighlighter
 * ====================================================================
 * 
 * The code for the class com.runwaysdk.inspector.SyntaxHighlighter is
 * distributed under the Apache Liscense 2.0, which can be viewed at the
 * following URL: http://www.apache.org/licenses/LICENSE-2.0.txt.
 * 
 * The original source code can be found at the following URL:
 * http://www.jswidget.com/blog/download/highlight_js.js. The code
 * has been modified to be encapsulated within the class
 * com.runwaysdk.inspector.SyntaxHighlighter. Also, the dependency
 * on CSS has been removed by using inline styles.
 * 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY 
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE 
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * THIS LICENSE APPLIES ONLY TO THE com.runwaysdk.inspector.SyntaxHighlighter
 * CLASS, WHICH HAS BEEN BUNDLED INTO THIS FILE FOR CONVENIENCE.
 *
 * http:\\www.jswidget.com
 * 2008
 */
Mojo.Meta.newClass('com.runwaysdk.inspector.SyntaxHighlighter', {

  Constants : {
  
     KEYWORDS : [ "break", "case", "comment", "continue", "default", "delete", "do", "document", "else", "export", "for", "function", "if", "import", "in", "label", "new", "null", "prototype", "return", "switch", "this", "typeof", "var", "void", "while", "window", "with", "goto", "true", "false", "try", "catch", "throw", "throws", "finally", "Array", "Boolean", "Date", "Error", "Math", "Number", "RegExp", "String" ]
  
  },
  
  Instance : {

    initialize : function()
    {
   
    },
    
    parse : function(src)
    {
      var exp = new RegExp();
      var reg1 = new RegExp();
      var that = this;
   
      src = src.replace(/</g,"&lt;").replace(/>/g,"&gt;");
   
      // Processing block comment
      exp.compile("/\\*","g");
      src = src.replace(exp,
      function(str,index, s2){
          var bRet = that._withinString(s2,index);
          if ( bRet ){
             return str;
          }else{
             return "<c>" + str;
          }
      });
      
      exp.compile("\\*/","g");
      src = src.replace(exp,
      function(str,index, s2){
          var bRet = that._withinString(s2,index);
          if ( bRet ){
             return str;
          }else{
             return str + "</c>";
          }
      });
      
      //check single-line comment //
      src = src.replace(/\/\/(.*)/g,
          function(str,s,index,s2){
             var bRet = that._withinString(s2,index);
             if ( bRet ){
                return "//" + s;
             }else{
                return "<c>//" + s + "</c>";
             }
          }
      );
   
      // processing keyword and string
      var index = src.indexOf("<c>");
      if ( index != -1 ){
         var s1 = src.substr(0,index);
         var s2 = src.substring(index, src.length);
         
         s1 = this._processKeyword(reg1,s1);
         s1 = this._processString(s1);
         src = s1 + s2;
      }else{
         src = this._processKeyword(reg1,src);
         src = this._processString(src);
      }
      
      exp.compile("</c>([^<]*)<c>","g");
      
      src = src.replace(exp,
          function(strOrg,sMatch){
             sMatch = "</c>" + sMatch + "<c>";
             sMatch = that._processKeyword(reg1,sMatch);
             sMatch = that._processString(sMatch);
             return sMatch;
          });
      
      var index = src.lastIndexOf("</c>");
      if ( index != -1 ){
         var s1 = src.substr(0,index + 4);
         var s2 = src.substring(index + 4, src.length);
         
         s2 = this._processKeyword(reg1,s2);
         
         s2 = this._processString(s2);
         src = s1 + s2;
      }
      
      src = src.replace(/ /g,"&nbsp;").
                replace(/\t/g,"&nbsp;&nbsp;&nbsp;&nbsp;").
                replace(/\n/g,"<br />");
    
      src = src.replace(/<k>/g,"<span style='color: #0000FF'>").
                replace(/<\/k>/g,"</span>").
                replace(/<c>/g,"<span style='color: #008200'>").
                replace(/<\/c>/g,"</span>").
                replace(/<s>/g,"<span style='color: #A51410'>").
                replace(/<\/s>/g,"</span>");
      
      return src;
    },
   
   _withinString : function(str,index){
      var len = str.length;
      var rq = 0,rq1 = 0;
      for ( var i = index; i < len; i ++ ){
         var ch = str.charAt(i);
         if ( ch == "\""){rq ++;} 
         if ( ch == "'"){rq1 ++;} 
         if ( ch == "\n" ){break;}
      }
      
      if ( rq % 2 != 0 ){
         return true;
      }
      if ( rq1 % 2 != 0 ){
         return true;
      }
      return false;
   },
   
   _processKeyword : function(exp,sMatch){
   
     var js_keyword = this.constructor.KEYWORDS;
   
     for ( var i = 0; i < js_keyword.length; i ++ ){    
        exp.compile("\\b" + js_keyword[i] + "\\b","g");
        sMatch = sMatch.replace(exp,"<k>" + js_keyword[i] + "</k>");
     }
     return sMatch;
   },
 
   _processString : function(sMatch){
   
     var that = this;
   
     sMatch = sMatch.replace(/(.*)(\".*\")(.*)/g,
             function(sOrg,s0,s1,s2){
               s0 = that._processString(s0);
               s1 = "<s>" + s1 + "</s>";
               s2 = that._processString(s2);
               return s0 + s1 + s2;
            });
     sMatch = sMatch.replace(/(.*)(\'.*\')(.*)/g,
            function(sOrg,s0,s1,s2){
               s0 = that._processString(s0);
               s1 = "<s>" + s1 + "</s>";
               s2 = that._processString(s2);
               return s0 + s1 + s2;
            });
     return sMatch;
   }
                 
  }
});

com.runwaysdk.Exception.addEventListener(function(ex){
  //Log it
  var msg = "A new exception was instantiated: " + ex.getDeveloperMessage();
  com.runwaysdk.inspector.Inspector.getLogger().logInfo(msg);
});

})();