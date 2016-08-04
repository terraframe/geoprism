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
package net.geoprism.data.etl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.geoprism.MappableClass;
import net.geoprism.ontology.GeoEntityUtil;

import com.runwaysdk.business.Transient;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.Universal;

public class LocationValidator implements ConverterIF
{
  private static class State
  {
    private Collection<Term> universals;

    private Iterator<Term>   iterator;

    private Term             current;

    public State(Collection<Term> universals)
    {
      this.universals = universals;
      this.iterator = this.universals.iterator();
    }

    public void next()
    {
      if (this.iterator.hasNext())
      {
        this.current = this.iterator.next();
      }
      else
      {
        this.current = null;
      }
    }

    public boolean hasNext()
    {
      return this.iterator.hasNext();
    }

    public Universal getCurrent()
    {
      return (Universal) current;
    }
  }

  private TargetContextIF        context;

  private Set<LocationProblemIF> problems;

  private Map<String, State>     states;

  public LocationValidator(TargetContextIF context)
  {
    this.context = context;
    this.problems = new TreeSet<LocationProblemIF>();
    this.states = new HashMap<String, State>();

    List<TargetDefinitionIF> definitions = context.getDefinitions();

    for (TargetDefinitionIF definition : definitions)
    {
      MappableClass mClass = MappableClass.getMappableClass(definition.getTargetType());

      List<? extends Universal> lowests = mClass.getAllUniversal().getAll();

      for (Universal lowest : lowests)
      {
        Universal root = Universal.getRoot();

        Collection<Term> universals = GeoEntityUtil.getOrderedAncestors(root, lowest, AllowedIn.CLASS);

        this.states.put(definition.getSourceType(), new State(universals));
      }
    }
  }

  @Override
  public void create(Transient source, List<HashMap<String, String>> locationExclusions)
  {
    Universal universal = this.states.get(source.getType()).getCurrent();

    if (universal != null)
    {
      List<TargetFieldIF> fields = this.context.getFields(source.getType());

      for (TargetFieldIF field : fields)
      {
        if (field instanceof TargetFieldGeoEntityIF)
        {
          TargetFieldGeoEntityIF entity = (TargetFieldGeoEntityIF) field;

          LocationProblemIF problem = entity.getLocationProblem(source, universal, locationExclusions);

          if (problem != null)
          {
            this.problems.add(problem);
          }
        }
      }
    }
  }

  public Collection<LocationProblemIF> getProblems()
  {
    return problems;
  }

  public void next()
  {
    List<TargetDefinitionIF> definitions = context.getDefinitions();

    for (TargetDefinitionIF definition : definitions)
    {
      State state = this.states.get(definition.getSourceType());
      state.next();
    }
  }

  public boolean isFinished()
  {
    boolean isFinished = ( this.problems.size() > 0 );

    if (!isFinished)
    {
      List<TargetDefinitionIF> definitions = context.getDefinitions();

      for (TargetDefinitionIF definition : definitions)
      {
        State state = this.states.get(definition.getSourceType());

        isFinished = isFinished || !state.hasNext();
      }
    }

    return isFinished;
  }
}
