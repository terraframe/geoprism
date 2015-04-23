package com.runwaysdk.geodashboard.gis.persist;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMomentDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.MdAttributeView;
import com.runwaysdk.geodashboard.util.Predicate;

public class MdAttributeViewPredicate implements Predicate<MdAttributeView>, Reloadable
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

    return comparisonAttribute.getId().equals(mdAttribute.getId());
  }

}
