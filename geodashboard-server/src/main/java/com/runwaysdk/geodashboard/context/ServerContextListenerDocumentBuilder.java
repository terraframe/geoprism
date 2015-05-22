package com.runwaysdk.geodashboard.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.runwaysdk.generation.loader.Reloadable;

public class ServerContextListenerDocumentBuilder implements Reloadable
{

  public List<ServerContextListenerInfo> read()
  {
    List<ServerContextListenerInfo> list = new LinkedList<ServerContextListenerInfo>();

    InputStream stream = this.getClass().getResourceAsStream("/server-initializer.xml");

    if (stream != null)
    {
      try
      {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(stream);

        NodeList elements = document.getElementsByTagName("server-context-listener");

        for (int i = 0; i < elements.getLength(); i++)
        {
          Node element = elements.item(i);

          NamedNodeMap attributes = element.getAttributes();
          Node className = attributes.getNamedItem("class");

          list.add(new ServerContextListenerInfo(className.getNodeValue()));
        }

      }
      catch (SAXException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      catch (ParserConfigurationException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      finally
      {
        try
        {
          stream.close();
        }
        catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }

    return list;
  }
}
