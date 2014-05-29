<%--

    Copyright (c) 2013 TerraFrame, Inc. All rights reserved.

    This file is part of Runway SDK(tm).

    Runway SDK(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Runway SDK(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>



<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.runwaysdk.constants.DeployProperties"%>
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>

<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO" %>
<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardMap" %>

<%
  String webappRoot = request.getContextPath() + "/";
%>

<jsp:include page="/WEB-INF/templates/headImports.jsp"></jsp:include>

<script src="https://maps.google.com/maps/api/js?v=3&amp;sensor=false"></script>

<%
  ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
%>

<script type="text/javascript">
<%// use a try catch before printing out the definitions, otherwise, if an
  // error occurs here, javascript spills onto the actual page (ugly!)
  try
  {
    String js = JSONController.importTypes(clientRequest.getSessionId(), new String[] {
      DashboardMapDTO.CLASS
      }, true);
    out.print(js);
  }
  catch(Exception e)
  {
    // perform cleanup
    throw e;
  }

  /* doIt(request, out); */%>
</script>

<script type="text/javascript">

$(document).ready(function(){
  
  // Render the map
//   var map = new GDB.gis.DynamicMap('map');
  var map = new GDB.gis.DynamicMap("mapDivId", '${mapId}');
  
  map.render();
});

</script>

    <form action="#" class="control-form">
      <fieldset>
        <legend class="none">controls form</legend>
        <button class="none">save</button>
        <article class="info-box">
          <h3>Map Layers</h3>
          <div id="overlayLayerContainer" class="holder">
<!--            <div class="row-form"> -->
<!--              <input id="f03" class="check" type="checkbox" checked="checked"> -->
<!--              <label for="f03">Unit Sales by District</label> -->
<!--              <div class="cell"> -->
<!--                <a href="#" class="ico-remove">remove</a> -->
<!--                <a href="#" class="ico-edit">edit</a> -->
<!--                <a href="#" class="ico-control">control</a> -->
<!--              </div> -->
<!--            </div> -->
          </div>
        </article>
        <article class="info-box">
          <h3>Base Map</h3>
          <div id="baseLayerContainer" class="holder">
          </div>
        </article>
<!--        <ul class="scale-nav"> -->
<!--          <li><a href="#">plus</a></li> -->
<!--          <li><a href="#" class="minus">minus</a></li> -->
<!--        </ul> -->
      </fieldset>
    </form>
    <!-- contain aside of the page -->
    <aside class="aside animated slideInRight">
      <div class="nav-bar">
        <a href="#" class="opener-drop" data-toggle="tooltip" data-placement="bottom" title="Menu">opener</a>
        <div class="sales-menu dropdown">
          <a href="#" class="link-opener dropdown-toggle" id="sales-dropdown" data-toggle="dropdown">Q4 Sales Engagement</a>
          <ul class="dropdown-menu" role="menu" aria-labelledby="sales-dropdown">
            <li><a href="#">Dashboard two</a></li>
            <li><a href="#">Dashboard three</a></li>
            <li><a href="#">Dashboard four</a></li>
            <li><a href="#">Dashboard five</a></li>
          </ul>
        </div>
      </div>
        <button class="none">sabmit</button>
        <div class="choice-form">
          <div class="row-holder">
            <input type="text" autocomplete="off" id="geocode" name="geocode" class="jcf-unselectable select-choice-select select-area select-focus" style="width: 267px;" />
          </div>
          <!-- datapicker block -->
          <div class="data-block">
            <div class="col">
              <label for="from-field">From</label>
              <span class="text">
                <input class="checkin" id="from-field" type="text" placeholder="">
                <a href="#" class="datapicker-opener">datapicker</a>
              </span>
            </div>
            <div class="col">
              <label for="to-field">To</label>
              <span class="text">
                <input class="checkout" id="to-field" type="text" placeholder="">
                <a href="#" class="datapicker-opener">datapicker</a>
              </span>
            </div>
          </div>
        </div>
        <div class="sales-accortion panel-group">
          <div class="panel panel-default">
            <a class="opener" data-toggle="collapse" data-parent="#accordion" href="#collapse02">Enterprise</a>
            <!-- slide block -->
            <div id="collapse02" class="panel-collapse collapse">
              <div class="panel-body">
                
                <!-- END: Rank -->
  
                <div class="panel">
                  <h4 class="panel-title"><a class="opener-link" data-toggle="collapse" data-parent="#accordion3" href="#collapse016">Capital Investments Total Value</a> <a href="#" class="opener" data-toggle="tooltip" data-original-title="New map layer" data-placement="left" ><span data-toggle="modal" data-target="#modal01">opener</span></a></h4>
                  <!-- slide block -->
                  <div id="collapse016" class="panel-collapse collapse">
                    <div class="panel-body">
                      <div class="filter-block">
                        <div class="row-holder">
                          <label for="f31">Filter</label>
                        </div>
                        <div class="row-holder">
                          <!-- Number attribute -->
                          <div class="select-holder">
                            <select id="f31" class="filter-select">
                              <option>&gt;</option>
                              <option>&gt;=</option>
                              <option>&lt;</option>
                              <option>&lt;=</option>
                            </select>
                          </div>
                          <div class="text">
                            <label for="f301" class="none">number</label>
                            <input id="f301" type="text" placeholder="Number">
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- END: Rank -->
                
              </div>
            </div>
          </div>
        </div>
    </aside>
 
  <!-- modal -->
  <div class="modal fade" id="modal01" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="heading">
          <h1>New map layer for Number of Units</h1>
        </div>
        <form action="#" class="modal-form">
          <fieldset>
            <legend class="none"> modal form</legend>
            <button class="button none">sabmit</button>
            <div class="row-holder">
              <div class="label-holder">
                <strong>Name the layer</strong>
              </div>
              <div class="holder">
                <label class="none" for="f312">name</label>
                <span class="text"><input id="f312" type="text" placeholder=""></span>
              </div>
            </div>
            <div class="row-holder">
              <div class="label-holder style02">
                <strong>Labels and Values</strong>
              </div>
              <div class="holder">
                <div class="row-holder">
                  <div class="check-block">
                    <input id="f51" class="check" type="checkbox" checked="checked">
                    <label for="f51">Display Value</label>
                  </div>
                  <div class="cell style02">
                    <label for="f52">Font</label>
                    <div class="select-holder">
                      <select class="font-select" id="f52">
                        <option>Helvetica</option>
                        <option>Lorem ipsum</option>
                        <option>Lorem ipsum</option>
                        <option>Lorem ipsum</option>
                      </select>
                    </div>
                  </div>
                  <div class="cell">
                    <label for="f53">Size</label>
                    <div class="select-holder">
                      <select class="size-select" id="f53">
                        <option>14</option>
                        <option>15</option>
                        <option>16</option>
                        <option>17</option>
                      </select>
                    </div>
                  </div>
                  <div class="cell">
                    <span>Color</span>
                    <div class="color-holder">
                      <a href="#" class="color-choice">
                        <span class="ico">icon</span>
                        <span class="arrow">arrow</span>
                      </a>
                    </div>
                  </div>
                  <div class="cell">
                    <span>Label Halo</span>
                    <div class="color-holder">
                      <a href="#" class="color-choice">
                        <span class="ico" style="background:#fff;">icon</span>
                        <span class="arrow">arrow</span>
                      </a>
                    </div>
                  </div>
                  <div class="cell">
                    <label for="f54">Halo Width</label>
                    <div class="select-holder">
                      <select class="size-select" id="f54">
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                        <option>6</option>
                        <option>7</option>
                      </select>
                    </div>
                  </div>
                </div>
                <div class="row-holder">
                  <div class="check-block">
                    <input id="f94" class="check" type="checkbox" checked="checked">
                    <label for="f94">Display Label</label>
                  </div>
                  <div class="cell style02">
                    <label for="f55">Font</label>
                    <div class="select-holder">
                      <select class="font-select" id="f55">
                        <option>Helvetica</option>
                        <option>Lorem ipsum</option>
                        <option>Lorem ipsum</option>
                        <option>Lorem ipsum</option>
                      </select>
                    </div>
                  </div>
                  <div class="cell">
                    <label for="f95">Size</label>
                    <div class="select-holder">
                      <select class="size-select" id="f95">
                        <option>14</option>
                        <option>15</option>
                        <option>16</option>
                        <option>17</option>
                      </select>
                    </div>
                  </div>
                  <div class="cell">
                    <span>Color</span>
                    <div class="color-holder">
                      <a href="#" class="color-choice">
                        <span class="ico">icon</span>
                        <span class="arrow">arrow</span>
                      </a>
                    </div>
                  </div>
                  <div class="cell">
                    <span>Label Halo</span>
                    <div class="color-holder">
                      <a href="#" class="color-choice">
                        <span class="ico" style="background:#fff;">icon</span>
                        <span class="arrow">arrow</span>
                      </a>
                    </div>
                  </div>
                  <div class="cell">
                    <label for="f57">Halo Width</label>
                    <div class="select-holder">
                      <select class="size-select" id="f57">
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                        <option>6</option>
                        <option>7</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row-holder">
              <div class="label-holder style03">
                <strong>Define an aggregation method</strong>
              </div>
              <div class="holder add">
                <div class="box">
                  <label for="f58">Group by</label>
                  <div class="select-box">
                    <select id="f58" class="method-slect">
                      <option>District</option>
                      <option>Lorem ipsum</option>
                      <option>Lorem ipsum</option>
                      <option>Lorem ipsum</option>
                    </select>
                  </div>
                </div>
                <div class="box">
                  <label for="f59">According to</label>
                  <div class="select-box">
                    <select id="f59" class="method-slect">
                      <option>Sum</option>
                      <option>Lorem ipsum</option>
                      <option>Lorem ipsum</option>
                      <option>Lorem ipsum</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>
            <div class="row-holder">
              <div class="label-holder style04">
                <strong>Choose a layer type</strong>
              </div>
              <div class="holder style04">
                <ul class="nav-tabs type-tabs">
                  <li class="active">
                    <a href="#" data-toggle="tab">
                      <input checked id="radio1" name="radio-group" type="radio">
                      <label for="radio1">Basic</label>
                    </a>
                  </li>
                  <li class="bubble">
                    <a href="#" data-toggle="tab">
                      <input id="radio2" name="radio-group" type="radio">
                      <label for="radio2">Bubble</label>
                    </a>
                  </li>
                  <li class="gradient">
                    <a href="#" data-toggle="tab">
                      <input id="radio3" name="radio-group" type="radio">
                      <label for="radio3">Gradient</label>
                    </a>
                  </li>
                  <li class="category">
                    <a href="#" data-toggle="tab">
                      <input id="radio4" name="radio-group" type="radio">
                      <label for="radio4">Category</label>
                    </a>
                  </li>
                </ul>
              </div>
            </div>
            <div class="row-holder">
              <div class="label-holder">
                <strong>Style the layer</strong>
              </div>
              <div class="holder">
                <div class="tab-content">
                  <div class="tab-pane active" id="tab001">
                    <div class="fill-block">
                      <strong class="title">Fill</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <span>Color</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:#00a3d9;">icon</span>
                              <span class="arrow">arrow</span>
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f210">Opacity</label>
                          <span class="text"><input id="f210" type="text" placeholder="0 to 100"></span>
                        </div>
                      </div>
                    </div>
                    <div class="stroke-block">
                      <strong class="title">Stroke</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <span>Color</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:#0086b3;">icon</span>
                              <span class="arrow">arrow</span>
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f63">Width</label>
                          <div class="select-holder">
                            <select id="f63" class="tab-select">
                              <option>1</option>
                              <option>2</option>
                              <option>3</option>
                              <option>4</option>
                            </select>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f64">Opacity</label>
                          <div class="text"><input id="f64" type="text" placeholder="0 to 100"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="tab-pane active" id="tab002">
                    <div class="fill-block">
                      <strong class="title">Fill</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <span>Color</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:#00a3d9;">icon</span>
                              <span class="arrow">arrow</span>
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f71">Opacity</label>
                          <div class="text"><input id="f71" type="text" placeholder="0 to 100"></div>
                        </div>
                      </div>
                    </div>
                    <div class="stroke-block">
                      <strong class="title">Stroke</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <span>Color</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:#0086b3;">icon</span>
                              <span class="arrow">arrow</span>
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f73">Width</label>
                          <div class="select-holder">
                            <select id="f73" class="tab-select">
                              <option>1</option>
                              <option>2</option>
                              <option>3</option>
                              <option>4</option>
                            </select>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f74">Opacity</label>
                          <div class="text"><input id="f74" type="text" placeholder="0 to 100"></div>
                        </div>
                      </div>
                    </div>
                    <div class="fill-block">
                      <strong class="title">Radius</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <label for="f75">Min</label>
                          <div class="text"><input id="f75" type="text" placeholder="10"></div>
                        </div>
                        <div class="cell">
                          <label for="f76">Max</label>
                          <div class="text"><input id="f76" type="text" placeholder="100"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="tab-pane active" id="tab003">
                    <div class="fill-block">
                      <strong class="title">Fill</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <span>Color</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:#00a3d9;">icon</span>
                              <span class="arrow">arrow</span>
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f78">Opacity</label>
                          <div class="text"><input id="f78" type="text" placeholder="0 to 100"></div>
                        </div>
                      </div>
                    </div>
                    <div class="stroke-block">
                      <strong class="title">Stroke</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <span>Color</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:#0086b3;">icon</span>
                              <span class="arrow">arrow</span>
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f80">Width</label>
                          <div class="select-holder">
                            <select id="f80" class="tab-select">
                              <option>1</option>
                              <option>2</option>
                              <option>3</option>
                              <option>4</option>
                            </select>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f81">Opacity</label>
                          <div class="text"><input id="f81" type="text" placeholder="0 to 100"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="tab-pane active" id="tab004">
                    <div class="color-section">
                      <strong class="title">Fill</strong>
                      <div class="heading-list">
                        <span>Name of Ontology</span>
                        <span>Color</span>
                      </div>
                      <div class="color-block">
                        <div class="panel-group choice-color">
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color01">First Node <span class="icon-color">color</span></a>
                            <div id="choice-color01" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color02">Second Node <span class="icon-color" style="background:#d24dff;">color</span></a>
                            <div id="choice-color02" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color03">Third Node <span class="icon-color" style="background:#00a3d9;">color</span></a>
                            <div id="choice-color03" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color04">Fourth Node <span class="icon-color" style="background:#ffdb6f;">color</span></a>
                            <div id="choice-color04" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color05">Fifth Node <span class="icon-color" style="background:#ff7a4d;">color</span></a>
                            <div id="choice-color05" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color06">Sixth Node <span class="icon-color" style="background:#ffa64d;">color</span></a>
                            <div id="choice-color06" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color07">First Node <span class="icon-color">color</span></a>
                            <div id="choice-color07" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color08">Second Node <span class="icon-color" style="background:#d24dff;">color</span></a>
                            <div id="choice-color08" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color09">Third Node <span class="icon-color" style="background:#00a3d9;">color</span></a>
                            <div id="choice-color09" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color10">Fourth Node <span class="icon-color" style="background:#ffdb6f;">color</span></a>
                            <div id="choice-color10" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color11">Fifth Node <span class="icon-color" style="background:#ff7a4d;">color</span></a>
                            <div id="choice-color11" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color12">Sixth Node <span class="icon-color" style="background:#ffa64d;">color</span></a>
                            <div id="choice-color12" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="stroke-block">
                      <strong class="title">Stroke</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <span>Color</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:#0086b3;">icon</span>
                              <span class="arrow">arrow</span>
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f101">Width</label>
                          <div class="select-holder">
                            <select id="f101" class="tab-select">
                              <option>1</option>
                              <option>2</option>
                              <option>3</option>
                              <option>4</option>
                            </select>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f82">Opacity</label>
                          <div class="text"><input id="f82" type="text" placeholder="0 to 100"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row-holder">
              <div class="label-holder"></div>
              <div class="holder">
                <div class="row-holder">
                  <div class="check-block style02">
                    <input id="f65" class="check" type="checkbox" checked="checked">
                    <label for="f65">Display in Legend</label>
                  </div>
                </div>
                <div class="button-holder">
                  <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                  <button type="button" class="btn btn-primary">Map it</button>
                </div>
              </div>
            </div>
          </fieldset>
        </form>
      </div>
    </div>
  </div>
  
  
  <!-- map container -->
  <div class="bg-stretch">
    <div id="mapDivId" class="dynamicMap"></div>
  </div>
  <!-- reporting container -->
  <article class="reporticng-container">
    <div class="box"><img src="images/img02.jpg" width="250" height="250" alt="image description"></div>
    <div class="box"><img src="images/img02.jpg" width="250" height="250" alt="image description"></div>
    <div class="box"><img src="images/img02.jpg" width="250" height="250" alt="image description"></div>
    <div class="box"><img src="images/img02.jpg" width="250" height="250" alt="image description"></div>
  </article>
  <!-- allow a user to go to the top of the page -->
  <div class="skip">
    <a href="#wrapper">Back to top</a>
  </div>
</body>
</html>