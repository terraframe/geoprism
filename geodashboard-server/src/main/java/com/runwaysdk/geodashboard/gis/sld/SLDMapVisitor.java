package com.runwaysdk.geodashboard.gis.sld;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverProperties;
import com.runwaysdk.geodashboard.gis.model.FeatureStrategy;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.Map;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;
import com.runwaysdk.geodashboard.gis.model.ThematicStyle;
import com.runwaysdk.geodashboard.gis.model.condition.And;
import com.runwaysdk.geodashboard.gis.model.condition.Category;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;
import com.runwaysdk.geodashboard.gis.model.condition.Equal;
import com.runwaysdk.geodashboard.gis.model.condition.Gradient;
import com.runwaysdk.geodashboard.gis.model.condition.GreaterThan;
import com.runwaysdk.geodashboard.gis.model.condition.GreaterThanOrEqual;
import com.runwaysdk.geodashboard.gis.model.condition.IsBetween;
import com.runwaysdk.geodashboard.gis.model.condition.IsLike;
import com.runwaysdk.geodashboard.gis.model.condition.IsNull;
import com.runwaysdk.geodashboard.gis.model.condition.LessThan;
import com.runwaysdk.geodashboard.gis.model.condition.LessThanOrEqual;
import com.runwaysdk.geodashboard.gis.model.condition.Or;
import com.runwaysdk.geodashboard.gis.model.condition.Primitive;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.transport.conversion.ConversionException;

/**
 * Traverses an object graph of map Component objects and creates an SLD
 * document.
 */
public class SLDMapVisitor implements MapVisitor
{
  private static class Provider
  {
    protected SLDMapVisitor visitor;
    
    private Provider(SLDMapVisitor visitor)
    {
      this.visitor = visitor;
    }
    
    protected NodeBuilder node(String node)
    {
      return this.visitor.node(node);
    }
    
    protected NodeBuilder node(String ns, String node)
    {
      return this.visitor.node(ns, node);
    }

    protected NodeBuilder css(String name, Object value)
    {
      return this.visitor.css(name, value);
    }

    protected NodeBuilder css(String name)
    {
      return this.visitor.css(name);
    }
  }
  
  private static abstract class Symbolizer extends Provider
  {
    protected Style         style;

    private Symbolizer(SLDMapVisitor visitor, Style style)
    {
      super(visitor);
      this.style = style;
    }

    protected abstract String getSymbolizerName();

    protected Node getSLD()
    {
      return node(this.getSymbolizerName()).build();
    }
  }

  private static class PointSymbolizer extends Symbolizer
  {
    private PointSymbolizer(SLDMapVisitor visitor, Style style)
    {
      super(visitor, style);
    }

    @Override
    protected String getSymbolizerName()
    {
      return "PointSymbolizer";
    }

    @Override
    protected Node getSLD()
    {
      Node root = super.getSLD();

      String fill = this.style.getPointFill();
      Double opacity = this.style.getPointOpacity();
      String stroke = this.style.getPointStroke();
      Integer width = this.style.getPointStrokeWidth();
      Double strokeOpacity = this.style.getPointStrokeOpacity();
      String wkn = this.style.getPointWellKnownName();
      Integer rotation = this.style.getPointRotation();

      NodeBuilder sizeNode = interpolateSize();
      
      node("Graphic").child(
          node("Mark").child(node("WellKnownName").text(wkn),
              node("Fill").child(css("fill", fill), css("fill-opacity", opacity)),
              node("Stroke").child(css("stroke", stroke), css("stroke-width", width), css("stroke-opacity", strokeOpacity))),
          sizeNode, node("Rotation").text(rotation)).build(root);

      return root;
    }
    
    private NodeBuilder interpolateSize()
    {
      if(this.visitor.currentLayer.getFeatureStrategy() == FeatureStrategy.BUBBLE)
      {
        ThematicStyle tStyle = (ThematicStyle) style;
        // attribute must be lowercase to work with postgres
        String attribute = tStyle.getAttribute().toLowerCase();
        String minAttr = SLDConstants.getMinProperty(attribute);
        String maxAttr = SLDConstants.getMaxProperty(attribute);
        
        
        // thematic interpolation
        return node("Size").child(
          node(OGC, "Function").attr("name", "Interpolate").child(
              // property to interpolate
              node(OGC, "PropertyName").text(attribute),
              // min definition
              node(OGC, "PropertyName").text(minAttr),
              node(OGC, "Literal").text(tStyle.getPointMinSize()),
              // max definition
              node(OGC, "PropertyName").text(maxAttr),
              node(OGC, "Literal").text(tStyle.getPointMaxSize()),
              // interpolation method
              node(OGC, "Literal").text("numeric")
              )
            );
      }
      else
      {
        // non-thematic
        return node("Size").text(style.getPointSize());
      }
    }
  }

  private static class PolygonSymbolizer extends Symbolizer
  {
    private PolygonSymbolizer(SLDMapVisitor visitor, Style style)
    {
      super(visitor, style);
    }

    @Override
    protected String getSymbolizerName()
    {
      return "PolygonSymbolizer";
    }

    @Override
    protected Node getSLD()
    {
      Node root = super.getSLD();

      Integer width = this.style.getPolygonStrokeWidth();
      Double fillOpacity = this.style.getPolygonFillOpacity();
      String stroke = this.style.getPolygonStroke();
      Double strokeOpacity = this.style.getPolygonStrokeOpacity();

      NodeBuilder fillNode = this.interpolateColor();
      
      node("Fill").child(fillNode, css("fill-opacity", fillOpacity)).build(root);

      node("Stroke").child(css("stroke", stroke), css("stroke-width", width),
          css("stroke-opacity", strokeOpacity)).build(root);

      return root;
    }
    
    private NodeBuilder interpolateColor()
    {
      if(this.visitor.currentLayer.getFeatureStrategy() == FeatureStrategy.GRADIENT)
      {
        ThematicStyle tStyle = (ThematicStyle) style;
        // attribute must be lowercase to work with postgres
        String attribute = tStyle.getAttribute().toLowerCase();
        String minAttr = SLDConstants.getMinProperty(attribute);
        String maxAttr = SLDConstants.getMaxProperty(attribute);
        
        
        // thematic interpolation
        return css("fill").child(
          node(OGC, "Function").attr("name", "Interpolate").child(
              // property to interpolate
              node(OGC, "PropertyName").text(attribute),
              // min definition
              node(OGC, "PropertyName").text(minAttr),
              node(OGC, "Literal").text(tStyle.getPolygonMinFill()),
              // max definition
              node(OGC, "PropertyName").text(maxAttr),
              node(OGC, "Literal").text(tStyle.getPolygonMaxFill()),
              // interpolation method
              node(OGC, "Literal").text("color")
              )
            );
      }
      else
      {
        // non-thematic
        String fill = this.style.getPolygonFill();
        return css("fill", fill);
      }
    }
  }

  private static class LineSymbolizer extends Symbolizer
  {
    private LineSymbolizer(SLDMapVisitor visitor, Style style)
    {
      super(visitor, style);
    }

    @Override
    protected String getSymbolizerName()
    {
      return "LineSymbolizer";
    }

    @Override
    protected Node getSLD()
    {
      Node root = super.getSLD();

      return root;
    }
  }

  private static class TextSymbolizer extends Symbolizer
  {
    private Node[] nodes;
    
    private String color;
    private String font;
    private String halo;
    private Integer haloWidth;
    private Integer size;   
    
    private TextSymbolizer(SLDMapVisitor visitor, Style style, Node... nodes)
    {
      super(visitor, style);
      this.nodes = nodes;
      this.color = style.getLabelColor();
      this.font = style.getLabelFont();
      this.halo = style.getLabelHalo();
      this.haloWidth = style.getLabelHaloWidth();
      this.size = style.getLabelSize();
    }

    @Override
    protected String getSymbolizerName()
    {
      return "TextSymbolizer";
    }

    @Override
    protected Node getSLD()
    {
      Node root = super.getSLD();

      node("Label").child(nodes).build(root);
      node("Font").child(
          css("font-size", size),
          css("font-family", font)
          ).build(root);
      node("Halo").child(
          node("Radius").text(haloWidth),
          node("Fill").child(css("fill", halo))
          ).build(root);
      node("Fill").child(
         css("fill", color) 
          ).build(root);
      
      // Label Positioning
      if (this.visitor.currentLayer.getFeatureType().compareTo(FeatureType.POINT) == 0) {
        node("LabelPlacement").child(node("PointPlacement").child(node("AnchorPoint").child(node("AnchorPointX").text("0.0"), node("AnchorPointY").text("0.5")))).build(root);
      }
      else {
        node("LabelPlacement").child(node("PointPlacement").child(node("AnchorPoint").child(node("AnchorPointX").text("0.5"), node("AnchorPointY").text("0.5")))).build(root);
      }
      
      // vendor options
      node("VendorOption").attr("name", "group").text(GeoserverProperties.getLabelGroup()).build(root);
      node("VendorOption").attr("name", "conflict-resolution").text(GeoserverProperties.getLabelConflictResolution()).build(root);
      node("VendorOption").attr("name", "spaceAround").text(GeoserverProperties.getLabelSpaceAround()).build(root);
      node("VendorOption").attr("name", "goodnessOfFit").text(GeoserverProperties.getLabelGoodnessOfFit()).build(root);
      
      return root;
    }
  }

  /**
   * Builder class to simplify node creation.
   */
  private static class NodeBuilder
  {
    private SLDMapVisitor visitor;

    private Document      doc;

    private Node          el;

    private NodeBuilder   parentBuilder;

    private NodeBuilder(SLDMapVisitor visitor, String ns, String node)
    {
      this(null, visitor, ns, node);
    }

    private NodeBuilder(NodeBuilder parentBuilder, SLDMapVisitor visitor, String ns, String node)
    {
      this.parentBuilder = parentBuilder;

      this.visitor = visitor;
      this.doc = visitor.doc;

      if (ns != null)
      {
        this.el = this.doc.createElement(ns + ":" + node);
      }
      else
      {
        this.el = this.doc.createElement(node);
      }
    }

    /**
     * This should only be called on Elements
     * 
     * @param name
     * @param value
     * @return
     */
    private NodeBuilder attr(String name, String value)
    {
      ( (Element) this.el ).setAttribute(name, value);
      return this;
    }

    private NodeBuilder text(Object o)
    {
      if (o != null)
      {
        text(o.toString());
      }

      return this;
    }

    private NodeBuilder text(String text)
    {
      if (text != null)
      {
        el.appendChild(this.doc.createTextNode(text));
      }

      return this;
    }

    private Node build(Node parent)
    {
      parent.appendChild(this.el);
      return this.el;
    }

    private Node build()
    {
      return this.el;
    }

    /**
     * Creates new builder that appends children to the current node.
     * 
     * @param ns
     * @param name
     * @return
     */
    private NodeBuilder child(NodeBuilder... children)
    {
      for (NodeBuilder child : children)
      {
        this.el.appendChild(child.el);
      }

      return this;
    }

    private NodeBuilder child(Node... children)
    {
      for (Node child : children)
      {
        this.el.appendChild(child);
      }

      return this;
    }
  }

  /**
   * The containing SLD Document.
   */
  private Document                                          doc;

  private static final String                               OGC = "ogc";

  private Map                                               map;

  private Stack<Node>                                       parents;

  private Node                                              currentLayerNode;

  private java.util.Map<String, Node>                       layerToNodeMap;

  private java.util.Map<Node, LinkedList<DocumentFragment>> layers;

  private Boolean                                           virtual;

  private FeatureType                                       featureType;

  private java.util.Map<Condition, Node>                    conditions;

  private Node                                              root;

  private Layer currentLayer;

  public SLDMapVisitor()
  {
    this.virtual = false;

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;

    try
    {
      builder = factory.newDocumentBuilder();
    }
    catch (ParserConfigurationException e)
    {
      throw new ConversionException(e);
    }

    this.map = null;
    this.doc = builder.newDocument();
    this.doc.setStrictErrorChecking(false);
    this.doc.setXmlStandalone(true);

    this.currentLayerNode = null;
    this.layerToNodeMap = new HashMap<String, Node>();
    this.parents = new Stack<Node>();
    this.layers = new LinkedHashMap<Node, LinkedList<DocumentFragment>>();
    this.root = null;
    this.conditions = new HashMap<Condition, Node>();
  }

  private NodeBuilder node(String ns, String node)
  {
    return new NodeBuilder(this, ns, node);
  }

  /**
   * Utility method to create a CSS parameter node.
   * 
   * @param name
   * @param value
   * @return
   */
  private NodeBuilder css(String name, Object value)
  {
    return new NodeBuilder(this, null, "CssParameter").attr("name", name).text(value);
  }

  private NodeBuilder css(String name)
  {
    return new NodeBuilder(this, null, "CssParameter").attr("name", name);
  }

  private NodeBuilder node(String node)
  {
    return new NodeBuilder(this, null, node);
  }

  public String getSLD(Layer layer)
  {
    Node layerNode = this.layerToNodeMap.get(layer.getId());

    Node sld = this.root.cloneNode(true);

    List<DocumentFragment> frags = this.layers.get(layerNode);
    for (DocumentFragment frag : frags)
    {
      layerNode.appendChild(frag);
    }

    sld.appendChild(layerNode);

    return printNode(sld);
  }

  public List<String> getSLD()
  {
    List<String> slds = new LinkedList<String>();
    for (Layer layer : this.map.getLayers())
    {
      slds.add(this.getSLD(layer));
    }

    return slds;
  }

  private String printNode(Node node)
  {
    try
    {
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(node), new StreamResult(writer));
      return writer.toString();
    }
    catch (TransformerException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void visit(Map map)
  {
    this.map = map;

    this.root = this.node("StyledLayerDescriptor").attr("xmlns", "http://www.opengis.net/sld")
        .attr("xmlns:sld", "http://www.opengis.net/sld").attr("xmlns:ogc", "http://www.opengis.net/ogc")
        .attr("xmlns:gml", "http://www.opengis.net/gml").attr("version", "1.0.0").build(this.doc);

    for (Layer layer : map.getLayers())
    {
      layer.accepts(this);
    }
  }

  @Override
  public void visit(Layer layer)
  {
    this.root = this.node("StyledLayerDescriptor").attr("xmlns", "http://www.opengis.net/sld")
        .attr("xmlns:sld", "http://www.opengis.net/sld").attr("xmlns:ogc", "http://www.opengis.net/ogc")
        .attr("xmlns:gml", "http://www.opengis.net/gml").attr("version", "1.0.0").build(this.doc);
    
    // We're starting a new layer so clear the prior structures
    this.parents.clear();
    this.conditions.clear();
    
    this.virtual = layer.getVirtual();
    this.featureType = layer.getFeatureType();
    
    Node layerNode = this.node("NamedLayer").child(this.node("Name").text(layer.getName())).build();
    
    Node userStyle = this.node("UserStyle").build(layerNode);
    
    parents.push(userStyle);
    
    this.currentLayer = layer;
    this.currentLayerNode = layerNode;
    layerToNodeMap.put(layer.getId(), layerNode);
    this.layers.put(layerNode, new LinkedList<DocumentFragment>());

    for (Style style : layer.getStyles())
    {
      style.accepts(this);
    }
  }

  private void style(Style style, Condition cond)
  {
    DocumentFragment rulesFragment = this.doc.createDocumentFragment();

    Node rule = this.node("Rule").child(this.node("Name").text(style.getName())).build();

    if (this.virtual)
    {
      Node fts = this.node("FeatureTypeStyle").child(rule).build();
      rulesFragment.appendChild(fts);
    }
    else
    {
      rulesFragment.appendChild(rule);
    }

    Symbolizer symbolizer;
    if (this.featureType == FeatureType.POINT)
    {
      symbolizer = new PointSymbolizer(this, style);
    }
    else if (this.featureType == FeatureType.POLYGON)
    {
      symbolizer = new PolygonSymbolizer(this, style);
    }
    else if (this.featureType == FeatureType.LINE)
    {
      symbolizer = new LineSymbolizer(this, style);
    }
    // TODO text symbolizer
    else
    {
      throw new ProgrammingErrorException("Geometry type [" + this.featureType
          + "] is not supported for SLD generation.");
    }

    // START - Thematic filter
    if (cond != null)
    {
      Node filter = this.node(OGC, "Filter").build(rule);

      this.parents.push(filter);

      cond.accepts(this);

      // pop the filter as the conditions tree has been added by now
      this.parents.pop();
    }
    // END - Thematic filter

    rule.appendChild(symbolizer.getSLD());

    boolean thematic = style instanceof ThematicStyle;

    if (thematic && style.getEnableLabel() && style.getEnableValue())
    {
      ThematicStyle tStyle = (ThematicStyle) style;
      Node[] nodes = new Node[] {
          node(OGC, "PropertyName").text(GeoEntity.DISPLAYLABEL.toLowerCase()).build(),
          this.doc.createTextNode(" - "),
          node(OGC, "PropertyName").text(tStyle.getAttribute().toLowerCase()).build() };

      TextSymbolizer text = new TextSymbolizer(this, style, nodes);
      rule.appendChild(text.getSLD());
    }
    else if (style.getEnableLabel())
    {
      Node[] nodes = new Node[] { node(OGC, "PropertyName").text(GeoEntity.DISPLAYLABEL.toLowerCase())
          .build() };

      TextSymbolizer text = new TextSymbolizer(this, style, nodes);
      rule.appendChild(text.getSLD());
    }
    else if (thematic && style.getEnableValue())
    {
      ThematicStyle tStyle = (ThematicStyle) style;
      Node[] nodes = new Node[] { node(OGC, "PropertyName").text(tStyle.getAttribute().toLowerCase())
          .build() };

      
      TextSymbolizer text = new TextSymbolizer(this, style, nodes);
      rule.appendChild(text.getSLD());
    }

    // append the rule to user styles
    this.parents.pop().appendChild(rulesFragment);
  }

  /**
   * Each Style is translated into a custom SLD Rule.
   */
  @Override
  public void visit(Style style)
  {
    DocumentFragment rulesFragment = this.doc.createDocumentFragment();

    Node rule = this.node("Rule").child(this.node("Name").text(style.getName())).build();

    if (this.virtual)
    {
      Node fts = this.node("FeatureTypeStyle").child(rule).build();
      rulesFragment.appendChild(fts);
    }
    else
    {
      rulesFragment.appendChild(rule);
    }

    Symbolizer symbolizer;
    if (this.featureType == FeatureType.POINT)
    {
      symbolizer = new PointSymbolizer(this, style);
    }
    else if (this.featureType == FeatureType.POLYGON)
    {
      symbolizer = new PolygonSymbolizer(this, style);
    }
    else if (this.featureType == FeatureType.LINE)
    {
      symbolizer = new LineSymbolizer(this, style);
    }
    // TODO text symbolizer
    else
    {
      throw new ProgrammingErrorException("Geometry type [" + this.featureType
          + "] is not supported for SLD generation.");
    }

    rule.appendChild(symbolizer.getSLD());

    this.parents.pop().appendChild(rulesFragment);
  }

  @Override
  public void visit(ThematicStyle style)
  {
    DocumentFragment rulesFragment = this.doc.createDocumentFragment();

    Node rule = this.node("Rule").child(this.node("Name").text(style.getName())).build();

    if (this.virtual)
    {
      Node fts = this.node("FeatureTypeStyle").child(rule).build();
      rulesFragment.appendChild(fts);
    }
    else
    {
      rulesFragment.appendChild(rule);
    }

    Symbolizer symbolizer;
    if (this.featureType == FeatureType.POINT)
    {
      symbolizer = new PointSymbolizer(this, style);
    }
    else if (this.featureType == FeatureType.POLYGON)
    {
      symbolizer = new PolygonSymbolizer(this, style);
    }
    else if (this.featureType == FeatureType.LINE)
    {
      symbolizer = new LineSymbolizer(this, style);
    }
    // TODO text symbolizer
    else
    {
      throw new ProgrammingErrorException("Geometry type [" + this.featureType
          + "] is not supported for SLD generation.");
    }

    // START - Thematic filter
//    Condition cond = style.getCondition();
//    if (cond != null)
//    {
//      Node filter = this.node(OGC, "Filter").build(rule);
//
//      this.parents.push(filter);
//
//      cond.accepts(this);
//
//      // pop the filter as the conditions tree has been added by now
//      this.parents.pop();
//    }
    // END - Thematic filter

    rule.appendChild(symbolizer.getSLD());

    boolean thematic = style instanceof ThematicStyle;

    if (thematic && style.getEnableLabel() && style.getEnableValue())
    {
      ThematicStyle tStyle = (ThematicStyle) style;
      Node[] nodes = new Node[] {
          node(OGC, "PropertyName").text(GeoEntity.DISPLAYLABEL.toLowerCase()).build(),
          this.doc.createTextNode(" - "),
          node(OGC, "PropertyName").text(tStyle.getAttribute().toLowerCase()).build() };

      TextSymbolizer text = new TextSymbolizer(this, style, nodes);
      rule.appendChild(text.getSLD());
    }
    else if (style.getEnableLabel())
    {
      Node[] nodes = new Node[] { node(OGC, "PropertyName").text(GeoEntity.DISPLAYLABEL.toLowerCase())
          .build() };

      TextSymbolizer text = new TextSymbolizer(this, style, nodes);
      rule.appendChild(text.getSLD());
    }
    else if (thematic && style.getEnableValue())
    {
      ThematicStyle tStyle = (ThematicStyle) style;
      Node[] nodes = new Node[] { node(OGC, "PropertyName").text(tStyle.getAttribute().toLowerCase())
          .build() };

      
      TextSymbolizer text = new TextSymbolizer(this, style, nodes);
      rule.appendChild(text.getSLD());
    }

    // append the rule to user styles
    this.parents.pop().appendChild(rulesFragment);
  }

  @Override
  public void visit(Or component)
  {
    Node or = this.node(OGC, "Or").build(this.parents.peek());

    this.parents.push(or);

    component.getLeftCondition().accepts(this);
    component.getRightCondition().accepts(this);

    this.parents.pop();
  }

  @Override
  public void visit(And component)
  {
    Node and = this.node(OGC, "And").build(this.parents.peek());

    this.parents.push(and);

    component.getLeftCondition().accepts(this);
    component.getRightCondition().accepts(this);

    this.parents.pop();
  }

  /**
   * Utility method that writes a Primitive condition.
   * 
   * @param name
   * @param cond
   */
  private void primitive(String name, Primitive cond)
  {
    Node parent = this.parents.peek();

    node(OGC, name).child(node(OGC, "PropertyName").text(cond.getThematicStyle().getAttribute()),
        node(OGC, "Literal").text(cond.getValue())).build(parent);
  }

  @Override
  public void visit(Equal component)
  {
    primitive("PropertyIsEqualTo", component);
  }

  @Override
  public void visit(GreaterThan component)
  {
    primitive("PropertyIsGreaterThan", component);
  }

  @Override
  public void visit(GreaterThanOrEqual component)
  {
    primitive("PropertyIsGreaterThanOrEqualTo", component);
  }

  @Override
  public void visit(LessThan component)
  {
    primitive("PropertyIsLessThan", component);
  }

  @Override
  public void visit(LessThanOrEqual component)
  {
    primitive("PropertyIsLessThanOrEqualTo", component);
  }

  @Override
  public void visit(IsBetween cond)
  {
    node("PropertyIsBetween").child(node("PropertyName").text(cond.getThematicStyle().getAttribute()),
        node("LowerBoundary").text(cond.getLowerBound()),
        node("UpperBoundary").text(cond.getUpperBound())).build(this.parents.peek());
  }

  @Override
  public void visit(IsLike component)
  {
    primitive("PropertyIsLike", component);
  }

  @Override
  public void visit(IsNull component)
  {
    node("PropertyIsNull").child(node("PropertyName").text(component.getThematicStyle().getAttribute()))
        .build(this.parents.peek());
  }

  @Override
  public void visit(Gradient component)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(Category component)
  {
    // TODO Auto-generated method stub

  }

}
