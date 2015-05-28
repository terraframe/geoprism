package com.runwaysdk.geodashboard.report;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.persist.AllAggregationType;
import com.runwaysdk.geodashboard.localization.LocalizationFacade;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierQuery;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.Coalesce;
import com.runwaysdk.query.F;
import com.runwaysdk.query.GeneratedBusinessQuery;
import com.runwaysdk.query.SelectableChar;
import com.runwaysdk.query.SelectablePrimitive;
import com.runwaysdk.query.SelectableReference;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF;
import com.runwaysdk.system.gis.geo.LocatedInQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;

public abstract class AbstractProvider implements Reloadable, ReportProviderIF
{
  public static final String CHILDREN     = "1";

  public static final String GRANDKIDS    = "2";

  public static final String SIBLINGS     = "3";

  public static final String COUSINS      = "4";

  public static final String PARENTS      = "5";

  public static final String GRANDPARENTS = "6";

  public static final String NONE         = "7";

  public static final String TYPE         = "8";

  protected void addSelectables(GeneratedBusinessQuery query, List<ReportAttributeMetadata> attributes, ValueQuery vQuery)
  {
    MdEntityDAOIF mdClass = query.getMdClassIF();

    for (ReportAttributeMetadata attribute : attributes)
    {
      String attributeName = attribute.getAttributeName();

      SelectablePrimitive selectable = this.getSelectable(vQuery, query, attribute);
      selectable.setColumnAlias(attributeName);
      selectable.setUserDefinedAlias(attributeName);
      selectable.setUserDefinedDisplayLabel(mdClass.definesAttribute(attributeName).getDisplayLabel(Session.getCurrentLocale()));

      vQuery.SELECT(selectable);

      if (attribute.isOrderBy())
      {
        vQuery.ORDER_BY_DESC(selectable);
      }
    }
  }

  protected SelectablePrimitive getSelectable(ValueQuery vQuery, GeneratedBusinessQuery query, ReportAttributeMetadata attribute)
  {
    Attribute selectable = query.get(attribute.getAttributeName());

    if (selectable instanceof SelectableReference)
    {
      MdAttributeReferenceDAOIF mdAttribute = (MdAttributeReferenceDAOIF) selectable.getMdAttributeIF();

      if (mdAttribute.getReferenceMdBusinessDAO().definesType().equals(Classifier.CLASS))
      {
        AttributeReference reference = (AttributeReference) selectable;
        ClassifierQuery cQuery = new ClassifierQuery(vQuery);

        vQuery.WHERE(reference.LEFT_JOIN_EQ(cQuery));

        return cQuery.getDisplayLabel().localize();
      }
    }
    else if (attribute.getAggregation() != null && attribute.getAggregation().equals(AllAggregationType.SUM))
    {
      return F.SUM(selectable);
    }

    return (SelectablePrimitive) selectable;
  }

  protected boolean hasDepth(GeoEntity geoEntity, int minDepth)
  {
    Term root = Universal.getRoot();
    Term universal = geoEntity.getUniversal();

    int depth = this.getDepth(root, universal, 0);

    return ( depth > minDepth );
  }

  protected int getDepth(Term dest, Term source, int depth)
  {

    if (dest.getId().equals(source.getId()))
    {
      return depth;
    }

    int parentDepth = Integer.MAX_VALUE;

    List<Term> parents = source.getDirectAncestors(AllowedIn.CLASS).getAll();

    for (Term parent : parents)
    {
      parentDepth = Math.min(parentDepth, this.getDepth(dest, parent, depth + 1));
    }

    return parentDepth;
  }

  protected void addGeoEntityQuery(ValueQuery vQuery, String aggregation, GeoEntity geoEntity, GeoEntityQueryReferenceIF geoAttribute)
  {
    GeoEntityQuery entityQuery = new GeoEntityQuery(vQuery);

    MdAttributeConcreteDAOIF mdAttribute = geoAttribute.getMdAttributeIF();

    Coalesce geoLocation = entityQuery.getDisplayLabel().localize();
    geoLocation.setColumnAlias(mdAttribute.definesAttribute());
    geoLocation.setUserDefinedAlias(mdAttribute.definesAttribute());
    geoLocation.setUserDefinedDisplayLabel(mdAttribute.getDisplayLabel(Session.getCurrentLocale()));

    SelectableChar geoId = entityQuery.getGeoId();

    vQuery.SELECT(geoLocation, geoId);

    if (aggregation != null && aggregation.equals(COUSINS))
    {
      // cousins
      LocatedInQuery parentQuery = new LocatedInQuery(vQuery);
      LocatedInQuery grandParentQuery = new LocatedInQuery(vQuery);
      LocatedInQuery uncleQuery = new LocatedInQuery(vQuery);
      LocatedInQuery cuzQuery = new LocatedInQuery(vQuery);
      GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(vQuery);

      vQuery.WHERE(parentQuery.getChild().EQ(geoEntity));
      vQuery.AND(grandParentQuery.getChild().EQ(parentQuery.getParent()));
      vQuery.AND(uncleQuery.getParent().EQ(grandParentQuery.getParent()));
      vQuery.AND(cuzQuery.getParent().EQ(uncleQuery.getChild()));
      vQuery.AND(aptQuery.getParentTerm().EQ(cuzQuery.getChild()));
      vQuery.AND(entityQuery.EQ(aptQuery.getParentTerm()));
      vQuery.AND(geoAttribute.EQ(aptQuery.getChildTerm()));

      this.addGeoLabel(vQuery, "parent", parentQuery.getParent());
      this.addGeoLabel(vQuery, "grandparent", grandParentQuery.getParent());
    }
    else if (aggregation != null && aggregation.equals(SIBLINGS))
    {
      // Siblings
      LocatedInQuery parentQuery = new LocatedInQuery(vQuery);
      LocatedInQuery siblingQuery = new LocatedInQuery(vQuery);
      GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(vQuery);

      vQuery.WHERE(parentQuery.getChild().EQ(geoEntity));
      vQuery.AND(siblingQuery.getParent().EQ(parentQuery.getParent()));
      vQuery.AND(aptQuery.getParentTerm().EQ(siblingQuery.getChild()));
      vQuery.AND(entityQuery.EQ(aptQuery.getParentTerm()));
      vQuery.AND(geoAttribute.EQ(aptQuery.getChildTerm()));

      this.addGeoLabel(vQuery, "parent", parentQuery.getParent());
    }
    else if (aggregation != null && aggregation.equals(CHILDREN))
    {
      // Kids
      LocatedInQuery kidsQuery = new LocatedInQuery(vQuery);
      GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(vQuery);

      vQuery.WHERE(kidsQuery.getParent().EQ(geoEntity));
      vQuery.AND(aptQuery.getParentTerm().EQ(kidsQuery.getChild()));
      vQuery.AND(entityQuery.EQ(aptQuery.getParentTerm()));
      vQuery.AND(geoAttribute.EQ(aptQuery.getChildTerm()));
    }
    else if (aggregation != null && aggregation.equals(PARENTS) && this.hasDepth(geoEntity, 1))
    {
      LocatedInQuery lQuery = new LocatedInQuery(vQuery);
      GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(vQuery);

      vQuery.WHERE(lQuery.getChild().EQ(geoEntity));
      vQuery.AND(aptQuery.getParentTerm().EQ(lQuery.getParent()));
      vQuery.AND(entityQuery.EQ(aptQuery.getParentTerm()));
      vQuery.AND(geoAttribute.EQ(aptQuery.getChildTerm()));
    }
    else if (aggregation != null && aggregation.equals(GRANDPARENTS) && this.hasDepth(geoEntity, 2))
    {
      LocatedInQuery lQuery1 = new LocatedInQuery(vQuery);
      LocatedInQuery lQuery2 = new LocatedInQuery(vQuery);
      GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(vQuery);

      vQuery.WHERE(lQuery1.getChild().EQ(geoEntity));
      vQuery.AND(lQuery2.getChild().EQ(lQuery1.getParent()));
      vQuery.AND(aptQuery.getParentTerm().EQ(lQuery2.getParent()));
      vQuery.AND(entityQuery.EQ(aptQuery.getParentTerm()));
      vQuery.AND(geoAttribute.EQ(aptQuery.getChildTerm()));
    }
    else if (aggregation != null && aggregation.equals(TYPE))
    {
      GeoEntityQuery gQuery = new GeoEntityQuery(vQuery);

      GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(vQuery);

      vQuery.WHERE(gQuery.getUniversal().EQ(geoEntity.getUniversal()));
      vQuery.AND(aptQuery.getParentTerm().EQ(gQuery));
      vQuery.AND(entityQuery.EQ(aptQuery.getParentTerm()));
      vQuery.AND(geoAttribute.EQ(aptQuery.getChildTerm()));

      this.addUniversalLabel(vQuery, "universal", gQuery.getUniversal());
    }
    else
    {
      // Direct
      GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(vQuery);

      vQuery.WHERE(aptQuery.getParentTerm().EQ(geoEntity));
      vQuery.AND(entityQuery.EQ(aptQuery.getParentTerm()));
      vQuery.AND(geoAttribute.EQ(aptQuery.getChildTerm()));
    }
  }

  private void addGeoLabel(ValueQuery vQuery, String suffix, SelectableReference parent)
  {
    GeoEntityQuery parentEntityQuery = new GeoEntityQuery(vQuery);

    Coalesce parentLocation = parentEntityQuery.getDisplayLabel().localize();
    parentLocation.setColumnAlias(suffix + "Label");
    parentLocation.setUserDefinedAlias(suffix + "Label");
    parentLocation.setUserDefinedDisplayLabel(LocalizationFacade.getFromBundles(suffix + "Label"));

    SelectableChar parentGeoId = parentEntityQuery.getGeoId();
    parentGeoId.setColumnAlias(suffix + "GeoId");
    parentGeoId.setUserDefinedAlias(suffix + "GeoId");
    parentGeoId.setUserDefinedDisplayLabel(LocalizationFacade.getFromBundles(suffix + "GeoId"));

    vQuery.SELECT(parentLocation, parentGeoId);
    vQuery.AND(parentEntityQuery.EQ(parent));
  }

  private void addUniversalLabel(ValueQuery vQuery, String suffix, SelectableReference parent)
  {
    UniversalQuery universalQuery = new UniversalQuery(vQuery);

    Coalesce parentLocation = universalQuery.getDisplayLabel().localize();
    parentLocation.setColumnAlias(suffix + "Label");
    parentLocation.setUserDefinedAlias(suffix + "Label");
    parentLocation.setUserDefinedDisplayLabel(LocalizationFacade.getFromBundles(suffix + "Label"));

    vQuery.SELECT(parentLocation);
    vQuery.AND(universalQuery.EQ(parent));
  }

  @Override
  public PairView[] getSupportedAggregation()
  {
    List<PairView> list = new LinkedList<PairView>();

    list.add(PairView.create(NONE, "NONE"));
    list.add(PairView.create(CHILDREN, "CHILDREN"));
    list.add(PairView.create(GRANDKIDS, "GRANDKIDS"));
    list.add(PairView.create(PARENTS, "PARENTS"));
    list.add(PairView.create(GRANDPARENTS, "GRANDPARENTS"));
    list.add(PairView.create(SIBLINGS, "SIBLINGS"));
    list.add(PairView.create(COUSINS, "COUSINS"));
    list.add(PairView.create(TYPE, "TYPE"));

    return list.toArray(new PairView[list.size()]);
  }

}
