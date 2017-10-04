package net.geoprism;

import java.io.IOException;
import java.io.InputStream;

public class ContentStream extends InputStream
{
  private final InputStream in;

  private final String      contentType;

  public ContentStream(InputStream in, String contentType)
  {
    this.in = in;
    this.contentType = contentType;
  }

  public int read() throws IOException
  {
    return in.read();
  }

  public int read(byte b[]) throws IOException
  {
    return in.read(b);
  }

  public int read(byte b[], int off, int len) throws IOException
  {
    return in.read(b, off, len);
  }

  public long skip(long n) throws IOException
  {
    return in.skip(n);
  }

  public int available() throws IOException
  {
    return in.available();
  }

  public void close() throws IOException
  {
    in.close();
  }

  public void mark(int readlimit)
  {
    in.mark(readlimit);
  }

  public void reset() throws IOException
  {
    in.reset();
  }

  public boolean markSupported()
  {
    return in.markSupported();
  }

  public String getContentType()
  {
    return contentType;
  }
}
