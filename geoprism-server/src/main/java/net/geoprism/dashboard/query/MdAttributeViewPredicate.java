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
package net.geoprism.dashboard.query;

import net.geoprism.dashboard.MdAttributeView;
import net.geoprism.util.Predicate;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMomentDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;


public class MdAttributeViewPredicate implements Predicate<MdAttributeView>
{
  private MdAttributeDAOIF mdAttribute;

  public MdAttributeViewPredicate(MdAttributeDAOIF mdAttribute)
  {
    this.mdAttribute = mdAttribute;
  }

  @Override
  public boolean evaulate(MdAttributeView _t)
  {
    MdAttributeDAOIF comparisonAttribute = MdAttributeDAO.get(_t.getMdAttributeId());

    if (comparisonAttribute.getMdAttributeConcrete() instanceof MdAttributeMomentDAOIF)
    {
      return true;
    }

    return comparisonAttribute.getOid().equals(mdAttribute.getOid());
  }

}
