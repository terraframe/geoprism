package net.geoprism.ontology;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.vividsolutions.jts.geom.Envelope;

public class PublisherUtil
{
  public static Envelope getTileBounds(JSONObject object)
  {
    try
    {
      int x = object.getInt("x");
      int y = object.getInt("y");
      int zoom = object.getInt("z");

      return PublisherUtil.getTileBounds(x, y, zoom);
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static Envelope getTileBounds(int x, int y, int zoom)
  {
    try
    {
      CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326", true);
      CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3857", true);
      MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);

      Envelope envelope = new Envelope(getLong(x, zoom), getLong(x + 1, zoom), getLat(y, zoom), getLat(y + 1, zoom));

      return JTS.transform(envelope, transform);
    }
    catch (FactoryException | TransformException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static double getLong(int x, int zoom)
  {
    return ( x / Math.pow(2, zoom) * 360 - 180 );
  }

  public static double getLat(int y, int zoom)
  {
    double n = Math.PI - 2 * Math.PI * y / Math.pow(2, zoom);
    // return ( 180 / Math.PI * Math.atan(0.5 * ( Math.exp(n) - Math.exp(-n) )) );
    return Math.toDegrees(Math.atan(Math.sinh(n)));
  }
}
