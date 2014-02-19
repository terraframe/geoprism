<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/mdssLib.tld" prefix="mdss"%>
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>

<header id="header">
  <h1>Login</h1>
</header>

<%@page import="com.runwaysdk.constants.DeployProperties" %>
<%
  String webappRoot = "/" + DeployProperties.getAppName() + "/";
%>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/log4js.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/errorcatch.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/Util.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ClassFramework.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/Structure.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_Core.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_DTO.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_GIS.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_Inspector.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/RunwaySDK_UI.js"></script>

<style type="text/css">
input[type="button"],input[type="submit"] {
	display: inline;
	background: F00;
	height: 39px;
	background-position: right top;
	font-size: 18px;
	font-weight: bold;
	color: #FFFFFF;
	line-height: 30px;
	text-decoration: none;
	margin-left: -13px;
	margin-top: 13px;
	text-shadow: 0 0 0 #000;
	border-width: 0px;
	width: 130px;
	padding: 0px;
	z-index: 3;
}

.submitButton:hover {
	background-image: url(<% out.print(webappRoot);%>com/runwaysdk/geodashboard/images/submitButtonBackOver.gif);
}

.submitButton {
	display: inline;
	background-image: url(<% out.print(webappRoot);%>com/runwaysdk/geodashboard/images/submitButtonBack.gif);
	background-repeat: repeat-x;
	height: 39px;
	background-position: right top;
	font-size: 18px;
	font-weight: bold;
	color: #FFFFFF;
	line-height: 60px;
	text-decoration: none;
	margin-left: -13px;
	text-shadow: 0 0 0 #000;
	margin-top: 13px;
}

input[type="button"]:hover {
	border: 0px
}

input[type="button"]:active {
	border: 0px
}

.submitButton:hover {
	background-image: url(<% out.print(webappRoot);%>com/runwaysdk/geodashboard/images/submitButtonBackOver.gif);
}

.submitButton_bl {
	width: 24px;
	background-image: url(<% out.print(webappRoot);%>com/runwaysdk/geodashboard/images/submitButtonLeft.gif);
	height: 39px;
	float: left;
	z-index: 4;
	position: relative;
	left: 11px;
	margin-top: 13px;
}
</style>

<div class="pageContent">
<div class="pageTitle"><mdss:localize key="login" /></div>


<form method="post" action="session/login" name="mform" id="mform"><c:if test="${bad_password}">
  <div class="alert alertbox">
  <p>${exception.localizedMessage}</p>
  </div>
</c:if>

<dl>
  <dt><label> <mdss:localize key="username" />: </label></dt>
  <dd><mjl:input param="username" type="text" /></dd>
  <dt><label> <mdss:localize key="password" />: </label></dt>
  <dd><mjl:input param="password" type="password" value="" /></dd>
</dl>

<div class="submitButton_bl"></div>
<input type="submit" value="<mdss:localize key="Login" />" name="LoginController" id="submitLogin" class="submitButton" />
</form>



<script type="text/javascript">

</script></div>

<script type="text/javascript" charset="utf-8">
var checkForFF = function () {
// Define private vars here.
  var downloadLink = "http://www.getfirefox.com";
  var agent = navigator.userAgent;
  var is_firefox = /Firefox.3\.(5|6)/i.test(agent) || /Shiretoko.3/i.test(agent); // is IE6??
  var overlayColor  = "#000000";  // Change these to fit your color scheme.
  var lightboxColor = "#ffffff";  // " "
  var borderColor   = "#ff0000";  // " "
// Hate to define CSS this way, but trying to keep to one file.
// I'll keep it as pretty as possible.
var overlayCSS =
 "display: block; position: absolute; top: 0%; left: 0%;" +
 "width: 100%; height: 100%; background-color: " + overlayColor + "; " +
 "z-index:1001; -moz-opacity: 0.8; opacity:.80; filter: alpha(opacity=80);";
var lightboxCSS =
 "display: block; position: absolute; top: 25%; left: 25%; width: 50%; " +
 "height: 50%; padding: 16px; border: 8px solid " + borderColor + "; " +
 "background-color:" + lightboxColor + "; " +
 "z-index:1002; overflow: auto;";
var lightboxContents =
 "<div style='width: 100%; height: 95%'>" +
   "<div style='text-align: center;'>" +
   "<div class='pageTitle'>${unsupported_browser_header}</div>" +
   "<br><br><br>" +
   "${unsupported_browser_contents}" +
   "<br><br><br>" +
   "<a style='color: #0000EE' href='" + downloadLink + "'>" + downloadLink + "</a>" +
   "</div>" +
 "</div>";
function isCookieSet() {
 if (document.cookie.length > 0) {
   var i = document.cookie.indexOf("sevenup=");
   return (i != -1);
 }
 return false;
}

return {  // Return object literal and public methods here.
  test: function(allowSkip) {
    if (! is_firefox) {
      // Write layer into the document.
      var layerHTML =
        "<div id='sevenUpOverlay' style='" + overlayCSS + "'>" +
          "<div style='" + lightboxCSS + "'>" +
           lightboxContents +
         "</div>" +
        "</div>";
     var layer = document.createElement('div');
     layer.innerHTML = layerHTML;
      document.body.appendChild(layer);
    }
  },
 setLightboxContents: function(newContents) {
   lightboxContents = newContents;
 }
};
}();

com.runwaysdk.ui.DOMFacade.execOnPageLoad(checkForFF);
</script>
