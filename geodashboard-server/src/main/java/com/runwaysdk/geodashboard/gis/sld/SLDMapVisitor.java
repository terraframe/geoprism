package com.runwaysdk.geodashboard.gis.sld;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.text.*;
import java.awt.Color;

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
import com.runwaysdk.system.gis.geo.Universal;
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
      return "FeatureTypeStyle";
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
      
      
      node("FeatureTypeName").text(style.getName()).build(root);
      
      Node ruleNode = node("Rule").build(root);
      node("Name").text("point").build(ruleNode);
      node("Title").text("point").build(ruleNode);
      
      // Polygon styles
      Node pointSymbolNode = node("PointSymbolizer").build(ruleNode);
      node("Geometry").child(node(OGC, "PropertyName").text("geom")).build(pointSymbolNode);
      
      node("Graphic").child(
          node("Mark").child(node("WellKnownName").text(wkn),
              node("Fill").child(css("fill", fill), css("fill-opacity", opacity)),
              node("Stroke").child(css("stroke", stroke), css("stroke-width", width), css("stroke-opacity", strokeOpacity))),
          sizeNode, node("Rotation").text(rotation)).build(pointSymbolNode);
      
      // Adding labels
      ThematicStyle tStyle = (ThematicStyle) style;
      boolean thematic = style instanceof ThematicStyle;

      if (thematic && style.getEnableLabel() && style.getEnableValue())
      {
        Node[] nodes = new Node[] {
            node(OGC, "PropertyName").text(GeoEntity.DISPLAYLABEL.toLowerCase()).build(),
            node(OGC, "PropertyName").text(tStyle.getAttribute().toLowerCase()).build() };

        TextSymbolizer text = new TextSymbolizer(visitor, style, nodes);
        ruleNode.appendChild(text.getSLD());
      }
      else if (style.getEnableLabel())
      {
        Node[] nodes = new Node[] { node(OGC, "PropertyName").text(GeoEntity.DISPLAYLABEL.toLowerCase()).build() };

        TextSymbolizer text = new TextSymbolizer(visitor, style, nodes);
        ruleNode.appendChild(text.getSLD());
      }
      else if (thematic && style.getEnableValue())
      {
        Node[] nodes = new Node[] { node(OGC, "PropertyName").text(tStyle.getAttribute().toLowerCase()).build() };

        TextSymbolizer text = new TextSymbolizer(visitor, style, nodes);
        ruleNode.appendChild(text.getSLD());
      }

      return root;
    }
    
    private NodeBuilder interpolateSize()
    {
      if(this.visitor.currentLayer.getFeatureStrategy() == FeatureStrategy.BUBBLE)
      {
        ThematicStyle tStyle = (ThematicStyle) style;
        // attribute must be lowercase to work with postgres
        String attribute = tStyle.getAttribute().toLowerCase();
        
        // This currently relies on 2 db table fields to contain min/max values for the entire table
        // This could be replaced with the getMinMax() method as used in PolygonSymbolizer but will require a rewrite
        // of the sld generation.  If this is done remember to remove the generation of the min/max fields from DashboardLayer.java
        //// http://docs.geoserver.org/latest/en/user/styling/sld-tipstricks/transformation-func.html#interpolate
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
  
  
  /**
   * Build <PolygonSymbolizer> tag and contents
   * a <PolygonSymbolizer> encloses the styling params and the geometry field used for rendering features
   * 
   */
  private static class PolygonSymbolizer extends Symbolizer
  {
    private PolygonSymbolizer(SLDMapVisitor visitor, Style style)
    {
      super(visitor, style);
    }

    @Override
    protected String getSymbolizerName()
    {
      return "FeatureTypeStyle";
    }

    @Override
    protected Node getSLD()
    {
      // supers up to Symbolizer to getSymbolizerName() and builds the node from the return
      // i.e. builds the <PolygonSymbolizer> node in this case
      Node root = super.getSLD();

      Integer width = this.style.getPolygonStrokeWidth();
      Double fillOpacity = this.style.getPolygonFillOpacity();
      String stroke = this.style.getPolygonStroke();
      Double strokeOpacity = this.style.getPolygonStrokeOpacity();
      String fill = this.style.getPolygonFill();

      
      node("FeatureTypeName").text(style.getName()).build(root);
      
      if(this.visitor.currentLayer.getFeatureStrategy() == FeatureStrategy.GRADIENT)
      {
        // SLD generation
        ThematicStyle tStyle = (ThematicStyle) style;
        // attribute must be lowercase to work with postgres
        String attribute = tStyle.getAttribute().toLowerCase();
        
        HashMap<Integer, Color> gradientColors = this.interpolateColor();
        
        HashMap<String, Double> minMaxMap = this.visitor.currentLayer.getLayerMinMax(attribute);
        double minAttrVal = minMaxMap.get("min");
        double maxAttrVal = minMaxMap.get("max");
        
        int numCategories;
        if(minAttrVal == maxAttrVal){
          // min/max are the same suggesting there is only one feature (i.e. gradient on a single polygon)
          numCategories = 1;
        }
        else{
          numCategories = 5;
        }
        
        double categoryLen = (maxAttrVal - minAttrVal) / numCategories;
        
        for(int i = 0; i<numCategories; i++){
          
          double currentCatMin = minAttrVal + (i * categoryLen);
          double currentCatMax = minAttrVal + ((i + 1) * categoryLen);  
          
          int currentColorPos = i + 1;
          Color currentColor = gradientColors.get(currentColorPos);
          String currentColorHex = String.format("#%02x%02x%02x", currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue());
          
          DecimalFormat displayVal = new DecimalFormat("#.##");
          String currentCatMinDisplay = displayVal.format(currentCatMin);
          String currentCatMaxDisplay = displayVal.format(currentCatMax);
          
          Node ruleNode = node("Rule").build(root);
          node("Name").text(currentCatMinDisplay+ " - " +currentCatMaxDisplay).build(ruleNode);
          node("Title").text(currentCatMinDisplay+ " - " +currentCatMaxDisplay).build(ruleNode);
  
          Node filterNode = node(OGC, "Filter").build(ruleNode);
          Node firstAndNode = node(OGC, "And").build(filterNode);
          Node notNode = node(OGC, "Not").build(firstAndNode);
          Node secondAndNode = node(OGC, "And").build(notNode);
          
          Node firstPropEqualToNode = node(OGC, "PropertyIsEqualTo").build(secondAndNode);
            node(OGC, "Literal").text("ALL_LABEL_CLASSES_ENABLED").build(firstPropEqualToNode);
            node(OGC, "Literal").text("ALL_LABEL_CLASSES_ENABLED").build(firstPropEqualToNode);
            
          Node orNode = node(OGC, "Or").build(secondAndNode);
          Node propIsNullNode = node(OGC, "PropertyIsNull").build(orNode);
            node(OGC, "PropertyName").text(attribute).build(propIsNullNode);
            
          Node propEqualToNode = node(OGC, "PropertyIsEqualTo").build(orNode);
            node(OGC, "Literal").text("NEVER").build(propEqualToNode);
            node(OGC, "Literal").text("TRUE").build(propEqualToNode);
            
          Node propIsBetween = node(OGC, "PropertyIsBetween").build(firstAndNode);
          node(OGC, "PropertyName").text(attribute).build(propIsBetween);
          node(OGC, "LowerBoundary").child(node("Literal").text(currentCatMin)).build(propIsBetween);
          node(OGC, "UpperBoundary").child(node("Literal").text(currentCatMax)).build(propIsBetween);

          
          // Polygon styles
          Node polySymbolNode = node("PolygonSymbolizer").build(ruleNode);
          node("Geometry").child(node(OGC, "PropertyName").text("geom")).build(polySymbolNode);
          Node geomFillNode = node("Fill").build(polySymbolNode);
              css("fill", currentColorHex).build(geomFillNode);
              css("fill-opacity", fillOpacity).build(geomFillNode);
              
          node("Stroke").child(css("stroke", stroke), css("stroke-width", width), css("stroke-opacity", strokeOpacity)).build(polySymbolNode);
          
          // Adding labels
          boolean thematic = style instanceof ThematicStyle;

          if (thematic && style.getEnableLabel() && style.getEnableValue())
          {
            Node[] nodes = new Node[] {
                node(OGC, "PropertyName").text(GeoEntity.DISPLAYLABEL.toLowerCase()).build(),
                node(OGC, "PropertyName").text(tStyle.getAttribute().toLowerCase()).build() };

            TextSymbolizer text = new TextSymbolizer(visitor, style, nodes);
            ruleNode.appendChild(text.getSLD());
          }
          else if (style.getEnableLabel())
          {
            Node[] nodes = new Node[] { node(OGC, "PropertyName").text(GeoEntity.DISPLAYLABEL.toLowerCase()).build() };

            TextSymbolizer text = new TextSymbolizer(visitor, style, nodes);
            ruleNode.appendChild(text.getSLD());
          }
          else if (thematic && style.getEnableValue())
          {
            Node[] nodes = new Node[] { node(OGC, "PropertyName").text(tStyle.getAttribute().toLowerCase()).build() };

            TextSymbolizer text = new TextSymbolizer(visitor, style, nodes);
            ruleNode.appendChild(text.getSLD());
          }
          
        };
      }
      else
      {
        
        Node ruleNode = node("Rule").build(root);
        node("Name").text("basic").build(ruleNode);
        node("Title").text("basic").build(ruleNode);
        
        // Polygon styles
        Node polySymbolNode = node("PolygonSymbolizer").build(ruleNode);
        node("Geometry").child(node(OGC, "PropertyName").text("geom")).build(polySymbolNode);
        Node geomFillNode = node("Fill").build(polySymbolNode);
            css("fill", fill).build(geomFillNode);
            css("fill-opacity", fillOpacity).build(geomFillNode);
            
        node("Stroke")
            .child(css("stroke", stroke), css("stroke-width", width), css("stroke-opacity", strokeOpacity)).build(polySymbolNode);
      }
      return root;
    }
    
 
    
    private HashMap<Integer, Color> interpolateColor()
    {
      HashMap<Integer, Color> colorRGBList = new HashMap<Integer, Color>();
      if(this.visitor.currentLayer.getFeatureStrategy() == FeatureStrategy.GRADIENT)
      {
        
        ThematicStyle tStyle = (ThematicStyle) style;
        
        String minFill = tStyle.getPolygonMinFill();
        String maxFill = tStyle.getPolygonMaxFill();
        
        Color minFillRGB = Color.decode(minFill);
        Color maxFillRGB = Color.decode(maxFill);
        colorRGBList.put(1, minFillRGB);
        colorRGBList.put(5, maxFillRGB);
        
        // RGB color values
        int r1 = minFillRGB.getRed();
        int g1 = minFillRGB.getGreen();
        int b1 = minFillRGB.getBlue();

        int r2 = maxFillRGB.getRed();
        int g2 = maxFillRGB.getGreen();
        int b2 = maxFillRGB.getBlue();
        
        double stepIncrease = .2;
        double stepVal = .2;
        for (int i = 0; i < 4; i++)
        {
          int red = (int) ( r1 + ( stepVal * ( r2 - r1 ) ) );
          int green = (int) ( g1 + ( stepVal * ( g2 - g1 ) ) );
          int blue = (int) ( b1 + ( stepVal * ( b2 - b1 ) ) );
          Color newColor = new Color(red, green, blue);
          colorRGBList.put(i+1, newColor);
          stepVal = stepIncrease + stepVal;
        }
      }
      return colorRGBList;
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
    
    Node userStyle = this.node("UserStyle").child(node("Title").text(layer.getName())).build(layerNode);
    
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
    
    // append the rule to user styles
    this.parents.pop().appendChild(symbolizer.getSLD());
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
