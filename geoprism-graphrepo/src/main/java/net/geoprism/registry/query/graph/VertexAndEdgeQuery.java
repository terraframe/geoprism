package net.geoprism.registry.query.graph;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.EdgeDirection;
import net.geoprism.registry.model.graph.ServerObjectVertex;
import net.geoprism.registry.service.business.EdgeQueryResultSetConverter;
import net.geoprism.registry.service.business.EdgeQueryResultSetConverter.EdgeQueryResult;

public class VertexAndEdgeQuery
{
  public enum Direction {
    CHILDREN, OUT, // Child is out
    PARENTS, IN, // Parent is in
    BOTH;

    static Direction from(EdgeDirection direction)
    {
      if (EdgeDirection.CHILD.equals(direction))
        return CHILDREN;
      else if (EdgeDirection.PARENT.equals(direction))
        return PARENTS;
      else
        return BOTH;
    }
  }

  @FunctionalInterface
  public interface ResultProcessor
  {
    List<? extends ServerObjectVertex> process(List<VertexObject> results, Date date);
  }

  public static class EdgeQueryObject
  {
    private final ServerObjectVertex    object;

    private final String                edgeClass;

    private final String                edgeOid;

    private final String                edgeUid;

    private final String                edgeSource;

    private final Date                  startDate;

    private final Date                  endDate;

    private final List<EdgeQueryObject> related;

    public EdgeQueryObject(ServerObjectVertex object, String edgeClass, String edgeOid, String edgeUid, String edgeSource, Date startDate, Date endDate)
    {
      this.object = object;
      this.edgeClass = edgeClass;
      this.edgeOid = edgeOid;
      this.edgeUid = edgeUid;
      this.edgeSource = edgeSource;
      this.startDate = startDate;
      this.endDate = endDate;
      this.related = new ArrayList<EdgeQueryObject>();
    }

    public ServerObjectVertex getObject()
    {
      return this.object;
    }

    public String getEdgeClass()
    {
      return this.edgeClass;
    }

    public String getOid()
    {
      return this.edgeOid;
    }

    public String getUid()
    {
      return this.edgeUid;
    }

    public String getSource()
    {
      return this.edgeSource;
    }

    public Date getStartDate()
    {
      return this.startDate;
    }

    public Date getEndDate()
    {
      return this.endDate;
    }

    public List<EdgeQueryObject> getRelated()
    {
      return this.related;
    }

    public void addRelated(EdgeQueryObject object)
    {
      this.related.add(object);
    }
  }

  private final VertexObject    source;

  private final String          edgeType;

  private final Direction       direction;

  private final ResultProcessor processor;

  private Date                  date;

  private String                boundsWKT;

  private Long                  skip;

  private Long                  limit;

  private boolean               recursive;

  public VertexAndEdgeQuery(VertexObject source, String edgeType, Direction direction, ResultProcessor processor)
  {
    this.source = source;
    this.edgeType = edgeType;
    this.direction = direction;
    this.processor = processor;
  }
  
  public VertexAndEdgeQuery(VertexObject source, String edgeType, EdgeDirection direction, ResultProcessor processor)
  {
    this.source = source;
    this.edgeType = edgeType;
    this.direction = Direction.from(direction);
    this.processor = processor;
  }

  public VertexAndEdgeQuery setDate(Date date)
  {
    this.date = date;
    return this;
  }

  public VertexAndEdgeQuery setBoundsWKT(String boundsWKT)
  {
    this.boundsWKT = boundsWKT;
    return this;
  }

  public VertexAndEdgeQuery setSkip(Long skip)
  {
    this.skip = skip;
    return this;
  }

  public VertexAndEdgeQuery setLimit(Long limit)
  {
    this.limit = limit;
    return this;
  }

  public VertexAndEdgeQuery setRecursive(boolean recursive)
  {
    this.recursive = recursive;
    return this;
  }

  public boolean isRecursive()
  {
    return this.recursive;
  }

  public List<EdgeQueryObject> getResults()
  {
    return this.getResults(new HashSet<String>());
  }

  private List<EdgeQueryObject> getResults(Set<String> visited)
  {
    visited.add(this.source.getRID().toString());

    List<EdgeQueryObject> results = this.getDirectResults();

    if (this.recursive)
    {
      for (EdgeQueryObject result : results)
      {
        String rid = result.getObject().getVertex().getRID().toString();

        if (!visited.contains(rid))
        {
          visited.add(rid);

          VertexAndEdgeQuery query = new VertexAndEdgeQuery(result.getObject().getVertex(), this.edgeType, this.direction, this.processor).setDate(this.date).setBoundsWKT(this.boundsWKT).setRecursive(true);

          List<EdgeQueryObject> related = query.getResults(visited);

          result.getRelated().addAll(related);
        }
      }
    }

    return results;
  }

  public List<EdgeQueryObject> getDirectResults()
  {
    Map<String, Object> parameters = new HashMap<String, Object>();

    parameters.put("rid", this.source.getRID());

    if (this.date != null)
    {
      parameters.put("date", this.date);
    }

    if (this.boundsWKT != null)
    {
      parameters.put("bounds", this.boundsWKT);
    }

    GraphQuery<EdgeQueryResult> query = new GraphQuery<EdgeQueryResult>(this.buildStatement(), parameters, new EdgeQueryResultSetConverter());

    List<EdgeQueryResult> rawResults = query.getResults();

    /*
     * The custom converter has reconstructed each actual traversal record as a
     * VertexObject. Feed that exact sequence into the existing object
     * processor.
     */
    List<VertexObject> objectResults = new ArrayList<VertexObject>();

    /*
     * Every traversal row belonging to the same root object contains the same
     * originVertexRid and edge metadata. We only need one metadata record per
     * origin vertex.
     */
    Map<String, EdgeQueryResult> metadata = new HashMap<String, EdgeQueryResult>();

    for (EdgeQueryResult result : rawResults)
    {
      if (result.getVertex() != null)
      {
        objectResults.add(result.getVertex());
      }

      if (result.getOriginVertexRid() != null)
      {
        metadata.put(result.getOriginVertexRid(), result);
      }
    }

    List<? extends ServerObjectVertex> objects = this.processor.process(objectResults, this.date);

    List<EdgeQueryObject> results = new ArrayList<EdgeQueryObject>();

    for (ServerObjectVertex object : objects)
    {
      String rid = object.getVertex().getRID().toString();

      EdgeQueryResult edge = metadata.get(rid);

      if (edge == null)
      {
        throw new ProgrammingErrorException("Unable to resolve edge metadata for vertex [" + rid + "]");
      }

      results.add(new EdgeQueryObject(object, edge.getEdgeClass(), edge.getEdgeOid(), edge.getEdgeUid(), edge.getEdgeSource(), edge.getStartDate(), edge.getEndDate()));
    }

    return results;
  }

  private String buildStatement()
  {
    String edgeExpression = this.getOriginEdgeExpression();

    StringBuilder statement = new StringBuilder();

    /*
     * The outer query expands the edge metadata from the originEdge calculated
     * by the inner traversal query. This avoids repeating the same edge lookup
     * for every metadata field.
     *
     * SELECT * is deliberate. The object/attribute types can contain attributes
     * created dynamically at runtime, so VertexAndEdgeQuery cannot know the
     * complete column list ahead of time.
     */
    statement.append("SELECT ");
    statement.append("*, ");
    statement.append("originEdge.@class AS edgeClass, ");
    statement.append("originEdge.oid AS edgeOid, ");
    statement.append("originEdge.uid AS edgeUid, ");
    statement.append("originEdge.dataSource.code AS edgeSource, ");
    statement.append("originEdge.startDate AS startDate, ");
    statement.append("originEdge.endDate AS endDate ");

    statement.append("FROM (");

    /*
     * traversedElement() must be invoked by the SELECT directly wrapping the
     * TRAVERSE command. Moving this expression into LET or the outer SELECT
     * causes OrientDB to report that it is not being invoked against a traverse
     * command.
     */
    statement.append("SELECT ");
    statement.append("*, ");
    statement.append("$depth AS traversalDepth, ");

    /*
     * The RID of the traversal root associates every traversal record with the
     * edge connecting that root object to the original query source.
     */
    statement.append("traversedElement(0).@rid AS originVertexRid, ");

    /*
     * Resolve the edge once per traversal row. The outer query reads all of the
     * individual metadata fields from this value.
     */
    statement.append(edgeExpression);
    statement.append(" AS originEdge ");

    statement.append("FROM (");

    statement.append("TRAVERSE out('");
    statement.append(EdgeConstant.HAS_VALUE.getDBClassName());
    statement.append("', '");
    statement.append(EdgeConstant.HAS_GEOMETRY.getDBClassName());
    statement.append("') ");

    statement.append("FROM (");
    statement.append(this.buildOriginQuery());
    statement.append(")");

    statement.append(")");

    statement.append(")");

    return statement.toString();
  }

  private String buildOriginQuery()
  {
    switch (this.direction)
    {
      case CHILDREN, OUT:
        return this.buildDirectedOriginQuery("outE", "inV");

      case PARENTS, IN:
        return this.buildDirectedOriginQuery("inE", "outV");

      case BOTH:
        return this.buildUndirectedOriginQuery();

      default:
        throw new UnsupportedOperationException("Unsupported direction [" + this.direction + "]");
    }
  }

  private String buildDirectedOriginQuery(String edgeFunction, String vertexFunction)
  {
    StringBuilder statement = new StringBuilder();

    statement.append("SELECT EXPAND(");
    statement.append(edgeFunction);
    statement.append("('");
    statement.append(this.edgeType);
    statement.append("')");

    if (this.date != null)
    {
      statement.append("[:date BETWEEN startDate AND endDate]");
    }

    statement.append(".");
    statement.append(vertexFunction);
    statement.append("()) ");

    statement.append("FROM :rid");

    this.appendBounds(statement, " WHERE ");

    this.appendPaging(statement);

    return statement.toString();
  }

  private String buildUndirectedOriginQuery()
  {
    StringBuilder statement = new StringBuilder();

    statement.append("SELECT FROM (");

    statement.append("SELECT EXPAND(");
    statement.append("bothE('");
    statement.append(this.edgeType);
    statement.append("')");

    if (this.date != null)
    {
      statement.append("[:date BETWEEN startDate AND endDate]");
    }

    statement.append(".bothV()) ");

    statement.append("FROM :rid");

    statement.append(") ");

    /*
     * bothV() returns both endpoints, including the source itself.
     */
    statement.append("WHERE @rid != :rid");

    this.appendBounds(statement, " AND ");

    this.appendPaging(statement);

    return statement.toString();
  }

  private void appendBounds(StringBuilder statement, String prefix)
  {
    if (this.boundsWKT == null)
    {
      return;
    }

    statement.append(prefix);

    statement.append("out('");
    statement.append(EdgeConstant.HAS_GEOMETRY.getDBClassName());
    statement.append("')");

    if (this.date != null)
    {
      statement.append("[:date BETWEEN startDate AND endDate" + " AND ST_INTERSECTS(value, :bounds) = true]");
    }
    else
    {
      statement.append("[ST_INTERSECTS(value, :bounds) = true]");
    }

    statement.append(".size() > 0");
  }

  private void appendPaging(StringBuilder statement)
  {
    if (this.skip != null)
    {
      statement.append(" SKIP ");
      statement.append(this.skip);
    }

    if (this.limit != null)
    {
      statement.append(" LIMIT ");
      statement.append(this.limit);
    }
  }

  private String getOriginEdgeExpression()
  {
    StringBuilder expression = new StringBuilder();

    expression.append("traversedElement(0).");

    switch (this.direction)
    {
      case CHILDREN, OUT:
      {
        /*
         * source --edge--> child
         *
         * Starting from the child, find the incoming edge whose OUT endpoint is
         * the source of this EdgeQuery.
         */
        expression.append("inE('");
        expression.append(this.edgeType);
        expression.append("')");

        this.appendEdgeDateFilter(expression);

        expression.append("[out = :rid][0]");

        break;
      }

      case PARENTS, IN:
      {
        /*
         * parent --edge--> source
         *
         * Starting from the parent, find the outgoing edge whose IN endpoint is
         * the source of this EdgeQuery.
         */
        expression.append("outE('");
        expression.append(this.edgeType);
        expression.append("')");

        this.appendEdgeDateFilter(expression);

        expression.append("[in = :rid][0]");

        break;
      }

      case BOTH:
      {
        /*
         * For an undirected relationship either endpoint may be :rid.
         */
        expression.append("bothE('");
        expression.append(this.edgeType);
        expression.append("')");

        this.appendEdgeDateFilter(expression);

        expression.append("[out = :rid OR in = :rid][0]");

        break;
      }

      default:
      {
        throw new UnsupportedOperationException("Unsupported direction [" + this.direction + "]");
      }
    }

    return expression.toString();
  }

  private void appendEdgeDateFilter(StringBuilder expression)
  {
    if (this.date != null)
    {
      expression.append("[:date BETWEEN startDate AND endDate]");
    }
  }
}