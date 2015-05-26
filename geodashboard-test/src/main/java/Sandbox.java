import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class Sandbox
{
  public static void main(String[] args) throws Exception
  {
    GeometryFactory gf = new GeometryFactory();

    Coordinate coord = new Coordinate(1,1);
    Point point = gf.createPoint(coord);

    System.out.println(point);
  }
}
