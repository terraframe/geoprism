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
import com.runwaysdk.transport.conversion.ConversionException;

/**
 * Traverses an object graph of map Component objects and creates an SLD
 * document.
 */
public class SLDMapVisitor implements MapVisitor
{
  private static abstract class Symbolizer
  {
    protected SLDMapVisitor visitor;
    protected Style style;
    
    private Symbolizer(SLDMapVisitor visitor, Style style)
    {
      super();
      this.visitor = visitor;
      this.style = style;
    }
    
    protected NodeBuilder node(String node)
    {
      return this.visitor.node(node);
    }
    
    protected NodeBuilder css(String name, Object value)
    {
      return this.visitor.css(name, value);
    }
    
    protected abstract String getSymbolizerName();
    
    protected Node getSLD()
    {
      return this.visitor.node(this.getSymbolizerName()).build();
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
      
      Integer size = this.style.getPointSize();
      String fill = this.style.getPointFill();
      Double opacity = this.style.getPointOpacity();
      String stroke = this.style.getPointStroke();
      Integer width = this.style.getPointStrokeWidth();
      String wkn = this.style.getPointWellKnownName();
      Integer rotation = this.style.getPointRotation();
      
      node("Graphic").child(
        node("Mark").child(
          node("WellKnownName").text(wkn),
          node("Fill").child(
            css("fill", fill),
            css("fill-opacity", opacity)
          ),
          node("Stroke").child(
            css("stroke", stroke),
            css("stroke-width", width)
          )
        ), node("Size").text(size),
        node("Rotation").text(rotation)
       ).build(root);
      
      return root;
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
      String fill = this.style.getPolygonFill();
      String stroke = this.style.getPolygonStroke();
      
      node("Fill").child(
        css("fill", fill)
      ).build(root);
      
      node("Stroke").child(
        css("stroke", stroke),
        css("stroke-width", width)
      ).build(root);
      
      return root;
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
    private TextSymbolizer(SLDMapVisitor visitor, Style style)
    {
      super(visitor, style);
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

    private Node       el;

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
        this.el = this.doc.createElementNS(ns, node);
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
      ((Element)this.el).setAttribute(name, value);
      return this;
    }

    private NodeBuilder text(Object o)
    {
      if(o != null)
      {
        text(o.toString());
      }
      
      return this;
    }
    
    private NodeBuilder text(String text)
    {
      if(text != null)
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
  private Document    doc;

  private Stack<Node> parents;
  
  private Node currentLayer;
  
  private java.util.Map<Layer, Node> layerToNodeMap;
  
  private java.util.Map<Node, LinkedList<DocumentFragment>> layers;
  
  private Boolean     virtual;

  private FeatureType featureType;
  
  private java.util.Map<Condition, Node> conditions;
  
  private Node root;

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

    this.doc = builder.newDocument();
    this.doc.setStrictErrorChecking(false);
    this.doc.setXmlStandalone(true);

    this.currentLayer = null;
    this.layerToNodeMap = new HashMap<Layer, Node>();
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

  private NodeBuilder node(String node)
  {
    return new NodeBuilder(this, null, node);
  }

  public Document getDocument()
  {
    return this.doc;
  }
  
  public String getSLD(Layer layer)
  {
    return null;
  }

  public String getSLD()
  {
    return assembleComponents();
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

  private String assembleComponents()
  {
   
    Node sld = this.doc.cloneNode(true);

    // Create a new document for each layer if requested
    for(Node layerNode : this.layers.keySet())
    {
      List<DocumentFragment> frags = this.layers.get(layerNode);
      for(DocumentFragment frag : frags)
      {
        layerNode.appendChild(frag);
      }
      
      sld.appendChild(layerNode);
    }
    
    return printNode(sld);
  }

  @Override
  public void visit(Map map)
  {
    this.root = this.node("StyledLayerDescriptioner").attr("xmlns", "http://www.opengis.net/sld")
        .attr("xmlns:sld", "http://www.opengis.net/sld").attr("xmlns:ogc", "http://www.opengis.net/ogc")
        .attr("xmlns:gml", "http://www.opengis.net/gml").attr("version", "1.0.0").build(this.doc);
  }

  @Override
  public void visit(Layer layer)
  {
    // We're starting a new layer so clear the prior structures
    this.parents.clear();
    this.conditions.clear();
    
    this.virtual = layer.getVirtual();
    this.featureType = layer.getFeatureType();

    Node layerNode = this.node("NamedLayer")
        .child(this.node("Name").text(layer.getName()), this.node("UserStyle")).build();

    parents.push(layerNode);

    this.currentLayer = layerNode;
    layerToNodeMap.put(layer, layerNode);
    this.layers.put(layerNode, new LinkedList<DocumentFragment>());
  }

  /**
   * Each Style is translated into a custom SLD Rule.
   */
  @Override
  public void visit(Style style)
  {
    DocumentFragment rulesFragment = this.doc.createDocumentFragment();
    
    this.layers.get(this.currentLayer).add(rulesFragment);
    
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
    if(this.featureType == FeatureType.POINT)
    {
      symbolizer = new PointSymbolizer(this, style);
    }
    else if(this.featureType == FeatureType.POLYGON)
    {
      symbolizer = new PolygonSymbolizer(this, style);
    }
    else if(this.featureType == FeatureType.LINE)
    {
      symbolizer = new LineSymbolizer(this, style);
    }
    // TODO text symbolizer
    else
    {
      throw new ProgrammingErrorException("Geometry type ["+this.featureType+"] is not supported for SLD generation.");
    }
    
    rule.appendChild(symbolizer.getSLD());
  }

  @Override
  public void visit(ThematicStyle style)
  {
    DocumentFragment rulesFragment = this.doc.createDocumentFragment();
    
    this.layers.get(this.currentLayer).add(rulesFragment);
    
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
    if(this.featureType == FeatureType.POINT)
    {
      symbolizer = new PointSymbolizer(this, style);
    }
    else if(this.featureType == FeatureType.POLYGON)
    {
      symbolizer = new PolygonSymbolizer(this, style);
    }
    else if(this.featureType == FeatureType.LINE)
    {
      symbolizer = new LineSymbolizer(this, style);
    }
    // TODO text symbolizer
    else
    {
      throw new ProgrammingErrorException("Geometry type ["+this.featureType+"] is not supported for SLD generation.");
    }
    
    rule.appendChild(symbolizer.getSLD());
   
    Node filter = this.node("Filter").build(rule);
    
    this.parents.push(filter);
  }

  @Override
  public void visit(Or component)
  {
    Node or = this.node("Or").build();

    Condition parent = component.getParentCondition();
    if(conditions.containsKey(parent))
    {
      conditions.get(parent).appendChild(or);
    }
    else
    {
      this.parents.peek().appendChild(or);
    }

    this.parents.push(or);
    conditions.put(component, or);
  }

  @Override
  public void visit(And component)
  {
    Node and = this.node("And").build();

    Condition parent = component.getParentCondition();
    if(conditions.containsKey(parent))
    {
      conditions.get(parent).appendChild(and);
    }
    else
    {
      this.parents.peek().appendChild(and);
    }
    
    this.parents.push(and);
    conditions.put(component, and);
  }
  
  /**
   * Utility method that writes a Primitive condition.
   * 
   * @param name
   * @param cond
   */
  private void primitive(String name, Primitive cond)
  {
    node(name).child(
        node("PropertyName").text(cond.getThematicStyle().getAttribute()),
        node("Literal").text(cond.getValue())).build(this.parents.peek());
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
    primitive("PropertyIsGreaterThanOrEqual", component);
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
    node("PropertyIsBetween").child(
      node("PropertyName").text(cond.getThematicStyle().getAttribute()),
      node("LowerBoundary").text(cond.getLowerBound()),
      node("UpperBoundary").text(cond.getUpperBound()))
    .build(this.parents.peek());
  }
  
  @Override
  public void visit(IsLike component)
  {
    primitive("PropertyIsLike", component);    
  }
  
  @Override
  public void visit(IsNull component)
  {
    node("PropertyIsNull").child(node("PropertyName")
        .text(component.getThematicStyle().getAttribute())).build(this.parents.peek());     
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
