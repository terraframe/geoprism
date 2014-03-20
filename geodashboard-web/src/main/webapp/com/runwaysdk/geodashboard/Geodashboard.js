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
      initialize : function(obj, node){
        this.$initialize(obj);
        
        this._node = null;
        
        if(Mojo.Util.isString(node)) {
          this._node = $('#'+node);          
        }
        else {
          this._node = $(node);
        }
        
        this._div = $('<div></div>');
        
        var offset = this._node.offset();
        var top = offset.top;
        var left = offset.left;
        
        this._div.height(this._node.outerHeight());
        this._div.width(this._node.outerWidth());
        this._div.index(this._node.index()+100);
        this._div.attr('id', 'standby__'+this._node.attr('id')+'_'+Mojo.Util.generateId(8));
        this._div.addClass('standby-overlay');
        
        this._div.css('min-height',this._div.height());
        this._div.css('min-width',this._div.width());
        this._div.css('top',top);
        this._div.css('left',left);
        
        this.addOnSendListener(Mojo.Util.bind(this, this._showStandby));
        this.addOnCompleteListener(Mojo.Util.bind(this, this._hideStandby));
      },

      _showStandby : function(transport){
        $(document.body).append(this._div);
      },

      _hideStandby : function(transport){
        this._div.remove();
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
        return com.runwaysdk.Localize.getLanguage("com.runwaysdk.geodashboard.BlockingClientRequest")[key];
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
  

  // Alias the project's root to GDB
  Mojo.GLOBAL[ALIAS] = Mojo.$.com.runwaysdk.geodashboard;
})();
