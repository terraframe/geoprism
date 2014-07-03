import com.runwaysdk.geodashboard.gis.persist.NoLayersExceptionDTO;
import com.runwaysdk.session.Request;


public class Sandbox
{
  @Request
  public static void main(String[] args)
  {
    NoLayersExceptionDTO ex = new NoLayersExceptionDTO(this.getClientRequest(), this.req.getLocale());
    String msg = ex.getLocalizedMessage();
  }
}
