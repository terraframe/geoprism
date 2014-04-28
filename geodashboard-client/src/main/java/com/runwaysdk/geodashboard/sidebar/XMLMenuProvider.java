package com.runwaysdk.geodashboard.sidebar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.RunwayConfigurationException;

public class XMLMenuProvider
{
  private static final String fileName = "geodashboard/sidebar.xml";
  
  private static final Object initializeLock = new Object();
  
  private static ArrayList<MenuItem> menu;
  
  public XMLMenuProvider() {
    String exMsg = "An exception occurred while reading the geodashboard sidebar configuration file.";
    
    try
    {
      this.readMenu();
    }
    catch (Exception e)
    {
      throw new RunwayConfigurationException(exMsg, e);
    }
  }
  
  public ArrayList<MenuItem> getMenu() {
    return menu;
  }
  
  public void readMenu() throws ParserConfigurationException, SAXException, IOException {
    
    synchronized(initializeLock) {
      if (menu != null) {
        return;
      }
      
      menu = new ArrayList<MenuItem>();
    }
    
    InputStream stream = ConfigurationManager.getResourceAsStream(ConfigurationManager.ConfigGroup.ROOT, fileName);
    
    if (stream != null) {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(stream);
      
      Element xmlMappings = doc.getDocumentElement();
      
      NodeList children = xmlMappings.getChildNodes();
      for (int i = 0; i < children.getLength(); ++i) {
        Node n = children.item(i);
        
        if (n.getNodeType() == Node.ELEMENT_NODE) {
          Element el = (Element) n;
          
          String name = el.getAttribute("name");
          String uri = null;
          if (el.hasAttribute("uri")) {
            uri = el.getAttribute("uri");
          }
          
          MenuItem item = new MenuItem(name, uri);
          
          NodeList itemChildren = el.getChildNodes();
          for (int iChild = 0; iChild < itemChildren.getLength(); ++iChild) {
            Node nodeItem = itemChildren.item(iChild);
            
            if (nodeItem.getNodeType() == Node.ELEMENT_NODE) {
              Element elChild = (Element) nodeItem;
              
              String name2 = elChild.getAttribute("name");
              String uri2 = elChild.getAttribute("uri");
              
              item.addChild(new MenuItem(name2, uri2));
            }
          }
          
          menu.add(item);
        }
      }
    }
    else {
      throw new RunwayConfigurationException("Expected the geodashboard sidebar configuration file on the classpath at [" + fileName + "].");
    }
  }
}
