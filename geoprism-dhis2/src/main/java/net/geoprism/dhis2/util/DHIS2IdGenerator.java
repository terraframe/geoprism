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
package net.geoprism.dhis2.util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * This code taken from DHIS2's core. We're going to generate our ids just like they would.
 * 
 * dhis2-core/dhis-2/dhis-api/src/main/java/org/hisp/dhis/common/CodeGenerator.java
 * https://github.com/dhis2/dhis2-core/blob/68eaa925c8785be6616a258f989e6029ecd918eb/dhis-2/dhis-api/src/main/java/org/hisp/dhis/common/CodeGenerator.java
 */
public class DHIS2IdGenerator
{
  public static final String letters = "abcdefghijklmnopqrstuvwxyz"
      + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  
  public static final String ALLOWED_CHARS = "0123456789" + letters;

  public static final int NUMBER_OF_CODEPOINTS = ALLOWED_CHARS.length();
  public static final int CODESIZE = 11;

  private static final Pattern CODE_PATTERN = Pattern.compile( "^[a-zA-Z]{1}[a-zA-Z0-9]{10}$" );

  /**
   * 192 bit, must be dividable by 3 to avoid padding "=".
   */
  private static final int URL_RANDOM_TOKEN_LENGTH = 24;
  
  /**
   * Generates a UID according to the following rules:
   * <ul>
   * <li>Alphanumeric characters only.</li>
   * <li>Exactly 11 characters long.</li>
   * <li>First character is alphabetic.</li>
   * </ul>
   * 
   * @return a UID.
   */
  public static String generateUid()
  {
      return generateCode( CODESIZE );
  }
      
  /**
   * Generates a pseudo random string with alphanumeric characters.
   * 
   * @param codeSize the number of characters in the code.
   * @return the code.
   */
  public static String generateCode( int codeSize )
  {
      Random sr = new Random();

      char[] randomChars = new char[codeSize];
      
      // First char should be a letter
      randomChars[0] = letters.charAt( sr.nextInt( letters.length() ) );
      
      for ( int i = 1; i < codeSize; ++i )
      {
          randomChars[i] = ALLOWED_CHARS.charAt( sr.nextInt( NUMBER_OF_CODEPOINTS ) );
      }
      
      return new String( randomChars );
  }
  
  /**
   * Tests whether the given code is a valid UID.
   * 
   * @param code the code to validate.
   * @return true if the code is valid.
   */
  public static boolean isValidUid( String code )
  {
      return code != null && CODE_PATTERN.matcher( code ).matches();
  }
  
  /**
   * Generates a random 32 character token to be used in URLs.
   * 
   * @return a token.
   */
//  public static String getRandomUrlToken()
//  {
//      SecureRandom sr = new SecureRandom();
//      byte[] tokenBytes = new byte[ URL_RANDOM_TOKEN_LENGTH ];
//      sr.nextBytes( tokenBytes );
//
//      return Base64Utils.encodeToUrlSafeString( tokenBytes );
//  }
}
