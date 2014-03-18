(function(){
  
  // jQuery is the default factory for geodashboard
  com.runwaysdk.ui.Manager.setFactory("JQuery");
  
  var ROOT_PACKAGE = Mojo.ROOT_PACKAGE + 'geodashboard.';
  var ALIAS = 'GDB';
  
  var Constants = Mojo.Meta.newInterface(ROOT_PACKAGE+'Constants', {
    Constants : {
      ROOT_PACKAGE: ROOT_PACKAGE,
      GIS_PACKAGE: ROOT_PACKAGE+"gis."
    }
  });
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.BlockingClientRequest', {
    Extends : Mojo.ClientRequest,
    Instance : {
      initialize : function(obj){
        this.$initialize(obj);
        
        this.addOnSendListener(Mojo.Util.bind(this, this._showModal));
        this.addOnCompleteListener(Mojo.Util.bind(this, this._hideModal));

        this._factory = com.runwaysdk.ui.Manager.getFactory();
        this._dialog = this._factory.newDialog(this.localize('information'), {modal: true});
        this._dialog.appendContent(this.localize('communicating'));        
      },
      
      localize : function(key)
      {
        return com.runwaysdk.Localize.getLanguage("com.runwaysdk.geodashboard.BlockingClientRequest").get(key);
      },
      
      getFactory : function() {
        return this._factory;
      },

      _showModal : function(transport){
        this._dialog.render();
      },

      _hideModal : function(transport){
        this._dialog.close();
      }
    }
  });  
  

  // Alias the project's root to GDB
  Mojo.GLOBAL[ALIAS] = Mojo.$.com.runwaysdk.geodashboard;
})();