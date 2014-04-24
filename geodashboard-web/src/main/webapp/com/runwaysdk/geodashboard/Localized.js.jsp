
<%@page import="org.json.JSONObject"%>
<%@page import="com.runwaysdk.geodashboard.localization.LocalizationFacadeDTO"%>
<%@page import="java.text.DecimalFormat"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="org.apache.taglibs.standard.tag.common.fmt.BundleSupport"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.*"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.AttributedCharacterIterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.runwaysdk.constants.Constants" %>
<%@page import="org.json.JSONArray"%>
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>

<!--It is very important to set the content type so that FF knows to read in these strings as UTF-8 -->
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
%>

/**
 * Constants used for localization in javascript.
 */
com.runwaysdk.Localize.addLanguages(<%=LocalizationFacadeDTO.getJSON(clientRequest)%>, true);
