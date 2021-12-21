/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data.etl;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

public interface TargetFieldIF
{
  /**
   * @return Name of the MdAttribute of the target field
   */
  public String getName();

  /**
   * @return Key of the MdAttribute of the target field
   */
  public String getKey();

  /**
   * @return Label of the target field
   */
  public String getLabel();

  /**
   * @return Flag indicates if the target field can be aggregated
   */
  public Boolean getAggregatable();

  /**
   * Gets the value from the source object for the given MdAttribute
   * 
   * @param mdAttribute
   * @param source
   * @return
   */
  public FieldValue getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source);

  /**
   * Persists the definition of the TargetField to the database
   * 
   * @param binding
   */
  public void persist(TargetBinding binding);

}
