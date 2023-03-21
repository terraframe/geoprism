package net.geoprism.account;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.lang.Nullable;

import net.geoprism.GeoprismUser;
import net.geoprism.GeoprismUserDTO;

public class GeoprismUserView
{
  /**
   * Is null when its a new user
   */
  @Nullable
  private String oid;
  
  @NotEmpty
  private String username;
  
  @Nullable
  private String email;
  
  @Nullable
  private String firstName;
  
  @Nullable
  private String lastName;
  
  @Nullable
  private String phoneNumber;
  
  public GeoprismUserView()
  {
    
  }

  public GeoprismUserView(String username, String email, String firstName, String lastName, String phoneNumber, String oid)
  {
    super();
    this.username = username;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.oid = oid;
  }
  
  public static GeoprismUserView fromGeoprismUserDTO(GeoprismUserDTO dto)
  {
    GeoprismUserView view = new GeoprismUserView(dto.getUsername(), dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getPhoneNumber(), dto.getOid());
    return view;
  }
  
  public static GeoprismUserView fromGeoprismUser(GeoprismUser business)
  {
    GeoprismUserView view = new GeoprismUserView(business.getUsername(), business.getEmail(), business.getFirstName(), business.getLastName(), business.getPhoneNumber(), business.getOid());
    return view;
  }
  
  public void populate(GeoprismUser business)
  {
    business.setUsername(this.getUsername());
    business.setEmail(this.getEmail());
    business.setFirstName(this.getFirstName());
    business.setLastName(this.getLastName());
    business.setPhoneNumber(this.getPhoneNumber());
  }
  
  public String getOid()
  {
    return oid;
  }

  public void setOid(String oid)
  {
    this.oid = oid;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }
}
