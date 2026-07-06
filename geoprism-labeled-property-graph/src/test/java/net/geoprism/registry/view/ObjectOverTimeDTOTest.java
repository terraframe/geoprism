package net.geoprism.registry.view;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ObjectOverTimeDTOTest
{

  @Test
  public void test()
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
    object.put("multi", new MultiValueDTO(Arrays.asList(new ValueOverTimeEntryDTO("ata", startDate, endDate, multiValue))));

    Assert.assertEquals(code, object.getCode());
    Assert.assertEquals(label, object.getLabel());
    Assert.assertEquals(type, object.getType());
    Assert.assertEquals(basicValue, object.getValue("basic"));

    List<ValueOverTimeEntryDTO> values = object.getValuesOverTime("multi");

    Assert.assertEquals(1, values.size());

    ValueOverTimeEntryDTO value = values.get(0);

    Assert.assertEquals(startDate, value.getStartDate());
    Assert.assertEquals(endDate, value.getEndDate());
    Assert.assertEquals(multiValue, value.getValue());
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
    object.put("multi", new MultiValueDTO(Arrays.asList(new ValueOverTimeEntryDTO("ata", startDate.getTime(), endDate.getTime(), multiValue))));

    ObjectAtTimeDTO dto = object.toDate(startDate.getTime());

    Assert.assertEquals(code, dto.getCode());
    Assert.assertEquals(label, dto.getLabel());
    Assert.assertEquals(type, dto.getType());
    Assert.assertEquals(basicValue, dto.getValue("basic"));
    Assert.assertEquals(multiValue, dto.getValue("multi"));

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
