/*
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
(function(){
  
  var ROOT_PACKAGE = Mojo.ROOT_PACKAGE + 'geodashboard.';
  var ALIAS = 'GDB';
  
  var Constants = Mojo.Meta.newInterface(ROOT_PACKAGE+'Constants', {
    Constants : {
      ROOT_PACKAGE: ROOT_PACKAGE,
      GIS_PACKAGE: ROOT_PACKAGE+"gis."
    }
  });
  
  Mojo.Meta.newClass(Constants.ROOT_PACKAGE+'StandbyClientRequest', {
    Extends : Mojo.ClientRequest,
    Instance : {
      /**
       * 
       * @param config A configuration object with onSuccess and onFailure handlers
       * @param blockable An element or string to be converted into an element.
       * @param parent
       */
      initialize : function(config, blockable) {
        this.$initialize(config);
        
        this._node = null;
        this._blockable = blockable;
        
        if (blockable instanceof com.runwaysdk.ui.factory.runway.Element) {
          this._node = blockable;
        }
        else {
          this._node = this.getFactory().newElement(blockable);
        }
        
        this._div = this.getFactory().newElement("div");
        
        var jqNode = $(this._node.getRawEl());
        
        var offset = jqNode.offset();
        var top = offset.top;
        var left = offset.left;
        
        var jqDiv = $(this._div.getRawEl());
        
        jqDiv.height(jqNode.outerHeight());
        jqDiv.width(jqNode.outerWidth());
        jqDiv.zIndex(jqNode.zIndex()+100);
        
        jqDiv.attr('id', 'standby__'+ this._node.getId() + '_'+Mojo.Util.generateId(8));
        
        jqDiv.addClass('standby-overlay');
        
//        jqDiv.css('min-height',jqDiv.height());
//        jqDiv.css('min-width',jqDiv.width());
        jqDiv.css('top',top);
        jqDiv.css('left',left);
        
        this.addOnSendListener(Mojo.Util.bind(this, this._showStandby));
        this.addOnCompleteListener(Mojo.Util.bind(this, this._hideStandby));
      },

      getFactory : function() {
        return com.runwaysdk.ui.Manager.getFactory();
      },
      
      _showStandby : function(transport) {
        var override = false;
        
        // FIXME : Replace this if check with an interface. F.E. Does blockable impelement BlockableIF?
        if (Mojo.Util.isFunction(this._blockable.showStandby)) {
          override = this._blockable.showStandby(this._div);
        }
        
        if (!override) {
          this._div.render();
        }
      },

      _hideStandby : function(transport) {
        var override = false;
        
        // FIXME : Replace this if check with an interface. F.E. Does blockable impelement BlockableIF?
        if (Mojo.Util.isFunction(this._blockable.hideStandby)) {
          override = this._blockable.hideStandby(this._div);
        }
        
        if (!override) {
          this._div.destroy();
        }
      }
    }
  });
  
  Mojo.Meta.newClass(Constants.ROOT_PACKAGE+'BlockingClientRequest', {
    Extends : Mojo.ClientRequest,
    Instance : {
      initialize : function(obj){
        this.$initialize(obj);
        
        this.addOnSendListener(Mojo.Util.bind(this, this._showModal));
        this.addOnCompleteListener(Mojo.Util.bind(this, this._hideModal));

        this._modal = new com.runwaysdk.ui.factory.runway.busymodal.BusyModal(this.localize("communicating"));
//        this._factory = com.runwaysdk.ui.Manager.getFactory();
//        this._dialog = this._factory.newDialog(this.localize('information'), {modal: true, showTitle: false});
//        this._dialog.appendContent(this.localize('communicating'));        
      },
      
      localize : function(key)
      {
        return com.runwaysdk.Localize.localize(this.getMetaClass().getQualifiedName(), key);
      },
      
      getFactory : function() {
        return this._factory;
      },

      _showModal : function(transport){
//        this._dialog.render();
        this._modal.render();
      },

      _hideModal : function(transport){
        this._modal.close();
//        this._dialog.close();
      }
    }
  });
  
  var ExceptionHandler = Mojo.Meta.newClass(Constants.ROOT_PACKAGE+'ExceptionHandler', {
    Static :{
      renderDialog : function(title, message) {
        var dialog = com.runwaysdk.ui.Manager.getFactory().newDialog(title, {modal: true});
        dialog.appendContent(message);
        dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){dialog.close();}, null, {class:'btn btn-primary'});
        dialog.render();                
      },
      handleException : function(e) {
        if($.type( e ) === "string") {
          ExceptionHandler.handleErrorMessage(e);
        }
        else {
          ExceptionHandler.handleErrorMessage(e.getLocalizedMessage());
        }
      },
      handleErrorMessage : function(message) {
        var title = com.runwaysdk.Localize.get("rError", "Error");
      
        ExceptionHandler.renderDialog(title, message);
      },
      handleInformation : function(information) {
        if(information != null) {
          var html = '<ul>';
        
          for(var i = 0; i < information.length; i++) {
            html += '<li>' + information[i].getMessage() +'</li>'
          }
          html += '</ul>';

          var title = com.runwaysdk.Localize.get("rInformation", "Information");
          
          ExceptionHandler.renderDialog(title, html);
        }
      }
    }
  });
  

  // Alias the project's root to GDB
  Mojo.GLOBAL[ALIAS] = Mojo.$.com.runwaysdk.geodashboard;
})();
