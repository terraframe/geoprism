package net.geoprism.registry;

public class RuntimeParseException extends RuntimeException
{

  private static final long serialVersionUID = 6346939160339087404L;

  public RuntimeParseException()
  {
    super();

  }

  public RuntimeParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public RuntimeParseException(String message, Throwable cause)
  {
    super(message, cause);

  }

  public RuntimeParseException(String message)
  {
    super(message);

  }

  public RuntimeParseException(Throwable cause)
  {
    super(cause);

  }

}
