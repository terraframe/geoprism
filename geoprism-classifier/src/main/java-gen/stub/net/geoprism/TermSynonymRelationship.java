/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
package net.geoprism;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.ClassAttributeQuery;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdBusinessQuery;

public class TermSynonymRelationship extends TermSynonymRelationshipBase 
{
  private static final long serialVersionUID = 798149257;

  public TermSynonymRelationship(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }

  public TermSynonymRelationship(com.runwaysdk.system.metadata.MdAttributeReference parent, com.runwaysdk.system.metadata.MdAttributeReference child)
  {
    this(parent.getOid(), child.getOid());
  }

  public PreparedStatement buildSynonymUpdateStatement(MdBusiness mdBusiness, Term term, String synonymId)
  {
    MdAttributeReference termAttribute = this.getParent();
    MdAttributeReference synonymAttribute = this.getChild();

    String sql = "UPDATE " + mdBusiness.getTableName();
    sql += " SET " + synonymAttribute.getColumnName() + " = ?";
    sql += " WHERE " + termAttribute.getColumnName() + " = ?";
    sql += " AND " + synonymAttribute.getColumnName() + " IS NULL";

    Connection conn = Database.getConnection();

    try
    {
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setString(1, synonymId);
      statement.setString(2, term.getOid());

      return statement;
    }
    catch (SQLException e)
    {
      throw new DatabaseException(e);
    }
  }

  public PreparedStatement buildClassifierUpdateStatement(MdBusiness mdBusiness, String synonymId, Term term)
  {
    MdAttributeReference termAttribute = this.getParent();
    MdAttributeReference synonymAttribute = this.getChild();

    String sql = "UPDATE " + mdBusiness.getTableName();
    sql += " SET " + termAttribute.getColumnName() + " = ?";
    sql += " WHERE " + synonymAttribute.getColumnName() + " = ?";

    Connection conn = Database.getConnection();

    try
    {
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setString(1, term.getOid());
      statement.setString(2, synonymId);

      return statement;
    }
    catch (SQLException e)
    {
      throw new DatabaseException(e);
    }
  }

  public static List<MdBusiness> getMdBusiness(String referenceType)
  {
    MdBusiness mdBusiness = MdBusiness.getMdBusiness(referenceType);

    QueryFactory factory = new QueryFactory();

    TermSynonymRelationshipQuery termSynonymQuery = new TermSynonymRelationshipQuery(factory);
    termSynonymQuery.WHERE(termSynonymQuery.getParent().aReference(MdAttributeReferenceInfo.REF_MD_ENTITY).EQ(mdBusiness));

    ClassAttributeQuery classAttributeQuery = new ClassAttributeQuery(factory);
    classAttributeQuery.WHERE(classAttributeQuery.childOid().EQ(termSynonymQuery.parentOid()));

    MdBusinessQuery mdBusinessQuery = new MdBusinessQuery(factory);
    mdBusinessQuery.WHERE(mdBusinessQuery.attribute(classAttributeQuery));

    OIterator<? extends MdBusiness> iterator = null;

    try
    {
      iterator = mdBusinessQuery.getIterator();

      return new LinkedList<MdBusiness>(iterator.getAll());
    }
    finally
    {
      if (iterator != null)
      {
        iterator.close();
      }
    }
  }

  public static List<TermSynonymRelationship> getRelationships(MdBusiness mdBusiness, String referenceType)
  {
    MdBusiness referenceBusiness = MdBusiness.getMdBusiness(referenceType);

    QueryFactory factory = new QueryFactory();

    ClassAttributeQuery classAttributeQuery = new ClassAttributeQuery(factory);
    classAttributeQuery.WHERE(classAttributeQuery.parentOid().EQ(mdBusiness.getOid()));

    TermSynonymRelationshipQuery termSynonymQuery = new TermSynonymRelationshipQuery(factory);
    termSynonymQuery.WHERE(termSynonymQuery.getParent().aReference(MdAttributeReferenceInfo.REF_MD_ENTITY).EQ(referenceBusiness));
    termSynonymQuery.AND(termSynonymQuery.parentOid().EQ(classAttributeQuery.childOid()));

    OIterator<? extends TermSynonymRelationship> iterator = null;

    try
    {
      iterator = termSynonymQuery.getIterator();

      return new LinkedList<TermSynonymRelationship>(iterator.getAll());
    }
    finally
    {
      if (iterator != null)
      {
        iterator.close();
      }
    }
  }

  public static void logSynonymData(Term term, String synonymId, String termType)
  {
    List<MdBusiness> mdBusinesses = TermSynonymRelationship.getMdBusiness(termType);

    for (MdBusiness mdBusiness : mdBusinesses)
    {
      List<TermSynonymRelationship> relationships = TermSynonymRelationship.getRelationships(mdBusiness, termType);

      for (TermSynonymRelationship relationship : relationships)
      {
        PreparedStatement statement = relationship.buildSynonymUpdateStatement(mdBusiness, term, synonymId);

        List<PreparedStatement> statements = new LinkedList<PreparedStatement>();
        statements.add(statement);

        Database.executeStatementBatch(statements);
      }
    }
  }

  public static void restoreSynonymData(Term term, String synonymId, String termType)
  {
    List<MdBusiness> mdBusinesses = TermSynonymRelationship.getMdBusiness(termType);

    for (MdBusiness mdBusiness : mdBusinesses)
    {
      List<TermSynonymRelationship> relationships = TermSynonymRelationship.getRelationships(mdBusiness, termType);

      for (TermSynonymRelationship relationship : relationships)
      {
        PreparedStatement statement = relationship.buildClassifierUpdateStatement(mdBusiness, synonymId, term);

        List<PreparedStatement> statements = new LinkedList<PreparedStatement>();
        statements.add(statement);

        Database.executeStatementBatch(statements);
      }
    }
  }

}
