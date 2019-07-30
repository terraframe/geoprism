/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.report;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;

import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.query.AttributeLocal;
import com.runwaysdk.query.Coalesce;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableReference;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdClass;

import net.geoprism.QueryUtil;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierQuery;

public class GenericTypeProvider extends AbstractProvider implements ReportProviderIF
{
  @Override
  public List<PairView> getSupportedQueryDescriptors()
  {
    List<MdClass> mdClasses = GenericTypeProviderCache.getInstance().getMdClassesWithGeoNodes();

    List<PairView> list = new LinkedList<PairView>();

    for (MdClass mdClass : mdClasses)
    {
      String type = mdClass.definesType();
      String label = mdClass.getDisplayLabel().getValue();

      list.add(PairView.createWithLabel(type, label));
    }

    return list;
  }

  @Override
  public List<GeoNode> getSupportedGeoNodes(String queryId)
  {
    /*
     * We are making the assumption that the queryId is type
     */

    return GenericTypeProviderCache.getInstance().getGeoNodes(queryId);
  }

  @Override
  public ValueQuery getReportQuery(String queryId, String context) throws JSONException
  {
    QueryConfiguration config = new QueryConfiguration(queryId, context);

    /*
     * We are making the assumption that the queryId is type
     */
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(queryId);

    GeneratedComponentQuery query = QueryUtil.getQuery(mdClass, config.getFactory());

    this.addSelectables(config, query);

    // For now we are assuming there is only one geo node per type
    SelectableReference geoEntityAttribute = this.getGeoEntityAttribute(config, query);

    this.addLocationQuery(config, geoEntityAttribute, query);

    ReportProviderUtil.addConditions(config, queryId, query);

    /*
     * IMPORTANT: All results must be ordered for blocking to work
     */
    Selectable oid = query.get(MdClassInfo.OID);

    ValueQuery vQuery = config.getValueQuery();
    vQuery.SELECT(oid);
    vQuery.ORDER_BY_ASC(oid);

    return vQuery;
  }

  private SelectableReference getGeoEntityAttribute(QueryConfiguration config, GeneratedComponentQuery query)
  {
    String geoNodeId = config.getGeoNodeId();
    GeoNode node = GeoNode.get(geoNodeId);

    MdAttributeReference mdAttributeGeoEntity = node.getGeoEntityAttribute();
    String attributeName = mdAttributeGeoEntity.getAttributeName();

    SelectableReference attribute = (SelectableReference) query.get(attributeName);

    return attribute;
  }

  private void addSelectables(QueryConfiguration config, GeneratedComponentQuery query)
  {
    ValueQuery vQuery = config.getValueQuery();

    Locale locale = Session.getCurrentLocale();
    MdClassDAOIF mdClass = query.getMdClassIF();

    List<? extends MdAttributeDAOIF> mdAttributes = mdClass.getAllDefinedMdAttributes();

    for (MdAttributeDAOIF mdAttribute : mdAttributes)
    {
      if (!mdAttribute.isSystem())
      {
        String attributeName = mdAttribute.definesAttribute();
        String displayLabel = mdAttribute.getDisplayLabel(locale);

        Selectable selectable = query.get(attributeName);

        MdAttributeConcreteDAOIF mdAttributeConcrete = mdAttribute.getMdAttributeConcrete();

        if (mdAttributeConcrete instanceof MdAttributeReferenceDAOIF)
        {
          SelectableReference selectableReference = (SelectableReference) selectable;

          MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttributeConcrete;
          MdBusinessDAOIF referenceMdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();
          String referenceType = referenceMdBusiness.definesType();

          if (referenceType.equals(Classifier.CLASS))
          {
            /*
             * If the selectable is referencing a Classifier then get the display label of the classifier
             */
            ClassifierQuery classifierQuery = new ClassifierQuery(vQuery);

            Coalesce label = classifierQuery.getDisplayLabel().localize(attributeName, displayLabel);
            label.setUserDefinedDisplayLabel(displayLabel);

            vQuery.SELECT(label);
            vQuery.WHERE(selectableReference.LEFT_JOIN_EQ(classifierQuery));
          }
        }
        else if (mdAttributeConcrete instanceof MdAttributeLocalDAOIF)
        {
          AttributeLocal selectableLocal = (AttributeLocal) selectable;

          /*
           * If the selectable is a localized attribute then get the localize label
           */
          Coalesce label = selectableLocal.localize(attributeName, displayLabel);
          label.setUserDefinedAlias(attributeName);

          vQuery.SELECT(label);
        }
        else if (mdAttributeConcrete instanceof MdAttributeIndicatorDAOIF)
        {
          // Indicators are not supported
        }        
        else if (mdAttributeConcrete instanceof MdAttributeEnumerationDAOIF)
        {
          // Enumerations are not supported
        }
        else if (mdAttributeConcrete instanceof MdAttributeGeometryDAOIF)
        {
          // Geometry attributes not supported
        }
        else
        {
          selectable.setColumnAlias(attributeName);
          selectable.setUserDefinedAlias(attributeName);
          selectable.setUserDefinedDisplayLabel(displayLabel);

          vQuery.SELECT(selectable);
        }
      }
    }
  }
}
