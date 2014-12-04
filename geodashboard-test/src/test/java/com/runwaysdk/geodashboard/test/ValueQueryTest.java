package com.runwaysdk.geodashboard.test;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

//import com.example.AdvancedDashboard;
//import com.example.BasicDashboard;
//import com.example.DashboardView;
//import com.example.DashboardViewDTO;
import com.runwaysdk.ClientSession;
import com.runwaysdk.business.ValueObjectDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdBusiness;

public class ValueQueryTest
{
  private static MdBusiness basicDashboard;

  private static MdBusiness advancedDashboard;

  @BeforeClass
  @Request
  public static void classStartup()
  {
//    basicDashboard = MdBusiness.getMdBusiness(BasicDashboard.CLASS);
//    advancedDashboard = MdBusiness.getMdBusiness(AdvancedDashboard.CLASS);
  }

  @AfterClass
  public static void classTeardown()
  {

  }
/*
  @Test
  @Request
  public void test1()
  {
    MdBusiness mdBusiness = MdBusiness.getMdBusiness(BasicDashboard.CLASS);

    ValueQuery values = DashboardView.getValues(mdBusiness.getId());

    OIterator<ValueObject> iterator = values.getIterator();

    try
    {
      int count = 0;

      while (iterator.hasNext())
      {
        ValueObject value = iterator.next();

        System.out.println("Result " + count + ": " + value.getValue(BasicDashboard.TESTCHAR) + ", " + value.getValue(BasicDashboard.TESTINTEGER));
      }
    }
    finally
    {
      iterator.close();
    }
  }

  @Test
  public void test2()
  {
    ClientSession session = ClientSession.createUserSession("admin", "admin", new Locale[] { Locale.US });

    try
    {
      ClientRequestIF request = session.getRequest();

      ValueQueryDTO query = DashboardViewDTO.getValues(request, basicDashboard.getId());

      List<ValueObjectDTO> results = query.getResultSet();
      Iterator<ValueObjectDTO> iterator = results.iterator();

      int count = 0;

      while (iterator.hasNext())
      {
        ValueObjectDTO value = iterator.next();

        System.out.println("Result " + count + ": " + value.getValue(BasicDashboard.TESTCHAR) + ", " + value.getValue(BasicDashboard.TESTINTEGER));
      }
    }
    finally
    {
      session.logout();
    }
  }

  @Test
  @Request
  public void testPopulator()
  {
    for (int i = 0; i < 100; i++)
    {
      BasicDashboard basic = new BasicDashboard();
      basic.setTestChar("Test basic character : " + ( i % 2 ));
      basic.setTestInteger(i);
      basic.apply();

      AdvancedDashboard advanced = new AdvancedDashboard();
      advanced.setAdvancedChar("Test advanced character : " + i);
      advanced.apply();
    }
  }
  */
}
