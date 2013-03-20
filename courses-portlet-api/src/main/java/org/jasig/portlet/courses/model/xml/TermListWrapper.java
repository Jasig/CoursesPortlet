/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.courses.model.xml;

import java.math.BigInteger;
import java.util.List;

import org.joda.time.DateTime;


/**
 * Adds base functionality to the {@link TermSummary} object
 * 
 * @author Eric Dalquist
 */
public abstract class TermListWrapper {
    
    public Term getTerm(String termCode) {
        
        for (Term term : getTerms()) {
            if (termCode.equals(term.getCode())) {
                return term;
            }
        }

        return null;
    }

    public Term getCurrentTerm() {
        Term bestDateMatch = null;
        int bestDist = Integer.MAX_VALUE;
        final int currentYear = DateTime.now().year().get();
        
        
        for (Term term : getTerms()) {
            if (term.isCurrent()) {
                return term;
            }
            
            //If terms have years set determine a fall-back term based on the current year and term years
            final BigInteger termYear = term.getYear();
            if (termYear != null) {
                if (bestDateMatch == null) {
                    bestDateMatch = term;
                    bestDist = Math.abs(termYear.intValue() - currentYear);
                }
                else {
                    final int dist = Math.abs(termYear.intValue() - currentYear);
                    if (dist < bestDist) {
                        bestDateMatch = term;
                    }
                }
            }
        }

        return bestDateMatch;
    }
    
    public abstract List<Term> getTerms();
}
