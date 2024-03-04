package net.geoprism.registry.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.registry.graph.AttributeGeometryType;
import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.AttributeValue;

public class AttributeState
{
  private static final class VertexTimeComparator implements Comparator<VertexObject>, Serializable
  {
    private static final long serialVersionUID = 5620180560116795690L;

    @Override
    public int compare(VertexObject o1, VertexObject o2)
    {
      Date d1 = o1.getObjectValue(AttributeValue.STARTDATE);
      Date d2 = o2.getObjectValue(AttributeValue.STARTDATE);

      return d1.compareTo(d2);
    }
  }

  private AttributeType           attributeType;

  private SortedSet<VertexObject> values;

  private List<VertexObject>      objectsToDelete;

  private boolean                 isModified;

  public AttributeState(AttributeType attributeType, List<VertexObject> values)
  {
    this.attributeType = attributeType;
    this.objectsToDelete = new LinkedList<>();
    this.values = new TreeSet<VertexObject>(new VertexTimeComparator());
    this.values.addAll(values);
    this.isModified = false;
  }

  public AttributeType getAttributeType()
  {
    return attributeType;
  }

  public boolean isModified()
  {
    return isModified;
  }

  public boolean add(VertexObject node)
  {
    this.isModified = true;

    return this.values.add(node);
  }
  
  void delete(VertexObject node)
  {
    this.isModified = true;

    this.objectsToDelete.add(node);

    this.values.remove(node);
  }

  public void persit(VertexObject vertex)
  {
    this.objectsToDelete.forEach(node -> node.delete());
    this.values.forEach(node -> {
      node.apply();

      if (attributeType instanceof AttributeGeometryType)
      {
        vertex.addChild(node, EdgeConstant.HAS_GEOMETRY.getMdEdge()).apply();
      }
      else
      {
        vertex.addChild(node, EdgeConstant.HAS_VALUE.getMdEdge()).apply();
      }
    });
  }

  public void delete()
  {
    this.objectsToDelete.forEach(node -> node.delete());
    this.values.forEach(node -> node.delete());
  }

  public void clear()
  {
    this.isModified = false;
    this.objectsToDelete.clear();
  }

  Iterator<VertexObject> iterator()
  {
    return new LinkedList<VertexObject>(this.values).iterator();
  }
  

  Stream<VertexObject> stream()
  {
    return this.values.stream();
  }
}
