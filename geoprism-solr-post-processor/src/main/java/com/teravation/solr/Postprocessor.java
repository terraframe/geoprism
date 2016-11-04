package com.teravation.solr;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

/**
 * @author chris
 *
 *         This is the postprocessor for Solr documents to annotate them with geotags based on terms recognized from the
 *         Runway geotree. It grabs a set of documents that are not already processed, reads them in, and then reads the
 *         desired text field and annotates it with geotags. It then updates the text field text with the annotated
 *         version and sets a flag to indicate that this document has been processed.
 */
public class Postprocessor
{
  // Default URL of the Solr instance to process
  public static String SOLR_URL                 = "http://localhost:8983/solr/demo";

  // Default name of the boolean field that acts as the flag for processing or not
  public static String SOLR_PROCESSED_FIELDNAME = "postprocessed";

  // Default maximum number of records to process during this pass
  public static int    SOLR_MAX_RECORD_COUNT    = 100;

  // Default name of the text field that contains the textual data to process
  public static String SOLR_TEXT_FIELDNAME      = "text";

  // Default filename containing the GeoTree information
  public static String GEOTREE_FILENAME         = "/home/terraframe/git/geoprism/geoprism-solr-post-processor/src/main/resources/GeoTree.csv";

  // Default name of the id field in these documents
  public static String SOLR_ID_FIELDNAME        = "id";

  // Default limit for number of tokens
  public static int    TOKEN_LIMIT              = 10;

  /**
   * Runway Geotree interface
   */
  private GeoTree      geoTree;

  /**
   * Number of records processed in this pass
   */
  private int          recordsProcessed;

  /**
   * Previous location id
   */
  private String       previous;

  /**
   * Counter for the number of tokens since a previous location token was discovered. Used to limit if a previous
   * location token is still valid.
   */
  private int          counter;

  public Postprocessor()
  {
    this.geoTree = new GeoTree();
    this.previous = null;
    this.counter = 0;
    this.recordsProcessed = 0;
  }

  /**
   * This is the method to process a set of documents (invoked by main() or programmatically. It grabs a set of
   * documents that are not already processed, reads them in, and then reads the desired text field and annotates it
   * with geotags. It then updates the text field text with the annotated version and sets a flag to indicate that this
   * document has been processed.
   * 
   * @param solrUrl
   *          URL of the Solr instance to process
   * @param processedFieldName
   *          Name of the boolean field that acts as the flag for processing or not
   * @param rows
   *          The maximum number of records to process during this pass
   * @param textFieldName
   *          Name of the text field that contains the textual data to process
   * @param geodataFileName
   *          Filename containing the GeoTree information
   */
  public void processDocuments(String solrUrl, String processedFieldName, int rows, String textFieldName, String geodataFileName)
  {
    // Load the GeoTree data from CSV
    try
    {
      geoTree.loadData(geodataFileName);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return;
    }

    this.processDocument(solrUrl, processedFieldName, rows, textFieldName);
  }

  public void processDocuments(String solrUrl, String processedFieldName, int rows, String textFieldName, GeoTree geoTree)
  {
    this.geoTree = geoTree;

    this.processDocument(solrUrl, processedFieldName, rows, textFieldName);
  }

  private void processDocument(String solrUrl, String processedFieldName, int rows, String textFieldName)
  {
    // Create the client
    SolrClient solr = new HttpSolrClient.Builder(solrUrl).build();

    // Create the query (format is -field:true so that we get docs without field and docs where field:false)
    SolrQuery query = new SolrQuery();
    query.setQuery("-" + processedFieldName + ":true");

    if (rows != -1)
    {
      query.setRows(rows);
    }

    // Run the query and process the results
    try
    {
      QueryResponse response = solr.query(query);
      SolrDocumentList list = response.getResults();
      for (SolrDocument doc : list)
      {
        recordsProcessed++;
        // Process the document to get the new value of the field (with annotations)
        String newValue = this.processDocument(doc, textFieldName);
        // Create a new partial update document
        SolrInputDocument updateDoc = this.getUpdateDoc(doc);
        // Set the new values for the text field and the boolean flag field
        this.updateField(updateDoc, "set", textFieldName, newValue);
        this.updateField(updateDoc, "set", processedFieldName, true);
        // Update the document and commit
        solr.add(updateDoc);
        solr.commit();
      }
    }
    catch (SolrServerException | IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      try
      {
        if (solr != null)
        {
          solr.close();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }

    }
  }

  /**
   * Process a single document from Solr
   * 
   * @param doc
   *          The Solr document
   * @param fieldName
   *          The fieldname of the text data to process
   * @return The new (annotated) value of the specified text field
   */
  private String processDocument(SolrDocument doc, String fieldName)
  {
    // Check to make sure that document contains the specified field
    // If so, process it
    if (doc.containsKey(fieldName))
    {
      Object fieldValue = doc.getFieldValue(fieldName);
      return this.processField(doc, fieldName, fieldValue);
    }

    System.out.println("No field " + fieldName + " in document " + doc.getFieldValue(SOLR_ID_FIELDNAME));
    return null;
  }

  /**
   * Process the correct text field and its value from the Solr document. Since fields can be multivalued, first see if
   * the field is an object or a list. If it is a String (text field), process it. If it's a list, recurse back in with
   * just the first object in the list. Otherwise, log an error.
   * 
   * @param doc
   *          The Solr document
   * @param fieldName
   *          The fieldname of the text data to process
   * @param fieldValue
   *          The value of the text data to process
   * @return The new (annotated) value
   */
  private String processField(SolrDocument doc, String fieldName, Object fieldValue)
  {
    String returnValue = null;
    // If it's a string (single value), process it
    if (fieldValue instanceof String)
    {
      returnValue = this.processFieldValue((String) fieldValue);
      // Otherwise, if it's a list with a single value, recurse in with only the first value from the list
    }
    else if (fieldValue instanceof ArrayList)
    {
      @SuppressWarnings("unchecked")
      ArrayList<Object> fieldValueList = (ArrayList<Object>) fieldValue;
      if (fieldValueList.size() > 1)
      {
        // More than one value in the multiple value list, so log an error
        System.out.println("ERROR: FIELD " + fieldName + " in document " + doc.getFieldValue(SOLR_ID_FIELDNAME) + " is multivalued");
      }
      else
      {
        // Only one value in the list, so process that value.
        returnValue = this.processField(doc, fieldName, fieldValueList.get(0));
      }
      // If it's something else, log an error
    }
    else
    {
      System.out.println("ERROR: FIELD " + fieldName + " in document " + doc.getFieldValue(SOLR_ID_FIELDNAME) + " is " + fieldValue.getClass() + ", not String");
    }
    return returnValue;
  }

  /**
   * Process the text in the field. Tokenize the data in the field, and then process each token individually. For each
   * one, see if there is a corresponding entry in the GeoTree: if so, annotate the text.
   * 
   * @param fieldValue
   *          The value of the text data to process
   * @return The new (annotated) value
   */
  private String processFieldValue(String fieldValue)
  {
    // Current offset of our processing
    int currentOffset = 0;
    // Output stringbuffer
    StringBuffer sb = new StringBuffer();
    System.out.println("Processing   " + fieldValue);

    // Create a new tokenizer on the field data
    Tokenizer tokenizer = new ClassicTokenizer();
    tokenizer.setReader(new StringReader(fieldValue));
    try
    {
      // Reset the tokenizer so we can start
      tokenizer.reset();
      // Character attribute of the tokenizer (so we can see the token value)
      CharTermAttribute term = tokenizer.addAttribute(CharTermAttribute.class);
      // Offset attribute of the tokenizer (so we can get the offset of the token)
      OffsetAttribute offset = tokenizer.addAttribute(OffsetAttribute.class);
      // Process each token
      while (tokenizer.incrementToken())
      {
        // Get the token text.
        String token = term.toString();
        // Copy everything prior to this token (i.e. leading "whitespace") to the buffer
        if (offset.startOffset() != currentOffset)
        {
          sb.append(fieldValue.substring(currentOffset, offset.startOffset()));
        }
        // Process this token to see if we need to annotate it
        sb.append(this.processToken(token));
        // Set the current processed offset to the end of the token
        currentOffset = offset.endOffset();
      }

      // Copy any trailing "whitespace" at the end of the text field
      if (currentOffset != fieldValue.length())
      {
        sb.append(fieldValue.substring(currentOffset, fieldValue.length()));
      }
      // End tokenization
      tokenizer.end();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (tokenizer != null)
      {
        try
        {
          tokenizer.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }

    System.out.println("Replacing w/ " + sb.toString());
    
    return sb.toString();
  }

  /**
   * Process this token. If there is a geotree match, return the token with the geotag annotation...otherwise just
   * return the token
   * 
   * @param token
   *          The input token text
   * @return The annotated (if necessary) token text
   */
  private String processToken(String token)
  {
    if (this.counter > TOKEN_LIMIT)
    {
      this.previous = null;
      this.counter = 0;
    }

    // See if there is a match in the GeoTree
    GeoData gd = geoTree.get(token);

    if (gd != null)
    {
      String locationId = gd.getLocationId(this.previous);

      if (locationId != null)
      {
        /*
         * Update the previous location id to the newly discovered location and reset the counter so that it is valid
         */
        this.previous = locationId;
        this.counter = 0;

        // If so, annotate
        return ( token + " " + "{{" + locationId + "}}" );
      }
    }

    if (this.counter < TOKEN_LIMIT)
    {
      this.counter++;
    }

    // Otherwise, return the token by itself
    return token;
  }

  /**
   * Utility method to return an editable SolrInputDocument shell from an existing read-only SolrDocument.
   * 
   * @param doc
   *          The read-only SolrDocument
   * @return The editable SolrDocument shell
   */
  private SolrInputDocument getUpdateDoc(SolrDocument doc)
  {
    // Create a new editable SolrInputDocument
    SolrInputDocument updateDoc = new SolrInputDocument();
    // Copy the id from the SolrDocument into the SolrInputDocument
    updateDoc.addField(SOLR_ID_FIELDNAME, (String) doc.get(SOLR_ID_FIELDNAME));
    return updateDoc;
  }

  /**
   * Perform a "partial update" on the given document, using the given operation on the specified field and value. See
   * https://cwiki.apache.org/confluence/display/solr/Updating+Parts+of+Documents
   * 
   * @param doc
   * @param op
   * @param field
   * @param value
   */
  private void updateField(SolrInputDocument doc, String op, String field, Object value)
  {
    // Create the partial update map
    Map<String, Object> update = new HashMap<String, Object>();
    // Set the key to the operation, and the value to the new value
    update.put(op, value);
    // Add the partial update map to the document as the given field
    doc.addField(field, update);
  }

  /**
   * @return The number of records processed in this pass
   */
  private int getRecordsProcessed()
  {
    return this.recordsProcessed;
  }

  /**
   * This is the main method for the Preprocessor (to be run from the command line). It requires 5 arguments: SOLR_URL =
   * URL of the Solr instance to process SOLR_PROCESSED_FIELDNAME = Name of the boolean field that acts as the flag for
   * processing or not SOLR_MAX_RECORD_COUNT = The maximum number of records to process during this pass
   * SOLR_TEXT_FIELDNAME = Name of the text field that contains the textual data to process
   * 
   * @param args
   */
  public static void main(String args[])
  {
    System.out.println("Starting postprocessing...");
    Postprocessor p = new Postprocessor();
    if (args.length == 0)
    {
      // Print out usage
      System.out.println("args:  SOLR_URL SOLR_PROCESSED_FIELDNAME SOLR_MAX_RECORD_COUNT SOLR_TEXT_FIELDNAME GEOTREE_FILENAME");
      System.out.println();
      System.out.println("    SOLR_URL = URL of the Solr instance to process (e.g. " + SOLR_URL + ")");
      System.out.println("    SOLR_PROCESSED_FIELDNAME = Name of the boolean field that acts as the flag for processing or not (e.g. " + SOLR_PROCESSED_FIELDNAME + ")");
      System.out.println("    SOLR_MAX_RECORD_COUNT = The maximum number of records to process during this pass (e.g. " + SOLR_MAX_RECORD_COUNT + ")");
      System.out.println("    SOLR_TEXT_FIELDNAME = Name of the text field that contains the textual data to process (e.g. " + SOLR_TEXT_FIELDNAME + ")");
      System.out.println("    GEOTREE_FILENAME = Filename containing the GeoTree information (e.g. " + GEOTREE_FILENAME + ")");

      // Run with default arguments (for dev/testing)
      p.processDocuments(SOLR_URL, SOLR_PROCESSED_FIELDNAME, SOLR_MAX_RECORD_COUNT, SOLR_TEXT_FIELDNAME, GEOTREE_FILENAME);
    }
    else
    {
      p.processDocuments(args[0], args[1], Integer.parseInt(args[2]), args[3], args[4]);
    }
    System.out.println("Postprocessing completed.  " + p.getRecordsProcessed() + " record(s) processed");
  }
}
