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
package net.geoprism.ontology;

@com.runwaysdk.business.ClassSignature(hash = 112335328)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ClassifierProblemView.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  abstract  class ClassifierProblemViewQueryBase extends com.runwaysdk.query.GeneratedViewQuery
 implements com.runwaysdk.generation.loader.Reloadable
{

  public ClassifierProblemViewQueryBase(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
  }

  public ClassifierProblemViewQueryBase(com.runwaysdk.query.QueryFactory componentQueryFactory, com.runwaysdk.query.ViewQueryBuilder viewQueryBuilder)
  {
    super(componentQueryFactory, viewQueryBuilder);
  }
  public String getClassType()
  {
    return net.geoprism.ontology.ClassifierProblemView.CLASS;
  }
  public com.runwaysdk.query.SelectableChar getClassifierId()
  {
    return getClassifierId(null);

  }
 
  public com.runwaysdk.query.SelectableChar getClassifierId(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.ontology.ClassifierProblemView.CLASSIFIERID, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getClassifierId(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.ontology.ClassifierProblemView.CLASSIFIERID, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableChar getConcreteId()
  {
    return getConcreteId(null);

  }
 
  public com.runwaysdk.query.SelectableChar getConcreteId(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.ontology.ClassifierProblemView.CONCRETEID, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getConcreteId(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.ontology.ClassifierProblemView.CONCRETEID, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableChar getId()
  {
    return getId(null);

  }
 
  public com.runwaysdk.query.SelectableChar getId(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.ontology.ClassifierProblemView.ID, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getId(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.ontology.ClassifierProblemView.ID, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableChar getProblem()
  {
    return getProblem(null);

  }
 
  public com.runwaysdk.query.SelectableChar getProblem(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.ontology.ClassifierProblemView.PROBLEM, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getProblem(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.ontology.ClassifierProblemView.PROBLEM, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableChar getProblemName()
  {
    return getProblemName(null);

  }
 
  public com.runwaysdk.query.SelectableChar getProblemName(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.ontology.ClassifierProblemView.PROBLEMNAME, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getProblemName(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.ontology.ClassifierProblemView.PROBLEMNAME, alias, displayLabel);

  }
 
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  @java.lang.SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends ClassifierProblemView> getIterator()
  {
    com.runwaysdk.query.ValueIterator valueIterator;
    if (_pageSize != null && _pageNumber != null)
    {
      valueIterator = (com.runwaysdk.query.ValueIterator<com.runwaysdk.dataaccess.ValueObject>)this.getComponentQuery().getIterator(_pageSize, _pageNumber);
    }
    else
    {
      valueIterator = (com.runwaysdk.query.ValueIterator<com.runwaysdk.dataaccess.ValueObject>)this.getComponentQuery().getIterator();
    }
    return new com.runwaysdk.query.ViewIterator<ClassifierProblemView>(this.getMdClassIF(), valueIterator);
  }

}
