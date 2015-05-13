package com.runwaysdk.geodashboard;

import java.sql.Savepoint;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.LocatedInQuery;
import com.runwaysdk.system.gis.geo.Synonym;
import com.runwaysdk.system.gis.geo.SynonymRelationship;
import com.runwaysdk.system.gis.geo.SynonymRelationshipQuery;

public class GeoEntityUtil extends GeoEntityUtilBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -395452858;

  public GeoEntityUtil()
  {
    super();
  }

  /**
   * Merges the source geo entity into the destination geo entity and creates a new synonym with the name of the source
   * geo entity.
   * 
   * @param sourceId
   * @param destinationId
   * 
   * @return Returns a list of all geo entity ids which may need to be refreshed in the browser
   */
  @Transaction
  public static String[] makeSynonym(String sourceId, String destinationId)
  {
    GeoEntity source = GeoEntity.get(sourceId);
    GeoEntity destination = GeoEntity.get(destinationId);

    LocatedInQuery query = new LocatedInQuery(new QueryFactory());
    query.WHERE(query.getChild().EQ(source));

    OIterator<? extends LocatedIn> iterator = query.getIterator();

    List<String> ids = new LinkedList<String>();

    try
    {
      while (iterator.hasNext())
      {
        LocatedIn locatedIn = iterator.next();

        ids.add(locatedIn.getParentId());
      }
    }
    finally
    {
      iterator.close();
    }

    makeSynonym(source, destination);

    ids.add(destinationId);

    return ids.toArray(new String[ids.size()]);
  }

  public static Synonym makeSynonym(GeoEntity source, GeoEntity destination)
  {
    // Copy over all synonyms
    SynonymRelationshipQuery query = new SynonymRelationshipQuery(new QueryFactory());
    query.WHERE(query.getParent().EQ(source));

    OIterator<? extends SynonymRelationship> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        SynonymRelationship sRelationship = it.next();

        Synonym sSynonymn = sRelationship.getChild();
        String synonymName = sSynonymn.getDisplayLabel().getValue();

        createSynonym(destination, synonymName);

        sSynonymn.delete();
      }
    }
    finally
    {
      it.close();
    }

    // Copy over any synonyms to the destination and delete the originals
    BusinessDAOIF sourceDAO = (BusinessDAOIF) BusinessFacade.getEntityDAO(source);

    BusinessDAOFactory.floatObjectIdReferences(sourceDAO.getBusinessDAO(), source.getId(), destination.getId(), true);

    Synonym synonym = createSynonym(destination, source.getDisplayLabel().getValue());

    source.delete();

    return synonym;
  }

  private static Synonym createSynonym(GeoEntity destination, String synonymName)
  {
    Savepoint savepoint = Database.setSavepoint();

    try
    {
      Synonym synonym = new Synonym();
      synonym.getDisplayLabel().setValue(synonymName);

      Synonym.create(synonym, destination.getId());

      return synonym;
    }
    catch (DuplicateDataException e)
    {
      // Rollback the savepoint
      Database.rollbackSavepoint(savepoint);

      savepoint = null;
    }
    finally
    {
      if (savepoint != null)
      {
        Database.releaseSavepoint(savepoint);
      }
    }

    return null;
  }
}
