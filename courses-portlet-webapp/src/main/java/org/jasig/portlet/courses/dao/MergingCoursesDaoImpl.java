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

package org.jasig.portlet.courses.dao;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;
import java.util.List;

import javax.annotation.Resource;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.courses.model.xml.Course;
import org.jasig.portlet.courses.model.xml.CourseSummary;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermSummary;

/**
 * MergingCoursesDaoImpl merges together information from multiple data sources.
 * This implementation will merge together matching terms and courses that 
 * contain identical IDs.  In the case that two sources specify information for 
 * a single-value property (e.g. GPA), the last DAO in the list will be given
 * priority.  In the case of multi-valued list properties (instructors), the 
 * lists from each source will be merged together.
 * 
 * @author Jen Bourey, jennifer.bourey@gmail.com
 * @version $Revision$
 */
public class MergingCoursesDaoImpl implements ICoursesDao {

    protected final Log log = LogFactory.getLog(getClass());
    
    private List<ICoursesDao> courseDaos;
    
    /**
     * Set the list of course DAOs to be called.  This list will be called in
     * order, and conflicting information will be resolved by prioritizing 
     * information from the last DAO.
     * 
     * @param courseDaos
     */
    @Resource(name="courseDaos")
    public void setCourseDaos(List<ICoursesDao> courseDaos) {
        this.courseDaos = courseDaos;
    }
    
    

    @Override
    @Cacheable(cacheName="TermSummaryCache",
                keyGenerator = @KeyGenerator(
                name = "org.jasig.portlet.courses.cachekeygenerators.PortletRequestCacheKeyGenerator")
               )
    public TermSummary getTermSummary(PortletRequest request) {
        TermSummary summary = null;
        
        // iterate over the list of course DAOs
        for (ICoursesDao dao : courseDaos) {
            try {
                
                TermSummary daoSummary = dao.getTermSummary(request);
                
                if (summary == null) {
                    summary = daoSummary;
                } else {
                    mergeTermSummaries(summary, daoSummary);
                }
                
                
            } catch (Exception e) {
                log.error("Exception reading course dao", e);
            }
        }

        return summary;
    }

    @Override
    @Cacheable(cacheName="CourseSummaryCache",
                keyGenerator = @KeyGenerator(
                name = "org.jasig.portlet.courses.cachekeygenerators.PortletRequestCacheKeyGenerator"
                  )
               )
    public CourseSummary getCourseSummary(PortletRequest request, String termCode) {
        CourseSummary summary = null;
        
        // iterate over the list of course DAOs
        for (ICoursesDao dao : courseDaos) {
            try {
                
                CourseSummary daoSummary = dao.getCourseSummary(request, termCode);
                
                if (summary == null) {
                    summary = daoSummary;
                } else {
                    mergeCourseSummaries(summary, daoSummary);
                }
                
                
            } catch (Exception e) {
                log.error("Exception reading course dao", e);
            }
        }

        return summary;
    }

    protected void mergeTermSummaries(TermSummary original, TermSummary additional) {
        final List<Term> originalTerms = original.getTerms();
        
        for (Term t : additional.getTerms()) {
            // if this term already exists in the summary, merge 
            // information from this DAO into the existing entry
            final Term originalTerm = original.getTerm(t.getCode());
            if (original.getTerm(t.getCode()) != null) {
                //TODO nothing to merge as of yet
            }
            
            // if we haven't seen this term before, just add it to the
            // list
            else {
                originalTerms.add(t);
            }
        }
    }

    protected void mergeCourseSummaries(CourseSummary original, CourseSummary additional) {
        
        // overall credit total
        if (additional.getCredits() != null) {
            original.setCredits(additional.getCredits());
        }
        
        // overall GPA
        if (additional.getGpa() != null) {
            original.setGpa(additional.getGpa());
        }

        // merge the course lists for the existing entry
        // and new DAO
        for (Course c : additional.getCourses()) {
            Course course = original.getCourse(c.getCode());
            
            if (course != null) {
                mergeCourse(course, c);
            }
            
            else {
                original.getCourses().add(c);
            }
        }

    }
    
    /**
     * Merge together information from the additional course into the provided
     * original course.
     * 
     * @param original
     * @param additional
     */
    protected void mergeCourse(Course original, Course additional) {
        if (additional.getCredits() != null) {
            original.setCredits(additional.getCredits());
        }
        
        if (additional.getGrade() != null) {
            original.setGrade(additional.getGrade());
        }
        
        if (additional.getLocation() != null) {
            original.setLocation(additional.getLocation());
        }
        
        if (additional.getCourseUpdates() != null) {
            original.getCourseUpdates().addAll(additional.getCourseUpdates());
        }
        
        if (additional.getInstructors() != null) {
            original.getInstructors().addAll(additional.getInstructors());
        }
        
        if (additional.getSchool() != null) {
            original.setSchool(additional.getSchool());
        }
        
        if (additional.getMeetingTimes() != null) {
            original.setMeetingTimes(additional.getMeetingTimes());
        }
        
        if (additional.getTitle() != null) {
            original.setTitle(additional.getTitle());
        }
        
        if (additional.getUrl() != null) {
            original.setUrl(additional.getUrl());
        }
    }
    
}
