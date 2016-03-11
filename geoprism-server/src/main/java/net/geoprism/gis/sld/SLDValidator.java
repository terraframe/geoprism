/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.gis.sld;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class SLDValidator
{
  
//  protected static final String         SLD_SCHEMA     = "src/test/resources/StyledLayerDescriptor.xsd";
//  
//  protected static final File           xsd            = new File(SLD_SCHEMA);
  

  protected static final Log            log            = LogFactory.getLog(SLDValidator.class);

  private InputStream xsd;
  
  public SLDValidator()
  {
    xsd = SLDValidator.class.getClassLoader().getResourceAsStream("StyledLayerDescriptor.xsd");
  }
  
  public void validate(String sld)
  {
    try
    {
      SchemaFactory f = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Source xsdS = new StreamSource(xsd);
      Schema schema = f.newSchema(xsdS);
      Validator v = schema.newValidator();

      InputStream stream;
      stream = new ByteArrayInputStream(sld.getBytes("UTF-8"));
      Source s = new StreamSource(stream);
      v.validate(s);
    }
    catch (Throwable e)
    {
      log.error(sld, e);
      throw new ProgrammingErrorException("Invalid SLD", e);
    }
  }
}
