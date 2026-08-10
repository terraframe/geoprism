package net.geoprism.registry.model;

public enum AuthorityType {
  GOVERNMENT("Government Agency", "National, state, provincial, municipal government organizations"),

  STATISTICAL("Statistical Agency", "Census bureaus, national statistics offices"),

  MAPPING("Mapping Agency", "National mapping agencies, cadastral agencies"),

  RESEARCH("Research Organization", "Universities, research institutes"),

  NGO("NGO", "WHO, UN agencies, World Bank"),

  PRIVATE("Private Sector", "Commercial organizations"),

  STANDARDS("Standards Organization", "OGC, HL7, ISO"),

  COMMUNITY("Community Organization", "OpenStreetMap communities, volunteer groups"),

  INDIVIDUAL("Individual", "Single person acting as publisher or curator"),

  CONSORTIUM("Consortium", "Multi-organization collaborative effort"),

  PROGRAM("Program", "A specific initiative that is not itself an organization");

  private String name;

  private String description;

  private AuthorityType(String name, String description)
  {
    this.name = name;
    this.description = description;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

}
