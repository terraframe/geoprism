package com.runwaysdk.geodashboard.gis;


public class BeanWrapper<T>
{
  private Object   instance;

  public BeanWrapper(Object instance)
  {
    this.instance = instance;
  }

  @SuppressWarnings("unchecked")
  public T get(String attribute)
  {
    String methodName = this.getMethodName(attribute);
    Class<? extends Object> clazz = instance.getClass();

    try
    {
      return (T) clazz.getMethod(methodName).invoke(instance);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public void set(String attribute, T value)
  {
    String methodName = this.setMethodName(attribute);
    Class<? extends Object> clazz = instance.getClass();
    
    try
    {
      clazz.getMethod(methodName, value.getClass()).invoke(instance, value);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  private String getMethodName(String attribute)
  {
    return "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
  }
  
  private String setMethodName(String attribute)
  {
    return "set" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
  }
}
