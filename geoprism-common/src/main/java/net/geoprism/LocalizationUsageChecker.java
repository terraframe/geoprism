package net.geoprism;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A simple program which, given a localization xml file, will check through a source codebase to find keys which are not used.
 * 
 * args[0] = path to the domain xml file containing localization keys
 * args[1] = path to the root of the source code repository where we will check to see if each key is used.
 * 
 * @author rrowlands
 */
public class LocalizationUsageChecker
{
  public static void main(String[] args)
  {
    new LocalizationUsageChecker(new File(args[0]), new File(args[1])).run();
  }
  
  class LocalizationObject {
    private String key;
    private String xml;
    private boolean used;
    
    public LocalizationObject(String key, String xml, boolean used)
    {
      super();
      this.key = key;
      this.xml = xml;
      this.used = used;
    }
    
    public String getKey()
    {
      return key;
    }
    public void setKey(String key)
    {
      this.key = key;
    }
    public String getXml()
    {
      return xml;
    }
    public void setXml(String xml)
    {
      this.xml = xml;
    }
    public boolean isUsed()
    {
      return used;
    }
    public void setUsed(boolean used)
    {
      this.used = used;
    }
  }
  
  final File domainFile;
  final File sourceCodeRepoPath;
  
  // Holds a map from 'key' to 'object' of all objects in the domain file
  Map<String, LocalizationObject> keyMap;
  
  public LocalizationUsageChecker(File domainFile, File sourceCodeRepoPath)
  {
    this.domainFile = domainFile;
    this.sourceCodeRepoPath = sourceCodeRepoPath;
    this.keyMap = new HashMap<String, LocalizationObject>();
  }
  
  public void run()
  {
    readKeys(domainFile);
    
    searchForKeys(sourceCodeRepoPath);
    
    prettyPrintUnused();
  }
  
  public void readKeys(File domainFile)
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    try {
      DocumentBuilder db = dbf.newDocumentBuilder();

      Document doc = db.parse(domainFile);

      doc.getDocumentElement().normalize();

      NodeList objects = doc.getElementsByTagName("object");

      for (int i = 0; i < objects.getLength(); i++) {
        Node object = objects.item(i);

        if (object.getNodeType() == Node.ELEMENT_NODE) {

            Element element = (Element) object;

            final String key = element.getAttribute("key");
            
            keyMap.put(key, new LocalizationObject(key, nodeToString(element), false));

        }
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
        throw new RuntimeException(e);
    }
  }
  
  private static String nodeToString(Node node) {
    StringWriter sw = new StringWriter();
    try {
      Transformer t = TransformerFactory.newInstance().newTransformer();
      t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      t.setOutputProperty(OutputKeys.INDENT, "no");
      t.transform(new DOMSource(node), new StreamResult(sw));
    } catch (TransformerException te) {
      throw new RuntimeException(te);
    }
    return sw.toString();
  }
  
  public void searchForKeys(File parent)
  {
    for (File file : parent.listFiles())
    {
      if (file.isFile() && (file.getName().endsWith(".ts") || file.getName().endsWith(".html")))
      {
        try
        {
          final String fileStr = FileUtils.readFileToString(file, "UTF-8");
          
          for (LocalizationObject obj : keyMap.values())
          {
            if (!obj.isUsed() && fileStr.contains(obj.getKey()))
            {
              obj.setUsed(true);
            }
          }
        }
        catch (IOException e)
        {
          throw new RuntimeException(e);
        }
      }
      else if (file.isDirectory() && !(file.getName().equals(".") || file.getName().equals("..")))
      {
        searchForKeys(file);
      }
    }
  }
  
  public void prettyPrintUnused()
  {
    List<String> removeObjects = new ArrayList<String>();
    
    for (LocalizationObject obj : keyMap.values())
    {
      if (!obj.isUsed())
      {
        removeObjects.add("<object key=\"" + obj.getKey() + "\" type=\"com.runwaysdk.localization.LocalizedValueStore\"/>");
      }
    }
    
    System.out.println("Unused objects:\n" + StringUtils.join(removeObjects, "\n"));
    
    List<String> usedObjects = new ArrayList<String>();
    
    for (LocalizationObject obj : keyMap.values())
    {
      if (obj.isUsed())
      {
        usedObjects.add(obj.getXml());
      }
    }
    
    System.out.println("Objects in use:\n" + StringUtils.join(usedObjects, "\n"));
  }
}
