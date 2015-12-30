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
package com.runwaysdk.geodashboard.ontology;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.MessageDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class ClassifierSynonymTest 
{
//  private static Stack<Classifier> classifierStack = new Stack<Classifier>();
//
//  private static Map<String, String> classifierIdMap = new HashMap<String, String>();
//  
//  private static String          label = "default";
//  
//  private static ClientSession   systemSession;
//  
//  
//
//  @BeforeClass
//  public static void classSetup() throws Exception
//  {
//    classSetupRequest();
//   
//    systemSession = ClientSession.createUserSession(label, ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
//  }
//
//  @Request
//  private static void classSetupRequest()
//  {    
//    LocalProperties.setSkipCodeGenAndCompile(true);
//    
//    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
//    
//    classSetupTransaction();
//  }
//  
//  @Transaction
//  private static void classSetupTransaction()
//  {    
//    Classifier a1 = createClassifier("a1");
//    classifierIdMap.put("a1", a1.getId());
//    classifierStack.add(a1);
//    
//    Classifier b1 = createClassifier("b1");
//    classifierIdMap.put("b1", b1.getId());
//    b1.addLink(a1, ClassifierIsARelationship.CLASS).apply();
//    classifierStack.add(b1);
//    
//    Classifier b1c1 = createClassifier("b1c1");
//    classifierIdMap.put("b1c1", b1c1.getId());
//    b1c1.addLink(b1, ClassifierIsARelationship.CLASS).apply();
//    classifierStack.add(b1c1);    
//    
//    Classifier b1c2 = createClassifier("b1c2");
//    classifierIdMap.put("b1c2", b1c2.getId());
//    b1c2.addLink(b1, ClassifierIsARelationship.CLASS).apply();
//    classifierStack.add(b1c2);  
//    
//    Classifier b2 = createClassifier("b2");
//    classifierIdMap.put("b2", b2.getId());
//    b2.addLink(a1, ClassifierIsARelationship.CLASS).apply();
//    classifierStack.add(b2);
//    
//    Classifier b2c1 = createClassifier("b2c1");
//    classifierIdMap.put("b2c1", b2c1.getId());
//    b2c1.addLink(b2, ClassifierIsARelationship.CLASS).apply();
//    classifierStack.add(b2c1);   
//
//    Classifier b2c2 = createClassifier("b2c2");
//    classifierIdMap.put("b2c2", b2c2.getId());
//    b2c2.addLink(b2, ClassifierIsARelationship.CLASS).apply();
//    classifierStack.add(b2c2);  
//    
//    Classifier b3 = createClassifier("b3");
//    classifierIdMap.put("b3", b3.getId());
//    b3.addLink(a1, ClassifierIsARelationship.CLASS).apply();
//    classifierStack.add(b3);
//    
//    Classifier b3c1 = createClassifier("b3c1");
//    classifierIdMap.put("b3c1", b3c1.getId());
//    b3c1.addLink(b3, ClassifierIsARelationship.CLASS);
//    classifierStack.add(b3c1); 
//    
//    Classifier b3c2 = createClassifier("b3c2");
//    classifierIdMap.put("b3c2", b3c2.getId());
//    b3c2.addLink(b3, ClassifierIsARelationship.CLASS);
//    classifierStack.add(b3c2);     
//  }
//
//  private static Classifier createClassifier(String classifierId)
//  {
//    Classifier classifier = new Classifier();
//    classifier.setClassifierId(classifierId);
//    classifier.getDisplayLabel().setDefaultValue(classifierId);
//    classifier.setClassifierPackage("TestPackage");
//    classifier.apply();
//    return classifier;
//  }
//
//  private static Classifier getClassifier(String classifierId)
//  {
//    String classifierKey = Classifier.buildKey("TestPackage", classifierId);
//    
//    return Classifier.getByKey(classifierKey);
//  }
//  
//  @AfterClass
//  public static void classTearDown() throws Exception
//  {
//    classTearDownRequest();
//
//    systemSession.logout();
//  }
//
//  @Request
//  private static void classTearDownRequest()
//  {
//    classTearDownTransaction();
//  }
//  
//  @Transaction
//  private static void classTearDownTransaction()
//  {
//    for (Classifier classifier : classifierStack)
//    {
//      classifier.delete();
//    }
//  }
//
//  
//  @Test
//  public void addSynonymChild()
//  {     
//    ClientRequestIF clientRequest = this.getRequest();
//    
//    ClassifierSynonymDTO sb1 = new ClassifierSynonymDTO(clientRequest);
//    ClassifierSynonymDTO sb1c1 = new ClassifierSynonymDTO(clientRequest);
//    
//    try
//    {
//      ClassifierDTO b1 = ClassifierDTO.get(clientRequest, classifierIdMap.get("b1"));
//      sb1.getDisplayLabel().setValue("sb1c1");
//      sb1.setClassifier(b1);
//      sb1.apply();
//      
//      ClassifierDTO b1c1 = ClassifierDTO.get(clientRequest, classifierIdMap.get("b1c1"));
//      sb1c1.getDisplayLabel().setValue("sb1c1");
//      sb1c1.setClassifier(b1c1);
//      sb1c1.apply();
//
//      List<MessageDTO> messageList = clientRequest.getMessages();
//      
//      Assert.assertEquals("Only one message is expected", 1, messageList.size());
//      
//      MessageDTO messageDTO = messageList.get(0);
//      
//      Assert.assertTrue("Message is not of the expected type", messageDTO instanceof PossibleAmbiguousSynonymDTO);
//    }
//    finally
//    {
//      if (!sb1.isNewInstance())
//      {
//        sb1.delete();
//      }
//      if (!sb1c1.isNewInstance())
//      {
//        sb1c1.delete();
//      }
//    }
//  }
//
//  @Test
//  public void addSynonymParent()
//  {     
//    ClientRequestIF clientRequest = this.getRequest();
//    
//    ClassifierSynonymDTO sb1 = new ClassifierSynonymDTO(clientRequest);
//    ClassifierSynonymDTO sb1c1 = new ClassifierSynonymDTO(clientRequest);
//    
//    try
//    {
//      ClassifierDTO b1c1 = ClassifierDTO.get(clientRequest, classifierIdMap.get("b1c1"));
//      sb1c1.getDisplayLabel().setValue("sb1c1");
//      sb1c1.setClassifier(b1c1);
//      sb1c1.apply();
//      
//      ClassifierDTO b1 = ClassifierDTO.get(clientRequest, classifierIdMap.get("b1"));
//      sb1.getDisplayLabel().setValue("sb1c1");
//      sb1.setClassifier(b1);
//      sb1.apply();
//      
//      List<MessageDTO> messageList = clientRequest.getMessages();
//      
//      Assert.assertEquals("Only one message is expected", 1, messageList.size());
//      
//      MessageDTO messageDTO = messageList.get(0);
//      
//      Assert.assertTrue("Message is not of the expected type", messageDTO instanceof PossibleAmbiguousSynonymDTO);
//    }
//    finally
//    {
//      if (!sb1.isNewInstance())
//      {
//        sb1.delete();
//      }
//      if (!sb1c1.isNewInstance())
//      {
//        sb1c1.delete();
//      }
//    }
//  }
//  
//  protected ClientRequestIF getRequest()
//  {
//    ClientRequestIF clientRequestIF = systemSession.getRequest();
//    clientRequestIF.setKeepMessages(false);
//
//    return clientRequestIF;
//  }
//
}
