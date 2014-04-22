<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <script type="text/javascript">
 
  // Check if this window is standalone or within an iframe.
  // A standalone window will have the parent set to itself.
  var standalone = (window.parent === window);

  var loginPath = '${pageContext.request.contextPath}' + '/login';

  if(standalone)
  {
    window.location = loginPath;
  }
  else
  {
	  
	if (!window.location.origin) {
		window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port: '');
	}
	  
    window.parent.postMessage(loginPath, window.location.origin);
  }
  </script>

</head>
<body>

</body>
</html>