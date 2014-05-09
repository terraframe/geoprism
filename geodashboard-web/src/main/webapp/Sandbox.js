
var BlockingRequest = Mojo.Meta.newClass('com.geodashboard.BlockingRequest', {

  Extends : Mojo.ClientRequest,
  
  Instance : {
    
    
    initialize : function(obj){
      this.$initialize(obj);

      this.addOnSendListener(Mojo.Util.bind(this, this._showModal));
      
      this.addOnCompleteListener(Mojo.Util.bind(this, this._hideModal));
    },
    
    _showModal : function(transport){
      console.log('show modal');
    },
    
    _hideModal : function(transport){
      console.log('hide modal');
    }
  }
  
});

var Sandbox = Mojo.Meta.newClass('Sandbox', {
  
  Instance : {
    
    initialize : function()
    {
      this._running = false;
      this._id = null;
    },
    go : function()
    {
      this._running = true;
      
      var that = this;
      
      var cr = new BlockingClientRequest({

        onSuccess: function(){
          alert("worked, but shouldn't");
        },
        onFailure : function(e){
          // do nothing
        }
      });
      
      com.runwaysdk.Facade.get(cr, 'bad-type', 'bad_id');
    },
    start : function(){
      this._id = setInterval(function(){
        console.log('.');
      }, 0.01);
    },
    stop : function()
    {
      this._running = false;
      clearInterval(this._id);
    }
    
  }
  
});