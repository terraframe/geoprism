package com.runwaysdk.geodashboard;

import java.io.File;
import java.io.IOException;

import com.runwaysdk.controller.tag.develop.TLDGenerator;

public class GeodashboardTLDGenerator
{
  public static void main(String[] args)
  {
    try
    {
      new TLDGenerator(new File(args[0]), new Class<?>[] { LocalizedTagSupport.class }, "Geodashboard").generate();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

}
