/**
 * This file is part of the XP framework
 *
 * $Id: QueryNormalizer.java 10195 2008-03-22 15:05:32Z kiesel $
 */
package net.xp_framework.lucene.query;

import java.util.StringTokenizer;
import java.util.ArrayList;

public class QueryNormalizer {
    public static String normalize(String query) {
      return QueryNormalizer.prepare(query);
    }
    
    public static String replaceChars(String query) {
      String tokens= "+-&|(){}[]!^?:\\";
      StringTokenizer st= new StringTokenizer(query, tokens, true);
      StringBuffer out= new StringBuffer((int)(query.length() * 1.5));

      while (st.hasMoreTokens()) {
          String token= st.nextToken();

          // Check for token
          if (1 == token.length()) {
              if (tokens.contains(token)) {
                  out.append("\\");
              }
          }

          out.append(token);
      }
      
      return out.toString();
    }
    
    protected static String prepare(String query) {
        StringTokenizer st= new StringTokenizer(query, " \r\n\t\"", true);
        StringBuffer out= new StringBuffer((int)(query.length() * 2));
        
        ArrayList<String> tokens= new ArrayList<String>();
        
        while (st.hasMoreTokens()) {
            String token= st.nextToken();
            
            if ("and" == token || "or" == token) {
                tokens.add("\"" + token + "\"");
            } else if ("\"" == token) {
                tokens.add("\"" + st.nextToken("\"") + "\"");
            } else {
                if (token.length() >= 2) {
                    tokens.add(QueryNormalizer.replaceChars(token));
                }
            }
        }
        
        for (int i= 0; i < tokens.size(); i++) {
            String token= tokens.get(i);
            out.append(token);
                
            if (!token.endsWith("*") && !token.endsWith("~")) {
                out.append("*");
            }

            if (i < tokens.size()- 1) {
                out.append(" AND ");
            }
        }
        
        return out.toString();
    }
}
