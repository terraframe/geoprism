package net.geoprism;

import java.util.Set;
import java.util.TreeSet;

public class GeoprismApplication
{
  private String      id;

  private String      src;

  private String      label;

  private String      url;

  private Set<String> roleNames;

  public GeoprismApplication()
  {
    this.roleNames = new TreeSet<String>();
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getSrc()
  {
    return src;
  }

  public void setSrc(String src)
  {
    this.src = src;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public void addRole(String roleName)
  {
    this.roleNames.add(roleName);
  }

  public Set<String> getRoleNames()
  {
    return roleNames;
  }

  public void setRoleNames(Set<String> roleNames)
  {
    this.roleNames = roleNames;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public String getUrl()
  {
    return url;
  }

  public boolean isValid(Set<String> roleNames)
  {
    TreeSet<String> contains = new TreeSet<String>(this.roleNames);
    contains.retainAll(roleNames);

    return ( this.roleNames.size() == 0 || !contains.isEmpty() );
  }
}
