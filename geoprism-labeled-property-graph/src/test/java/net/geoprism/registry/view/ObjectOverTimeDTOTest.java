/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.view;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.junit.Assert;
import org.junit.Test;

public class ObjectOverTimeDTOTest
{

  @Test
  public void testBasic()
  {
    Date startDate = new Date();
    Date endDate = new Date();
    String code = "TestCode";
    String label = "Test Code";
    TypeInfo type = new TypeInfo(TypeClass.BUSINESS_TYPE, "Test");
    String basicValue = "simple";
    String multiValue = "mu";

    ObjectOverTimeDTO object = new ObjectOverTimeDTO();
    object.setCode(code);
    object.setLabel(label);
    object.setType(type);
    object.setValue("basic", basicValue);
    object.put("multi", AttributeLocalDTO.of(ValueOverTimeEntryDTO.of("ata", startDate, endDate, new LocalizedValue(multiValue))));

    Assert.assertEquals(code, object.getCode());
    Assert.assertEquals(label, object.getLabel());
    Assert.assertEquals(type, object.getType());
    Assert.assertEquals(basicValue, object.getValue("basic"));

    List<ValueOverTimeEntryDTO<LocalizedValue>> values = object.getValuesOverTime("multi");

    Assert.assertEquals(1, values.size());

    ValueOverTimeEntryDTO<LocalizedValue> value = values.get(0);

    Assert.assertEquals(startDate, value.getStartDate());
    Assert.assertEquals(endDate, value.getEndDate());
    Assert.assertEquals(multiValue, value.getValue().getLocalizedValue());
  }

  @Test
  public void testSerialization()
  {
    Calendar startDate = Calendar.getInstance();
    startDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    startDate.clear();
    startDate.set(2020, 2, 1, 0, 0);

    Calendar endDate = Calendar.getInstance();
    endDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    endDate.clear();
    endDate.set(2020, 4, 1, 0, 0);

    String code = "TestCode";
    String label = "Test Code";
    TypeInfo type = new TypeInfo(TypeClass.BUSINESS_TYPE, "Test");
    String basicValue = "simple";
    String multiValue = "mu";

    ObjectOverTimeDTO object = new ObjectOverTimeDTO();
    object.setCode(code);
    object.setLabel(label);
    object.setType(type);
    object.setValue("basic", basicValue);
    object.put("boolean", AttributeBooleanDTO.of(ValueOverTimeEntryDTO.of("ata", startDate, endDate, true)));
    object.put("character", AttributeCharacterDTO.of(ValueOverTimeEntryDTO.of("ata", startDate, endDate, multiValue)));
    object.put("classification", AttributeClassificationDTO.of(ValueOverTimeEntryDTO.of("ata", startDate, endDate, multiValue)));
    object.put("source", AttributeDataSourceDTO.of(ValueOverTimeEntryDTO.of("ata", startDate, endDate, multiValue)));
    object.put("date", AttributeDateDTO.of(ValueOverTimeEntryDTO.of("ata", startDate, endDate, endDate.getTime())));
    object.put("float", AttributeFloatDTO.of(ValueOverTimeEntryDTO.of("ata", startDate, endDate, 1.5D)));
    object.put("integer", AttributeIntegerDTO.of(ValueOverTimeEntryDTO.of("ata", startDate, endDate, 1L)));
    object.put("local", AttributeLocalDTO.of(ValueOverTimeEntryDTO.of("ata", startDate, endDate, new LocalizedValue(multiValue))));

    String json = ObjectOverTimeDTO.toJson(object);

    System.out.println(json);

    object = ObjectOverTimeDTO.parseJson(json);

    Assert.assertEquals(code, object.getCode());
    Assert.assertEquals(label, object.getLabel());
    Assert.assertEquals(type, object.getType());
    Assert.assertEquals(basicValue, object.getValue("basic"));

    List<ValueOverTimeEntryDTO<LocalizedValue>> values = object.getValuesOverTime("local");

    Assert.assertEquals(1, values.size());

    ValueOverTimeEntryDTO<LocalizedValue> value = values.get(0);

    Assert.assertEquals(startDate.getTime(), value.getStartDate());
    Assert.assertEquals(endDate.getTime(), value.getEndDate());
    Assert.assertEquals(multiValue, value.getValue().getLocalizedValue());

    List<ValueOverTimeEntryDTO<Date>> dates = object.getValuesOverTime("date");

    Assert.assertEquals(1, dates.size());

    ValueOverTimeEntryDTO<Date> date = dates.get(0);

    Assert.assertEquals(startDate.getTime(), date.getStartDate());
    Assert.assertEquals(endDate.getTime(), date.getEndDate());
    Assert.assertEquals(endDate.getTime(), date.getValue());
  }

  @Test
  public void testToDate()
  {
    Calendar startDate = Calendar.getInstance();
    startDate.set(2020, 2, 1, 0, 0);

    Calendar endDate = Calendar.getInstance();
    endDate.set(2020, 4, 1, 0, 0);

    String code = "TestCode";
    String label = "Test Code";
    TypeInfo type = new TypeInfo(TypeClass.BUSINESS_TYPE, "Test");
    String basicValue = "simple";
    String multiValue = "mu";

    ObjectOverTimeDTO object = new ObjectOverTimeDTO();
    object.setCode(code);
    object.setLabel(label);
    object.setType(type);
    object.setValue("basic", basicValue);
    object.put("multi", AttributeLocalDTO.of(ValueOverTimeEntryDTO.of("ata", startDate, endDate, new LocalizedValue(multiValue))));

    ObjectAtTimeDTO dto = object.toDate(startDate.getTime());

    Assert.assertEquals(code, dto.getCode());
    Assert.assertEquals(label, dto.getLabel());
    Assert.assertEquals(type, dto.getType());
    Assert.assertEquals(basicValue, dto.getValue("basic"));
    Assert.assertEquals(multiValue, ( (LocalizedValue) dto.getValue("multi") ).getLocalizedValue());

    // Outside of multi time range entry
    Calendar calendar = Calendar.getInstance();
    calendar.set(2020, 7, 1, 0, 0);

    dto = object.toDate(calendar.getTime());

    Assert.assertEquals(code, dto.getCode());
    Assert.assertEquals(label, dto.getLabel());
    Assert.assertEquals(type, dto.getType());
    Assert.assertEquals(basicValue, dto.getValue("basic"));
    Assert.assertNull(dto.getValue("multi"));
  }
}
