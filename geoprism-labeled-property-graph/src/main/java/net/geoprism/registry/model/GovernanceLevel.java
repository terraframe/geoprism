package net.geoprism.registry.model;

public enum GovernanceLevel {
  AUTHORITATIVE("Authoritative", "Official source of record"),

  OFFICIAL("Official", "Organizational publication"),

  COMMUNITY("Community Curated", "Community-maintained dataset"),

  RESEARCH("Research", "Research-produced dataset"),

  DERIVED("Derived", "Generated from source data"),

  EXPERIMENTAL("Experimental", "Prototype or pilot dataset"),

  AD_HOC("Ad Hoc", "Informally assembled dataset");

  private String name;

  private String description;

  private GovernanceLevel(String name, String description)
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
